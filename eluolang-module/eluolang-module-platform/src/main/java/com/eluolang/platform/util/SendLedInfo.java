package com.eluolang.platform.util;

import act.Method;
import com.eluolang.platform.vo.DisplayParamVo;
import com.eluolang.platform.vo.TextDisplayParamVo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class SendLedInfo implements Runnable {
    // 需要控制的终端编号
    private String imeis[] = null;
    // new 一个方法对象
    private Method method = null;

    private String name = null;
    //顺序
    private int order = 0;
    //图片
    private DisplayParamVo displayParamVo = null;
    //文字
    private TextDisplayParamVo textDisplayParamVo = null;
    //类型
    private int type;

    public SendLedInfo(String imeis[], String name, DisplayParamVo displayParamVo, TextDisplayParamVo textDisplayParamVo, int type) {
        // TODO Auto-generated constructor stub
        this.imeis = imeis;
        this.name = name;
        this.method = new Method();
        this.displayParamVo = displayParamVo;
        this.textDisplayParamVo = textDisplayParamVo;
        this.type = type;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            //1、连接服务器
            // 进行与mServer的连接, 输入正确的IP地址，端口号，是否需要验证（1、需要验证
            // 0、不需要验证），需要验证的用户名和密码，不需要验证的用户名和密码为空串
            method.connect(
                    "www.listentech.com.cn", 9035, 1, "admin", "admin");
            long beginTime = System.currentTimeMillis();
            while (true) {
                while (method.isConnect()) {
                    //自己的执行代码
                    Thread.sleep(10000);
                    long endTime = System.currentTimeMillis();
                    System.out.println(this.name + " send bitmap time: " + (endTime - beginTime) / 1000 + " s");
                    //if (this.)
                    //实际使用时，不需要每次都连接断开。可保持常连接。
                    //如果返回值是RESULT_DISCONNECT，才需要断开重连
                    switch (type) {
                        case 1:
                            LEDLargeScreenUtil.sendImage(method, displayParamVo, imeis, order);
                            break;
                    }
                }
                System.out.println("断开连接");
                method.disConnect();
                method.connect(
                        "www.listentech.com.cn", 9035, 1, "admin", "admin");
            }
            //method.led3_dcc_listen_led(imeis);

        } /*catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */ catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}