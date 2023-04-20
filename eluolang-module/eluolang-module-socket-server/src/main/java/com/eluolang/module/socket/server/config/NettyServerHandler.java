package com.eluolang.module.socket.server.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.HardwareStatus;
import com.eluolang.common.core.constant.HttpStatus;

import com.eluolang.common.core.hardware.Ack;
import com.eluolang.common.core.hardware.dto.OneClickHelpDto;
import com.eluolang.common.core.pojo.EllDeviceAlarm;
import com.eluolang.common.core.util.DateUtils;
import com.eluolang.common.core.util.StringUtils;
import com.eluolang.common.core.web.Result;
import com.eluolang.module.socket.server.service.feign.DeviceManageRemoteService;
import com.eluolang.module.socket.server.service.feign.PlatformRemoteService;
import com.eluolang.module.socket.server.util.Constant;
import com.eluolang.module.socket.server.util.GatewayService;
import com.eluolang.module.socket.server.util.SpringUtil;
import com.eluolang.module.socket.server.websocket.WebSocketConstant;
import com.eluolang.module.socket.server.websocket.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author renzhixing
 * <p>
 * netty服务端处理器
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class.getName());

    //获取定义调用设备微服务的Bean对象
    DeviceManageRemoteService deviceManageRemoteService = (DeviceManageRemoteService) SpringUtil.getBean(DeviceManageRemoteService.class);
    //获取定义调用设备微服务的Bean对象
    PlatformRemoteService platformRemoteService = (PlatformRemoteService) SpringUtil.getBean(PlatformRemoteService.class);
//    //获取定义调用系统微服务的Bean对象
//    SystemManageRemoteService systemManageRemoteService = (SystemManageRemoteService) SpringUtil.getBean(SystemManageRemoteService.class);
//    //获取定义调用报表微服务的Bean对象
//    ReportManageRemoteService reportManageRemoteService = (ReportManageRemoteService) SpringUtil.getBean(ReportManageRemoteService.class);
//    //获取定义调用区域微服务的Bean对象
//    AreaManageRemoteService areaManageRemoteService = (AreaManageRemoteService) SpringUtil.getBean(AreaManageRemoteService.class);
//    //获取定义调用版本微服务的Bean对象
//    VersionManageRemoteService versionManageRemoteService = (VersionManageRemoteService) SpringUtil.getBean(VersionManageRemoteService.class);
//    //获取定义调用设备接口的Bean对象
//    PlatformCallHardwareService platformCallHardwareService = (PlatformCallHardwareService) SpringUtil.getBean(PlatformCallHardwareService.class);
//    //获取定义调用redis的Bean对象
//    RedisService redisService = (RedisService) SpringUtil.getBean(RedisService.class);
//    //获取定义调用rabbitmq的Bean对象
//    RabbitSendService rabbitSendService = (RabbitSendService) SpringUtil.getBean(RabbitSendService.class);

    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
//        GatewayService.addGatewayChannel(uuid,(SocketChannel)ctx.channel());
        System.out.println("a new connect come in: " + uuid);
        System.out.println("Channel active......");
    }

    /**
     * 客户端发消息会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NewDeviceProtocol protocol = (NewDeviceProtocol) msg;
        Receive((NewDeviceProtocol) msg, ctx);
        ctx.flush();
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 向指定客户端发送消息
     *
     * @param key
     * @param deviceProtocol
     */
    public static Integer sendClientData(String key, NewDeviceProtocol deviceProtocol) {
        //通过key去拿到该设备对应的连接通道
        try {
            SocketChannel socketChannel = GatewayService.getGatewayChannel(key);
            socketChannel.writeAndFlush(deviceProtocol);
            logger.info("send device--->" + DateUtils.getTime());
        } catch (Exception e) {
            logger.info("send device Error--->" + DateUtils.getTime());
            return Constant.ERROR;
        }
        return Constant.SUCCESS;
    }

    /**
     * 处理设备上传的信息
     *
     * @param deviceProtocol
     * @param ctx
     */
    public void Receive(NewDeviceProtocol deviceProtocol, ChannelHandlerContext ctx) {
        logger.info("Receive===" + deviceProtocol + "---hexString[data]===" + StringUtils.bytesToHexString(deviceProtocol.getData()));
        Result result = null;
        DeferredResult<Result> deferredResult = null;
        JSONObject jsonObject = null;
        //校验和的数据
        String data = "";
        try {
            Result deviceResult;
            //解析cmd控制命令
            short cmd = deviceProtocol.getCmd();
            //如果控制命令不是文件传输包,则将传递的data数据内容转化成json字符串
            if (cmd != HardwareStatus.DOWNLOAD_FILE_INFO) {
                data = StringUtils.byteToString(deviceProtocol.getData());
                logger.info("json===" + data);
                jsonObject = JSON.parseObject(data);
            }
            //如果传递的命令码是上传设备信息,读取出设备id作为连接通道的key中,在map中进行管理
            if (cmd == HardwareStatus.UPLOAD_DEVICE_INFO || cmd == HardwareStatus.HEARTBEAT_DEVICE_STATUS) {
                GatewayService.addGatewayChannel(jsonObject.getString("deviceId"), (SocketChannel) ctx.channel());
            }
            switch (cmd) {
                case HardwareStatus.UPLOAD_DEVICE_USE_ELECTRICITY_AMOUNT:
                    //调用保存设备每天用电量
                    result = deviceManageRemoteService.saveUseElectricityAmount(data);
                    //下发返回参数响应给硬件
                    ctx.write(response(Constant.HEAD, cmd, result.getData().toString()));
                    break;
                case HardwareStatus.HEARTBEAT_DEVICE_STATUS:
                    //调用保存心跳信息方法,将信息保存在redis缓存中
                    result = deviceManageRemoteService.saveHeartBeat(data);
                    //下发返回参数响应给硬件
                    ctx.write(response(Constant.HEAD, cmd, result.getData().toString()));
                    break;
                case HardwareStatus.DEVICE_ENVIRONMENT_PARAM:
                    //调用保存设备环境参数方法,将信息保存在redis缓存中
                    result = deviceManageRemoteService.saveDeviceEnvironmentParam(data);
                    //下发返回参数响应给硬件
                    ctx.write(response(Constant.HEAD, cmd, result.getData().toString()));
                    break;
                case HardwareStatus.ONE_CLICK_HELP:
                    //封装设备响应的一键求助信息对象
                    OneClickHelpDto dto = JSON.parseObject(data, OneClickHelpDto.class);
                    EllDeviceAlarm ellDeviceAlarm = new EllDeviceAlarm();
                    ellDeviceAlarm.setDeviceId(dto.getDeviceId());
                    Result r =  platformRemoteService.addAlarm(ellDeviceAlarm);
                    if (r.getCode() == HttpStatus.ERROR){
                        throw new Exception(r.getMessage());
                    }
                    result = deviceManageRemoteService.selOperIdByDeviceId(dto.getDeviceId());
                    if (result.getCode() == HttpStatus.ERROR) {
                        throw new Exception(result.getMessage());
                    }
                    for (Object object : (List<?>) result.getData()) {
                        WebSocketServer.sendInfo(WebSocketConstant.ONE_CLICK_HELP + ":" + dto.getDeviceId(), object.toString());
                    }
                    //下发返回参数响应给硬件
                    ctx.write(response(Constant.HEAD, cmd, result.getData().toString()));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        try {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.READER_IDLE)) {
                    logger.info(ctx.channel().remoteAddress() + "超时事件");
                    logger.info("心跳周期内没有收到客户端的心跳,关闭连接");
                    ctx.channel().close();
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接了");
    }

    /**
     * 将需要响应的给设备的信息进行统一封装
     *
     * @param head
     * @param cmd
     * @param result
     * @return
     */
    private NewDeviceProtocol response(byte[] head, short cmd, String result) {
        NewDeviceProtocol deviceProtocol = null;
        try {
            short length = (short) result.getBytes().length;
            //将读出的数据合并和字节数组,获取校验和
//            byte[] b1 = StringUtils.byteMerger(head, StringUtils.shortToByte(cmd));
//            byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte(length), result.getBytes());
//            short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
            //封装的协议对象下发
            deviceProtocol = new NewDeviceProtocol(head, cmd, length, result);
            logger.info("响应包" + deviceProtocol + "===data" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceProtocol;
    }


    /**
     * 将需要响应的给设备的信息二进制协议进行统一封装
     *
     * @param head
     * @param cmd
     * @param result
     * @return
     */
    private NewDeviceProtocol responseBinary(byte[] head, short cmd, byte[] result) {
        NewDeviceProtocol deviceProtocol = null;
        try {
            short length = (short) result.length;
            //将读出的数据合并和字节数组,获取校验和
//            byte[] b1 = StringUtils.byteMerger(head, StringUtils.shortToByte(cmd));
//            byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte(length), result);
//            byte[] b3 = StringUtils.byteMerger(b1, b2);
//            short sumCheck = StringUtils.sumCheck(b3, b3.length);
            //封装的协议对象下发
            deviceProtocol = new NewDeviceProtocol(head, cmd, length, result);
            System.out.println("响应包" + deviceProtocol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceProtocol;
    }


    /**
     * 封装ACK响应
     *
     * @param head
     * @param cmd
     * @param jsonObject
     * @return
     */
    private NewDeviceProtocol ack(byte[] head, short cmd, JSONObject jsonObject) {
        NewDeviceProtocol deviceProtocol = null;
        try {
            String ackJson = JSON.toJSONString(new Ack(HardwareStatus.ACK_FEEDBACK, cmd, jsonObject.getIntValue("ident")));
            short length = (short) ackJson.getBytes().length;
            //将读出的数据合并和字节数组,获取校验和
//            byte[] b1 = StringUtils.byteMerger(head, StringUtils.shortToByte((short) HardwareStatus.ACK_FEEDBACK));
//            byte[] b2 = StringUtils.byteMerger(StringUtils.shortToByte(length), ackJson.getBytes());
//            short sumCheck = StringUtils.sumCheck(StringUtils.byteMerger(b1, b2), StringUtils.byteMerger(b1, b2).length);
//            System.out.println("校验和:" + sumCheck);
            //封装的协议对象下发
            deviceProtocol = new NewDeviceProtocol(head, (short) HardwareStatus.ACK_FEEDBACK, length, ackJson);
            System.out.println(deviceProtocol.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceProtocol;
    }

//    private void handleResponseMsg(String message) {
//        //将message转成JsonObject
//        JSONObject jsonObject = JSON.parseObject(message);
//        //封装操作日志对象,将设备响应结果信息进行保存
//        OperLogDetail operLogDetail = new OperLogDetail();
//        operLogDetail.setDeviceId(jsonObject.getString("deviceId"));
//        operLogDetail.setResponseTime(DateUtils.getCurrentTime());
//        //判断是正常指令响应还是异常指令
//        if (jsonObject.getIntValue("cmd") == HardwareStatus.ERROR_FEEDBACK) {
//            operLogDetail.setIdent(jsonObject.getIntValue("err_ident"));
//            //设置错误原因
//            operLogDetail.setErrorMsg(jsonObject.getString("msg"));
//        } else {
//            operLogDetail.setIdent(jsonObject.getIntValue("ident"));
//        }
//        //设置操作类型
//        operLogDetail.setOperResult(jsonObject.getIntValue("code"));
//        //调用系统管理服务的修改操作日志详情的方法
//        systemManageRemoteService.updateOperLogDetail(operLogDetail);
//    }

    public String getIpAddress(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return inetSocketAddress.getAddress().getHostAddress();
    }


    /**
     * 授权响应处理
     *
     * @param authHouseholdOpenDoorVo
     */
//    public void handleAuthMsg(AuthHouseholdOpenDoorVo authHouseholdOpenDoorVo) throws InterruptedException {
//        //先查询出授权的住户信息
//        String authData = redisService.getCacheObject(Constant.IDENT + authHouseholdOpenDoorVo.getIdent());
//        AuthHouseholdOpenDoorDto authHouseholdOpenDoorDto = JSON.parseObject(authData, AuthHouseholdOpenDoorDto.class);
//        //获取授权住户id信息
//        String householdId = authHouseholdOpenDoorDto.getData().get(0).getHouseholdId();
//        //调用修改开门权限状态的方法
//        List<DeviceHousehold> deviceHouseHoldDtos = new ArrayList<>();
//        DeviceHousehold deviceHousehold = new DeviceHousehold();
//        deviceHousehold.setHouseholdId(householdId);
//        deviceHousehold.setDeviceId(authHouseholdOpenDoorVo.getDeviceId());
//        if (authHouseholdOpenDoorVo.getCode() == Constant.SUCCESS) {
//            deviceHousehold.setState(2);  //2 表示权限已激活
//            //判断权限中是否包含人脸信息,如果有人脸信息,需要进行文件传输将人脸图片下发到设备当中
//            List<AuthHouseholdOpenDoorDto.DataBean.AuthInfoBean> list = authHouseholdOpenDoorDto.getData().get(0).getAuthInfo();
//            if (StringUtils.isNotNull(list) && list.size() > 0) {
//                FileMgr fileMgrInfo = null;
//                List<DownloadFileInfoDto> downloadFileInfoDtos = new ArrayList<>();
//                for (int j = 0; j < list.size(); j++) {
//                    if (list.get(j).getAuthType() == 1 && list.get(j).getContentType() == 1) {
//                        //通过下发的文件id查询文件大小s信息,进行分包发送
//                        logger.info(list.get(j).getAuthContent().split("\\.")[0]);
//                        fileMgrInfo = JSON.parseObject(versionManageRemoteService.getFileInfo(list.get(j).getAuthContent().split("\\.")[0])
//                                .getData().toString(), FileMgr.class);
//                        int fileSize = (int) fileMgrInfo.getSize();
//                        int totalAmount = (int) ((double) fileSize / Constant.MAX_PAG_LEN <= 1 ? 1 : (double) fileSize / Constant.MAX_PAG_LEN + 1);
//                        byte[] faceBytes = FileUtils.getBytesByFile(fileMgrInfo.getFileUrl());
//                        for (int i = 0; i < totalAmount; i++) {
//                            byte[] bytes;
//                            if (i == totalAmount - 1) {
//                                logger.info("fileSize--->" + fileSize);
//                                bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, fileSize - (i * Constant.MAX_PAG_LEN));
//                            } else {
//                                bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, Constant.MAX_PAG_LEN);
//                            }
//                            //封装下发文件的参数
//                            DownloadFileInfoDto downloadFileInfoDto = new DownloadFileInfoDto();
//                            downloadFileInfoDto.setIdent((Integer) systemManageRemoteService.getIdent().getData());
//                            logger.info("get ident ------------" + downloadFileInfoDto.getIdent());
//                            downloadFileInfoDto.setDeviceId(authHouseholdOpenDoorDto.getDeviceId());
//                            downloadFileInfoDto.setId(fileMgrInfo.getFileName());
//                            downloadFileInfoDto.setTotalAmount(totalAmount);
//                            downloadFileInfoDto.setWhichPackage(i + 1);
//                            downloadFileInfoDto.setByteStream(bytes);
//                            if (i == 0) {
//                                //将第一包数据保存到redis当中,使用指令标识作为key,等下设备收到每包给出响应后,再继续进行下发,
//                                // 存一个Copy key方便当前IDENT过期超时处理
//                                redisService.setCacheObject(Constant.FILE_IDENT + downloadFileInfoDto.getIdent(),
//                                        downloadFileInfoDto, Constant.FILE_IDENT_TIME, TimeUnit.SECONDS);
//                                redisService.setCacheObject(Constant.COPY_FILE_IDENT + downloadFileInfoDto.getIdent(), downloadFileInfoDto);
//                                //调用下发文件接口,将第一包文件进行下发
//                                Integer resultFile = platformCallHardwareService.downloadFileInfo(downloadFileInfoDto);
//                                //如果下发指令到硬件失败,给出提示信息
//                                if (resultFile.equals(Constant.ERROR)) {
//                                    logger.error("调用下发文件接口失败");
//                                } else {
//                                    logger.info("调用下发文件接口成功");
//                                }
//                                logger.info("totalAmount====" + totalAmount + "---whichPackage===" + downloadFileInfoDto.getWhichPackage());
//                            }
//                            //将每包文件封装到List集合存入redis中
//                            downloadFileInfoDtos.add(downloadFileInfoDto);
//                        }
//                        //将封装了每包人脸文件的数据包List进行存储
//                        redisService.setCacheObject(Constant.FILE + fileMgrInfo.getFileName() + downloadFileInfoDtos.get(0).getIdent(), downloadFileInfoDtos);
//                        //授权处理完成后将redis中存储的授权信息进行删除
//                        redisService.deleteObject(Constant.IDENT + authHouseholdOpenDoorVo.getIdent());
//                        break;
//                    }
//                }
//            }
//        } else {
//            deviceHousehold.setState(1);  //1 表示权限未激活
//        }
//        deviceHouseHoldDtos.add(deviceHousehold);
//        //调用修改住户权限信息状态的方法
//        /*Result result = deviceManageRemoteService.updateAuthorityState(deviceHouseHoldDtos);
//        if (result.getCode() == HttpStatus.ERROR) {
//            logger.error("修改权限状态错误");
//        }*/
//    }

//    /**
//     * 信息发布后进行下发文件
//     *
//     * @param infoDeliveryVo
//     */
//    public void handleMsgDelivery(InfoDeliveryVo infoDeliveryVo) {
//        logger.info("进入信息发布下发文件方法==" + JSONObject.toJSONString(infoDeliveryVo));
//        //先查询出信息发布的数据
//        String deliveryData = redisService.getCacheObject(Constant.IDENT + infoDeliveryVo.getIdent());
//        logger.info("根据ident" + infoDeliveryVo.getIdent() + "==查询出的信息发布数据" + deliveryData);
//        InfoDeliveryDto infoDeliveryDto = JSON.parseObject(deliveryData, InfoDeliveryDto.class);
//        //信息发布后修改发布记录信息的状态
//        systemManageRemoteService.updateDeliveryState(infoDeliveryDto.getDeliveryId(), infoDeliveryVo.getCode() == 0 ? 1 : 2);
//        if (infoDeliveryVo.getCode() == Constant.SUCCESS) {
//            List<DownloadFileInfoDto> downloadFileInfoDtos = new ArrayList<>();
//            //通过下发的文件id查询文件大小信息,进行分包发送
//            FileMgr fileMgrInfo = JSON.parseObject(versionManageRemoteService.getFileInfo(infoDeliveryDto.getFileId().split("\\.")[0])
//                    .getData().toString(), FileMgr.class);
//            int fileSize = (int) fileMgrInfo.getSize();
//            logger.info("文件信息==" + JSONObject.toJSONString(fileMgrInfo));
//            int totalAmount = (int) ((double) fileSize / Constant.MAX_PAG_LEN <= 1 ? 1 : (double) fileSize / Constant.MAX_PAG_LEN + 1);
//            byte[] faceBytes = FileUtils.getBytesByFile(fileMgrInfo.getFileUrl());
//            for (int i = 0; i < totalAmount; i++) {
//                logger.info("totalAmount====" + totalAmount);
//                byte[] bytes;
//                if (i == totalAmount - 1) {
//                    logger.info("fileSize--->" + fileSize);
//                    bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, fileSize - (i * Constant.MAX_PAG_LEN));
//                } else {
//                    bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, Constant.MAX_PAG_LEN);
//                }
//                //封装下发文件的参数
//                DownloadFileInfoDto downloadFileInfoDto = new DownloadFileInfoDto();
//                downloadFileInfoDto.setIdent((Integer) systemManageRemoteService.getIdent().getData());
//                downloadFileInfoDto.setDeviceId(infoDeliveryVo.getDeviceId());
//                downloadFileInfoDto.setId(fileMgrInfo.getFileName());
//                downloadFileInfoDto.setTotalAmount(totalAmount);
//                downloadFileInfoDto.setWhichPackage(i + 1);
//                downloadFileInfoDto.setByteStream(bytes);
//                if (i == 0) {
//                    //将第一包数据保存到redis当中,使用指令标识作为key,等下设备收到每包给出响应后,再继续进行下发,
//                    // 存一个Copy key方便当前IDENT过期超时处理
//                    redisService.setCacheObject(Constant.FILE_IDENT + downloadFileInfoDto.getIdent(),
//                            downloadFileInfoDto, Constant.FILE_IDENT_TIME, TimeUnit.SECONDS);
//                    redisService.setCacheObject(Constant.COPY_FILE_IDENT + downloadFileInfoDto.getIdent(), downloadFileInfoDto);
//                    //调用下发文件接口,将第一包文件进行下发
//                    Integer resultFile = platformCallHardwareService.downloadFileInfo(downloadFileInfoDto);
//                    //如果下发指令到硬件失败,给出提示信息
//                    if (resultFile.equals(Constant.ERROR)) {
//                        logger.error("调用下发文件接口失败");
//                    } else {
//                        logger.info("调用下发文件接口成功");
//                    }
//                }
//                //将每包文件封装到List集合存入redis中
//                downloadFileInfoDtos.add(downloadFileInfoDto);
//            }
//            //将封装了每包人脸文件的数据包List进行存储
//            redisService.setCacheObject(Constant.FILE + fileMgrInfo.getFileName() + downloadFileInfoDtos.get(0).getIdent(), downloadFileInfoDtos);
//            //授权处理完成后将redis中存储的授权信息进行删除
//            redisService.deleteObject(Constant.IDENT + infoDeliveryVo.getIdent());
//        }
//    }

    /**
     * 升级固件后进行下发文件
     *
     * @param updateFirmwareVo
     */
//    public void handleUpdateFirmware(UpdateFirmwareVo updateFirmwareVo) throws InterruptedException {
//        //先查询出信息发布的数据
//        String versionData = redisService.getCacheObject(Constant.IDENT + updateFirmwareVo.getIdent());
//        UpdateFirmwareDto updateFirmwareDto = JSON.parseObject(versionData, UpdateFirmwareDto.class);
//        if (updateFirmwareVo.getCode() == Constant.SUCCESS) {
//            //固件升级后后修改发布记录信息的状态
//            List<DownloadFileInfoDto> downloadFileInfoDtos = new ArrayList<>();
//            //通过下发的文件id查询文件大小信息,进行分包发送
//            FileMgr fileMgrInfo = JSON.parseObject(versionManageRemoteService.getFileInfo(updateFirmwareDto.getFileId())
//                    .getData().toString(), FileMgr.class);
//            int fileSize = (int) fileMgrInfo.getSize();
//            int totalAmount = (int) ((double) fileSize / Constant.MAX_PAG_LEN <= 1 ? 1 : (double) fileSize / Constant.MAX_PAG_LEN + 1);
//            byte[] faceBytes = FileUtils.getBytesByFile(fileMgrInfo.getFileUrl());
//            for (int i = 0; i < totalAmount; i++) {
//                logger.info("totalAmount====" + totalAmount);
//                byte[] bytes;
//                if (i == totalAmount - 1) {
//                    logger.info("fileSize--->" + fileSize);
//                    bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, fileSize - (i * Constant.MAX_PAG_LEN));
//                } else {
//                    bytes = StringUtils.subBytes(faceBytes, i * Constant.MAX_PAG_LEN, Constant.MAX_PAG_LEN);
//                }
//                //封装下发文件的参数
//                DownloadFileInfoDto downloadFileInfoDto = new DownloadFileInfoDto();
//                downloadFileInfoDto.setIdent((Integer) systemManageRemoteService.getIdent().getData());
//                downloadFileInfoDto.setDeviceId(updateFirmwareVo.getDeviceId());
//                downloadFileInfoDto.setId(fileMgrInfo.getFileName());
//                downloadFileInfoDto.setTotalAmount(totalAmount);
//                downloadFileInfoDto.setWhichPackage(i + 1);
//                downloadFileInfoDto.setByteStream(bytes);
//                //将分成的每一包数据保存到redis当中,使用指令标识作为key,等下设备收到每包给出响应后,再继续进行下发,
//                // 存一个Copy key方便当前IDENT过期超时处理
//                redisService.setCacheObject(Constant.FILE_IDENT + downloadFileInfoDto.getIdent(),
//                        downloadFileInfoDto, Constant.FILE_IDENT_TIME, TimeUnit.SECONDS);
//                redisService.setCacheObject(Constant.COPY_FILE_IDENT + downloadFileInfoDto.getIdent(), downloadFileInfoDto);
//                if (i == 0) {
//                    //将第一包数据保存到redis当中,使用指令标识作为key,等下设备收到每包给出响应后,再继续进行下发,
//                    // 存一个Copy key方便当前IDENT过期超时处理
//                    redisService.setCacheObject(Constant.FILE_IDENT + downloadFileInfoDto.getIdent(),
//                            downloadFileInfoDto, Constant.FILE_IDENT_TIME, TimeUnit.SECONDS);
//                    redisService.setCacheObject(Constant.COPY_FILE_IDENT + downloadFileInfoDto.getIdent(), downloadFileInfoDto);
//                    //调用下发文件接口,将第一包文件进行下发
//                    Integer resultFile = platformCallHardwareService.downloadFileInfo(downloadFileInfoDto);
//                    //如果下发指令到硬件失败,给出提示信息
//                    if (resultFile.equals(Constant.ERROR)) {
//                        logger.error("调用下发文件接口失败");
//                    } else {
//                        logger.info("调用下发文件接口成功");
//                    }
//                }
//                //将每包文件封装到List集合存入redis中
//                downloadFileInfoDtos.add(downloadFileInfoDto);
//            }
//            //将封装了每包人脸文件的数据包List进行存储
//            redisService.setCacheObject(Constant.FILE + fileMgrInfo.getFileName() + downloadFileInfoDtos.get(0).getIdent(), downloadFileInfoDtos);
//            //授权处理完成后将redis中存储的授权信息进行删除
//            redisService.deleteObject(Constant.IDENT + updateFirmwareVo.getIdent());
//        }
//    }


}

