package com.eluolang.platform.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import po.Constant;
import po.DisplayParam;
import po.DriverParam;
import po.TextDisplayParam;
import po.TextInsertParam;
import act.Method;

/**
 * @author dengrunsen
 * @date 2022年06月06日 15:52
 */
public class Test {

    public static void main(String[] args) throws Exception {

        String imeis[] =new String[1];// {"240305004110343","240305001020001"};
        imeis[0]="240305004110343";
     //   imeis[1]="240305001020001";
        String name="Thread1";
        Thread thread1 = new Thread(new SendThread(imeis,name), name);
        thread1.start();
//		String name2="Thread2";
//		Thread thread2 = new Thread(new SendThread(imeis2,name2), name2);
//		thread2.start();

    }

}

class SendThread implements Runnable {
    // 需要控制的终端编号
    private String imeis[] = null;
    // new 一个方法对象
    private Method method = null;

    private String name=null;
    public SendThread(String imeis[],String name) {
        // TODO Auto-generated constructor stub
        this.imeis = imeis;
        this.name = name;
        this.method = new Method();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            //1、连接服务器
            // 进行与mServer的连接, 输入正确的IP地址，端口号，是否需要验证（1、需要验证
            // 0、不需要验证），需要验证的用户名和密码，不需要验证的用户名和密码为空串
            method.connect(
                    "www.listentech.com.cn", 9035,1, "admin", "admin");
            long beginTime = System.currentTimeMillis();
            while(true){
                while(method.isConnect()){
                    //自己的执行代码
                    sentCommand( method, 7, imeis);

                    Thread.sleep(10000);
                    long endTime = System.currentTimeMillis();
                    System.out.println(this.name+" send bitmap time: "+(endTime-beginTime)/1000+" s");
                    //实际使用时，不需要每次都连接断开。可保持常连接。
                    //如果返回值是RESULT_DISCONNECT，才需要断开重连

                }
                System.out.println("断开连接");
                method.disConnect();
                method.connect(
                        "www.listentech.com.cn", 9035,1, "admin", "admin");
            }
            //method.led3_dcc_listen_led(imeis);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sentCommand( Method m, int commandType,
                              String imeis[]) throws Exception {

        switch (commandType) {
            case 1:
                // 配置屏参
                Map<String, int[]> map1 = new HashMap<String, int[]>();
                map1.put(imeis[0], new int[] { 128, 64, 2 });
                // map1.put("240305001020002", new int[] { 128, 32, 1 });
                Map<String, String> m4 = m.led3_dcc_config_led( map1);
                Iterator<String> it4 = m4.keySet().iterator();
                while (it4.hasNext()) {
                    String imei = it4.next();
                    System.out.println(imei + m4.get(imei));
                }
                break;
            case 2:
                // 发送内码文字
                TextDisplayParam tdp = new TextDisplayParam();
                tdp.setStay_time(420);
                tdp.setScreen_width(128);
                tdp.setScreen_height(64);
                tdp.setFont_size(16);
                tdp.setFont_color(2);
                tdp.setDisplay_type(1);
                tdp.setDisplay_speed(10);
                Map<String, String> mp = m.led3_dcc_send_text( imeis, "5G科技园",
                        tdp, 1);
                Iterator<String> it9 = mp.keySet().iterator();
                while (it9.hasNext()) {
                    String imei = it9.next();
                    System.out.println(imei + mp.get(imei));
                }
                break;
            case 3:
                // 发送图片
                DisplayParam dp = new DisplayParam();
                dp.setStay_time(1200);
                dp.setScreen_width(320);//屏宽
                dp.setScreen_height(208);//屏高
                dp.setDisplay_type(0);
                dp.setDisplay_speed(10);
                dp.setColor(2);//双色
                Map<String, String> mp1 = m.led3_dcc_send_bitmap( imeis,
                        "e:\\zyp\\123456.png", dp, 1);
                Iterator<String> it10 = mp1.keySet().iterator();
                while (it10.hasNext()) {
                    String imei = it10.next();
                    System.out.println(imei + mp1.get(imei));
                }
                break;
            case 4:
                // 插播内码文字
                TextInsertParam tip = new TextInsertParam();
                tip.setTime_length(60);
                tip.setStay_time(10);
                tip.setFont_size(16);
                tip.setFont_color(2);
                tip.setDisplay_type(1);
                tip.setDisplay_speed(20);
                Map<String, String> mp2 = m.led3_dcc_insert_text_area(
                        imeis, "5G科技园", tip,8,8,64,16);
                Iterator<String> it11 = mp2.keySet().iterator();
                while (it11.hasNext()) {
                    String imei = it11.next();
                    System.out.println(imei + mp2.get(imei));
                }
                break;
            case 5:
                // 发送节目
                Map<String, String> mp3 = m.led3_dcc_send_program(
                        imeis, "e:/tempPro.dat", 9);
                Iterator<String> it12 = mp3.keySet().iterator();
                while (it12.hasNext()) {
                    String imei = it12.next();
                    System.out.println(imei + mp3.get(imei));
                }
                break;
            case 6:
                // 撤销节目
                Map<String, String> map8 = new HashMap<String, String>();
                map8.put("240305001020001", "1,2,9");
                map8.put("240305001020002", "1");
                Map<String, String> m8 = m.led3_dcc_withdraw_program( map8);
                Iterator<String> it8 = m8.keySet().iterator();
                while (it8.hasNext()) {
                    String imei = it8.next();
                    System.out.println(imei + m8.get(imei));
                }
                break;
            case 7:
                // 清屏
                Map<String, String> m3 = m.led3_dcc_clear_led( imeis);
                Iterator<String> it3 = m3.keySet().iterator();
                while (it3.hasNext()) {
                    String imei = it3.next();
                    System.out.println(imei + m3.get(imei));
                }
                break;
            case 8:
                // 关屏
                Map<String, String> m1 = m.led3_dcc_close_led( new String[] {
                        "240305001020001", "240305001023301" });
                Iterator<String> it1 = m1.keySet().iterator();
                while (it1.hasNext()) {
                    String imei = it1.next();
                    System.out.println(imei + m1.get(imei));
                }
                break;
            case 9:
                // 开屏
                Map<String, String> m2 = m.led3_dcc_open_led( new String[] {
                        "240305001020001", "240305001023301" });
                Iterator<String> it2 = m2.keySet().iterator();
                while (it2.hasNext()) {
                    String imei = it2.next();
                    System.out.println(imei + m2.get(imei));
                }
                break;

            case 10:
                // 校准时钟
                Map<String, String> mm = m.led3_dcc_adjust_time(
                        new String[] { "240305001020001", "240305001023301" });
                Iterator<String> it = mm.keySet().iterator();
                while (it.hasNext()) {
                    String imei = it.next();
                    System.out.println(imei + mm.get(imei));
                }
                break;

            case 11:
                // 定时开关屏
                Map<String, String[]> map2 = new HashMap<String, String[]>();
                map2.put("240305001020001", new String[] { "15:18-15:21" });
                map2.put("240305001020002", new String[] { "15:18-15:21" });
                Map<String, String> m5 = m.led3_dcc_auto_opcl( map2);
                Iterator<String> it5 = m5.keySet().iterator();
                while (it5.hasNext()) {
                    String imei = it5.next();
                    System.out.println(imei + m5.get(imei));
                }
                break;
            case 12:
                // 定时亮度调节
                Map<String, String[]> map3 = new HashMap<String, String[]>();
                map3.put("240305001020001", new String[] { "12:00-13:00,1" });
                map3.put("240305001020002", new String[] { "12:00-13:00,1" });
                Map<String, String> m6 = m.led3_dcc_auto_bright( map3);
                Iterator<String> it6 = m6.keySet().iterator();
                while (it6.hasNext()) {
                    String imei = it6.next();
                    System.out.println(imei + m6.get(imei));
                }
                break;
            case 13:
                // 亮度调节
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put(imeis[0], 2);
//			map.put("240305001020002", 15);
                Map<String, String> m7 = m.led3_dcc_set_bright( map);
                Iterator<String> it7 = m7.keySet().iterator();
                while (it7.hasNext()) {
                    String imei = it7.next();
                    System.out.println(imei + m7.get(imei));
                }
                break;
            case 14:
                String at_cmd = Constant.AT_QUERY_SIGNAL;
                Map<String, String> m9 = m.led3_dcc_at_cmd(
                        new String[] { "240305001020001" }, at_cmd);
                Iterator<String> it77 = m9.keySet().iterator();
                while (it77.hasNext()) {
                    String imei = it77.next();
                    System.out.println(imei + m9.get(imei));
                }
                break;
            case 15:
                DriverParam ddp = new DriverParam();
                ddp.setJing_xiang(1);
                ddp.setSao_miao(0);
                Map<String, String> m10 = m.led3_dcc_driver_param(
                        new String[] { "240305001020001" }, ddp);
                Iterator<String> it88 = m10.keySet().iterator();
                while (it88.hasNext()) {
                    String imei = it88.next();
                    System.out.println(imei + m10.get(imei));
                }
                break;
            case 16:
                // 插播内码图片
                TextInsertParam tip16 = new TextInsertParam();
                tip16.setTime_length(600);
                tip16.setStay_time(60);
                //tip16.setFont_size(16);
                tip16.setFont_color(2);//1单色 2双色
                tip16.setDisplay_type(1);
                tip16.setDisplay_speed(20);
                String urls[]={"D:\\EllTestImg\\1653113283894c594921f2691435b80cc3f6e986fbc0b\\8edf3fa6-ce63-4880-93e7-39d76fe93efc.png"};
                Map<String, String> mp16 = m.led3_dcc_insert_bitmap_area(
                        imeis, urls, tip16,0,0,31,47,32,48);
                Iterator<String> it16 = mp16.keySet().iterator();
                while (it16.hasNext()) {
                    String imei = it16.next();
                    System.out.println(imei + mp16.get(imei));
                }
                break;
            default:
                m.led3_dcc_clear_led( new String[] { "240305001020001" });
                break;
        }
    }
}
