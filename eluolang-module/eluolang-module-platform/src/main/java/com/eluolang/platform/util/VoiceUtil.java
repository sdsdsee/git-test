package com.eluolang.platform.util;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class VoiceUtil {
    private static DatagramSocket ds;

    //要发平台ip
    public static String address = "127.0.0.1";
    //设备IP
    public static String deviceAddress = "127.0.0.1";

    public void setAddress(String address, String deviceAddress) {
        this.address = address;
        this.deviceAddress = deviceAddress;
    }


    static {
        try {
            ds = new DatagramSocket(555);
            System.out.println("等待连接");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void sss() throws IOException, LineUnavailableException, InterruptedException {
        byte[] bys = new byte[20000];
        DatagramPacket dp = new DatagramPacket(bys, 0, bys.length);//建立信息包
        while (true) {
            ds.receive(dp);//将socket的信息接收到dp里
   /*         System.out.println(dp.getAddress());
            System.out.println(deviceAddress);
            System.out.println(address);
            System.out.println(("/"+address).equals(dp.getAddress().toString()));*/
            dp.setPort(987);
            //判断是否是前端设备ip
            if (("/"+address).equals(dp.getAddress().toString())) {
                dp.setAddress(InetAddress.getByName(deviceAddress));
            }else {
                dp.setAddress(InetAddress.getByName(address));
            }

            ds.send(dp);
        }
    }
}
