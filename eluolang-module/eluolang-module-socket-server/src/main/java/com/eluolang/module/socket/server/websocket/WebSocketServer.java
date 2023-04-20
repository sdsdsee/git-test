package com.eluolang.module.socket.server.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.CacheConstant;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.hardware.dto.EllDeviceInfoStateDto;
import com.eluolang.common.core.hardware.dto.PhysicalTrainDto;
import com.eluolang.common.core.hardware.vo.FaceDetectData;
import com.eluolang.common.core.hardware.vo.NowRunDataVo;
import com.eluolang.common.core.pojo.EllDailyExercisePlan;
import com.eluolang.common.core.pojo.EllUser;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.common.core.util.FileUtils;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.common.redis.service.RedisService;
import com.eluolang.module.socket.server.model.FaceComparisonVo;
import com.eluolang.module.socket.server.service.UserService;
import com.eluolang.module.socket.server.util.MegviiApiUtil;
import com.eluolang.module.socket.server.vo.FaceDataVo;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class.getName());
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private volatile static List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    /**
     * 接收userId
     */
    private String userId = "";
    private Integer stateTime = 120;
    private Integer status = 1;
    private String heart = "DEVICE_HEART";
    private String device = "DEVICE_CONNECT";

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;
    private static RedisService redis;

    private static UserService user;

    @PostConstruct
    public void init() {
        redis = redisService;
        user = userService;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        sessionList.add(session);
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            webSocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
            sendInfo(WebSocketConstant.PING, userId);
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        //发送命令提醒设备离线
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "deviceOffline");
        Map<String, Object> deviceMap = new HashMap<>();
        deviceMap.put("deviceId", userId);
        jsonObject.put("data",deviceMap);
        sendToAll(jsonObject.toJSONString());
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 接收二进制报文（智慧操场人脸识别记录接收缓存至redis）
     * capture抓拍记录会传输人脸小图数据，需要设置maxMessageSize大小
     *
     * @param message
     * @param session
     */
    @OnMessage(maxMessageSize = 3000000)
    public void handleBinaryMessage(ByteBuffer message, Session session) throws IOException {
        String userId = StringUtils.strip(session.getRequestParameterMap().get("userId").toString(), "[]");
//        log.info("onMessage::inputstream"+userId);

        //capture抓拍消息发送协议采用HEADER（16字节定长）+JSON+PIC（二进制）的组合方式进行发送
        //header由type（目前使用有人脸识别:Alerts,人脸抓拍:Capture,8字节），headerlen（后面json的长度，4字节），seq（上传数据序列，二进制表示，4个字节长度，每次发送加1，主要用于确认是否有丢数据包）
        byte[] alert = new byte[message.limit()];
        message.get(alert);

        //header type
//        String str = null;
//        try {
//            str = new String(alert, 0, 8, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //json len
        int json_len = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            json_len += (alert[i + 8] & 0xFF) << shift;
        }

        //seq
        int seq = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            seq += (alert[i + 12] & 0xFF) << shift;
        }

//        System.out.println("type:"+ str + " json length:" + json_len + " seq:" + seq);

        //json
        String json = null;
        try {
            json = new String(alert, 16, json_len, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        System.out.println("json:"+ json);

        //解析json，以Capture为例，其他请参考协议
        JSONObject object = JSONObject.parseObject(json);
        //视频通道号
        int video_num = object.getIntValue("videoNo");
        //摄像头布点或抓拍机布点名称，就是videoName 或 pictureName，根据工作模式workMode设置来决定
        String channel_name = object.getString("channelName");
        //视频中的一个人脸或一张抓拍机图片唯一编号，用于调用captrues接口调取抓拍图片使用
        int track_id = object.getIntValue("trackId");
        //base64的抓拍人脸图数据
        int captureFace_len = object.getIntValue("captureFace");
        //抓拍全景图的url
        int fullImage_len = object.getIntValue("fullImage");
        //识别记录
        if (userId.equals("Alert")) {
            try {
                long timestamp = object.getLongValue("timestamp");
                //回复ACK
                sendInfo(userId + "_" + video_num + "_" + timestamp + "_" + track_id + "_ACK", userId);
                FaceDetectData faceDetectData = JSONObject.parseObject(StringEscapeUtils.unescapeJava(object.toString()), FaceDetectData.class);
                if (faceDetectData.getChannelName() != null) {
                    EllUser ellUser = user.selSignUserByCode(faceDetectData.getImageId());
                    if (ellUser != null) {
                        String[] channelName = faceDetectData.getChannelName().split("_");
                        EllDailyExercisePlan ellDailyExercisePlan = user.selPlanDataById(Integer.valueOf(ellUser.getOrgId()));
                        //判断该学校是否有日常锻炼计划
                        if (ellDailyExercisePlan != null) {
                            //查询是否已检录，未查询到则检录，否则刷新已检录人员的缓存时间
                            PhysicalTrainDto dto = redis.getCacheObject(CacheConstant.RUNNING_FREE_KEY + ellDailyExercisePlan.getId() + ":" + ellUser.getId());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (dto == null) {
                                PhysicalTrainDto physicalTrainDto = new PhysicalTrainDto();
                                physicalTrainDto.setMac(faceDetectData.getImageId());
                                physicalTrainDto.setStartTime(DateUtils.timeStamp3Date(String.valueOf(faceDetectData.getTimestamp()), DateUtils.YYYY_MM_DD_HH_MM_SS));
                                physicalTrainDto.setDailyId(ellDailyExercisePlan.getId());
                                physicalTrainDto.setUserId(ellUser.getId());
                                physicalTrainDto.setLocation(channelName[0]);
                                redis.setCacheObject(CacheConstant.RUNNING_FREE_KEY + physicalTrainDto.getDailyId() + ":" + physicalTrainDto.getUserId(), physicalTrainDto, 5, TimeUnit.MINUTES);
                            } else {
                                if (dto.getLocation().equals(channelName[0])) {
                                    redis.refresh(CacheConstant.RUNNING_FREE_KEY + dto.getDailyId() + ":" + dto.getUserId(), 5, TimeUnit.MINUTES);
                                } else {
                                    dto.setLocation(channelName[0]);
                                    redis.setCacheObject(CacheConstant.RUNNING_FREE_KEY + dto.getDailyId() + ":" + dto.getUserId(), dto, 5, TimeUnit.MINUTES);
                                    Date afterTime = new Date(faceDetectData.getTimestamp() + 5 * 60 * 1000);
                                    NowRunDataVo nowRunDataVo = new NowRunDataVo();
                                    nowRunDataVo.setDailyId(dto.getDailyId());
                                    nowRunDataVo.setUserId(dto.getUserId());
                                    nowRunDataVo.setStartTime(dto.getStartTime());
                                    nowRunDataVo.setEndTime(dateFormat.format(afterTime));
                                    redis.setCacheObject(CacheConstant.NOW_RUNNING_TIME + dto.getDailyId() + ":" + dto.getUserId(), nowRunDataVo);
                                }

                            }
                        }

                        faceDetectData.setChannelName(channelName[0]);
                        redis.setCacheObject(
                                CacheConstant.FACE_KEY + faceDetectData.getImageId() + ":" + DateUtils.timeStamp3Date(String.valueOf(faceDetectData.getTimestamp()), DateUtils.YYYYMMDDHHMMSS), faceDetectData, 1, TimeUnit.DAYS);
                    }

//                log.info("Alert========"+object);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //抓拍记录
        if (userId.equals("Capture")) {
            long timestamp = object.getLongValue("catchTimestamp");
            //回复ACK
            sendInfo(userId + "_" + video_num + "_" + timestamp + "_" + track_id + "_ACK", userId);
            // 接受抓拍小图
            if (captureFace_len != 0) {
                byte[] captureFace_pic = new byte[captureFace_len];
                System.arraycopy(alert, 8 + 4 + 4 + json_len, captureFace_pic, 0, captureFace_len);

                OutputStream os = null;
                String path = "";
                //判断是否为windows
                if (FileUploadUtil.isLinux() == false) {
                    path = WindowsSite.WINDOWS_FACE_PATH;
                } else {
                    path = LinuxSite.LINUX_FACE_PATH;
                }
                File tempFile = new File(path);
                //检测上传的文件目录是否存在
                if (!tempFile.getParentFile().exists()) {
                    //不存在则新建文件夹
                    tempFile.getParentFile().mkdirs();
                }
                //将人脸图片保存到本地，图片命名格式为trackId + .jpeg
                try {
                    os = new FileOutputStream(tempFile.getPath() + File.separator + track_id +
                            ".jpeg");
                    os.write(captureFace_pic, 0, captureFace_len);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    //调用旷视1:n人脸算法接口，异常会被log捕获，异常情况object为null，报错内容一般为400未检测到人脸
                    Object objectData = MegviiApiUtil.search(tempFile.getPath() + File.separator + track_id + ".jpeg");
                    if (objectData != null) {
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(objectData);
                        //接口code为0表示为调用成功
                        if (jsonObject.get("code").toString().equals("0")) {
                            //获取返回结果中排名第一的人脸相似度数据
                            JSONObject data = (JSONObject) JSONObject.toJSON(jsonObject.get("data"));
                            FaceComparisonVo faceComparisonVo = JSON.parseObject(JSON.toJSONString(data.get("top1")), FaceComparisonVo.class);
                            ;
                            //判断排名第一的相似度人脸值是否大于60.0,大于此阈值则通过人脸验证
                            if (faceComparisonVo.getSearchScore() >= 60.0) {
                                //调用旷视根据faceToken查询imageId接口
                                Result result = MegviiApiUtil.faceToken(faceComparisonVo.getFaceToken());
                                if (result != null) {
                                    if (result.getCode() == 0) {
                                        FaceDataVo faceDataVo = JSONObject.parseObject(result.getData().toString(), FaceDataVo.class);
                                        EllUser ellUser = user.selSignUserByCode(faceDataVo.getImageId());
                                        if (ellUser != null) {
                                            String[] channelName = channel_name.split("_");

                                            FaceDetectData faceDetectData = new FaceDetectData();
                                            faceDetectData.setVideoNo(video_num);
                                            faceDetectData.setChannelName(channelName[0]);
                                            faceDetectData.setSearchScore(faceComparisonVo.getSearchScore());
                                            faceDetectData.setTrackId(track_id);
                                            faceDetectData.setImageId(faceDataVo.getImageId());
                                            faceDetectData.setTimestamp(timestamp);

                                            EllDailyExercisePlan ellDailyExercisePlan = user.selPlanDataById(Integer.valueOf(ellUser.getOrgId()));
                                            //查询该学校是否有日常锻炼计划
                                            if (ellDailyExercisePlan != null) {
                                                //查询是否已检录，未查询到则检录，否则刷新已检录人员的缓存时间
                                                PhysicalTrainDto dto = redis.getCacheObject(CacheConstant.RUNNING_FREE_KEY + ellDailyExercisePlan.getId() + ":" + ellUser.getId());
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                if (dto == null) {
                                                    PhysicalTrainDto physicalTrainDto = new PhysicalTrainDto();
                                                    physicalTrainDto.setMac(faceDataVo.getImageId());
                                                    physicalTrainDto.setStartTime(DateUtils.timeStamp3Date(String.valueOf(faceDetectData.getTimestamp()), DateUtils.YYYY_MM_DD_HH_MM_SS));
                                                    physicalTrainDto.setDailyId(ellDailyExercisePlan.getId());
                                                    physicalTrainDto.setUserId(ellUser.getId());
                                                    physicalTrainDto.setLocation(channelName[0]);
                                                    redis.setCacheObject(CacheConstant.RUNNING_FREE_KEY + physicalTrainDto.getDailyId() + ":" + physicalTrainDto.getUserId(), physicalTrainDto, 5, TimeUnit.MINUTES);
                                                } else {
                                                    //判断摄像头布点通道名是否与redis中的相同，相同刷新缓存，否则新增
                                                    if (dto.getLocation().equals(channelName[0])) {
                                                        redis.refresh(CacheConstant.RUNNING_FREE_KEY + dto.getDailyId() + ":" + dto.getUserId(), 5, TimeUnit.MINUTES);
                                                    } else {
                                                        dto.setLocation(channelName[0]);
                                                        redis.setCacheObject(CacheConstant.RUNNING_FREE_KEY + dto.getDailyId() + ":" + dto.getUserId(), dto, 5, TimeUnit.MINUTES);
                                                        Date afterTime = new Date(faceDetectData.getTimestamp() + 5 * 60 * 1000);
                                                        NowRunDataVo nowRunDataVo = new NowRunDataVo();
                                                        nowRunDataVo.setDailyId(dto.getDailyId());
                                                        nowRunDataVo.setUserId(dto.getUserId());
                                                        nowRunDataVo.setStartTime(dto.getStartTime());
                                                        nowRunDataVo.setEndTime(dateFormat.format(afterTime));
                                                        redis.setCacheObject(CacheConstant.NOW_RUNNING_TIME + dto.getDailyId() + ":" + dto.getUserId(), nowRunDataVo);
                                                    }
                                                }
                                            }
                                            faceDetectData.setChannelName(channelName[0]);
                                            //将识别出的人脸数据存入redis，缓存时间限制一天
                                            redis.setCacheObject(
                                                    CacheConstant.FACE_KEY + faceDetectData.getImageId() + ":" + DateUtils.timeStamp3Date(String.valueOf(faceDetectData.getTimestamp()), DateUtils.YYYYMMDDHHMMSS), faceDetectData, 1, TimeUnit.DAYS);

                                        }
                                    }
                                }
                            }
                        }
                    }
                    //删除本次人脸抓拍信息保存在本地的图片
                    FileUtils.deleteFile(tempFile.getPath() + File.separator + track_id + ".jpeg");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //接收抓拍全景图
//            if (fullImage_len != 0) {
//                byte[] fullImage_pic = new byte[fullImage_len];
//                System.arraycopy(alert, 8 + 4 + 4 + json_len + captureFace_len, fullImage_pic, 0, fullImage_len);
//
//                OutputStream os = null;
//                File tempFile = new File("E:\\");
//                try {
//                    os = new FileOutputStream(tempFile.getPath() + File.separator + "fullImage.jpeg");
//                    os.write(fullImage_pic, 0, fullImage_len);
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
            }
//            log.info("Capture========"+object);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (message.equals("DEVICE_RECEIVED")) {
            log.info("用户消息:" + userId + ",报文:" + message);
        }
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotNull(message)) {
            try {
                if (message.equals(device)) {
                    //保存大屏设备id
                    redis.setCacheObject(CacheConstant.LARGE_SCREEN_DEVICE + this.userId, this.userId);
                }
                if (message.equals(heart)) {
                    //保存设备的心跳信息
                    redis.setCacheObject(CacheConstant.DEVICE_STATE + this.userId, status, stateTime, TimeUnit.SECONDS);
                }
//                //解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserId",this.userId);
//                String toUserId=jsonObject.getString("fromUserId");
//                //传送给对应toUserId用户的websocket
//                if(webSocketMap.containsKey(toUserId)){
//                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//                }else{
//                    log.error("请求的userId:"+toUserId+"不在该服务器上");
//                    //否则不在这个服务器上，发送到mysql或者redis
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 异常
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        //发送命令提醒设备离线
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "deviceOffline");
        Map<String, Object> deviceMap = new HashMap<>();
        deviceMap.put("deviceId", userId);
        jsonObject.put("data",deviceMap);
        sendToAll(jsonObject.toJSONString());
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        try {
            if (sessionList.size() != 0) {
                for (Session s : sessionList) {
                    if (s != null && s.isOpen()) {
                        s.getBasicRemote().sendText(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            for (WebSocketServer webSocketServer : webSocketMap.values()) {
                if (webSocketServer.userId.equals(userId)) {
                    webSocketServer.session.getAsyncRemote().sendText(message);
                    break;
                }
            }
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * 广播
     */
    public static void sendToAll(String message) throws IOException {
        log.info("发送消息到:报文:" + message);
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            webSocketServer.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
