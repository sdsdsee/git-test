package com.eluolang.platform.util;

import act.Method;
import com.eluolang.platform.vo.DisplayParamVo;
import com.eluolang.platform.vo.TextDisplayParamVo;
import po.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author dengrunsen
 * @date 2022年06月06日 17:25
 */
public class LEDLargeScreenUtil {
    /**
     * led发送图片
     */
    public static Map<String, String> sendImage(Method m, DisplayParamVo displayParamVo, String imeis[], int order) throws Exception {
        // 发送图片
        DisplayParam dp = new DisplayParam();
        dp.setStay_time(displayParamVo.getStayTime());
        dp.setScreen_width(displayParamVo.getScreenWidth());//屏宽
        dp.setScreen_height(displayParamVo.getScreenHeight());//屏高
        dp.setDisplay_type(displayParamVo.getDisplayType());
        dp.setDisplay_speed(displayParamVo.getDisplaySpeed());
        dp.setColor(displayParamVo.getColor());//双色
        //idx
        Map<String, String> mp1 = m.led3_dcc_send_bitmap(imeis,
                displayParamVo.getFileUrl(), dp, order);
        Iterator<String> it10 = mp1.keySet().iterator();
        while (it10.hasNext()) {
            String imei = it10.next();
            System.out.println(imei + mp1.get(imei));
        }
        return mp1;
    }

    public static Map<String, String> sendText(Method m, TextDisplayParamVo textDisplayParamVo, String imeis[], int order) throws UnsupportedEncodingException {
        // 发送内码文字
        TextDisplayParam tdp = new TextDisplayParam();
        tdp.setStay_time(textDisplayParamVo.getStayTime());
        tdp.setScreen_width(textDisplayParamVo.getScreenWidth());
        tdp.setScreen_height(textDisplayParamVo.getScreenHeight());
        tdp.setFont_size(textDisplayParamVo.getFontSize());
        tdp.setFont_color(textDisplayParamVo.getFontColor());
        tdp.setDisplay_type(textDisplayParamVo.getDisplayType());
        tdp.setDisplay_speed(textDisplayParamVo.getDisplaySpeed());
        Map<String, String> mp = m.led3_dcc_send_text(imeis, textDisplayParamVo.getContent(),
                tdp, order);
        Iterator<String> it9 = mp.keySet().iterator();
        while (it9.hasNext()) {
            String imei = it9.next();
            System.out.println(imei + mp.get(imei));
        }
        return mp;
    }

    public static Map<String, String> clearScreen(String imeis[], Method m) {
        Map<String, String> m3 = m.led3_dcc_clear_led(imeis);
        Iterator<String> it3 = m3.keySet().iterator();
        while (it3.hasNext()) {
            String imei = it3.next();
            System.out.println(imei + m3.get(imei));
        }
        return m3;
    }

    /**
     * 调用led大屏api接口
     *
     * @param m           mserver连接方法类
     * @param commandType 调用类型
     * @param content     下发内容
     * @param imeis       设备IMEM号码数组
     * @throws Exception
     */
    private void entCommand(Method m, int commandType, String content,
                            String imeis[]) throws Exception {

        switch (commandType) {
            case 1:
                // 配置屏参
                Map<String, int[]> map1 = new HashMap<String, int[]>();
                map1.put(imeis[0], new int[]{128, 64, 2});
                // map1.put("240305001020002", new int[] { 128, 32, 1 });
                Map<String, String> m4 = m.led3_dcc_config_led(map1);
                Iterator<String> it4 = m4.keySet().iterator();
                while (it4.hasNext()) {
                    String imei = it4.next();
                    System.out.println(imei + m4.get(imei));
                }
                break;
            case 2:
                // 发送内码文字
                TextDisplayParam tdp = new TextDisplayParam();
                tdp.setStay_time(1);
                tdp.setScreen_width(128);
                tdp.setScreen_height(64);
                tdp.setFont_size(16);
                tdp.setFont_color(2);
                tdp.setDisplay_type(1);
                tdp.setDisplay_speed(10);
                Map<String, String> mp = m.led3_dcc_send_text(imeis, content,
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
                dp.setStay_time(100);
                dp.setScreen_width(32);//屏宽
                dp.setScreen_height(48);//屏高
                dp.setDisplay_type(0);
                dp.setDisplay_speed(10);
                dp.setColor(2);//双色
                Map<String, String> mp1 = m.led3_dcc_send_bitmap(imeis,
                        content, dp, 2);
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
                        imeis, content, tip, 8, 8, 88, 24);
                Iterator<String> it11 = mp2.keySet().iterator();
                while (it11.hasNext()) {
                    String imei = it11.next();
                    System.out.println(imei + mp2.get(imei));
                }
                break;
            case 5:
                // 发送节目
                Map<String, String> mp3 = m.led3_dcc_send_program(
                        imeis, content, 9);
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
                Map<String, String> m8 = m.led3_dcc_withdraw_program(map8);
                Iterator<String> it8 = m8.keySet().iterator();
                while (it8.hasNext()) {
                    String imei = it8.next();
                    System.out.println(imei + m8.get(imei));
                }
                break;
            case 7:
                // 清屏
                Map<String, String> m3 = m.led3_dcc_clear_led(imeis);
                Iterator<String> it3 = m3.keySet().iterator();
                while (it3.hasNext()) {
                    String imei = it3.next();
                    System.out.println(imei + m3.get(imei));
                }
                break;
            case 8:
                // 关屏
                Map<String, String> m1 = m.led3_dcc_close_led(imeis);
                Iterator<String> it1 = m1.keySet().iterator();
                while (it1.hasNext()) {
                    String imei = it1.next();
                    System.out.println(imei + m1.get(imei));
                }
                break;
            case 9:
                // 开屏
                Map<String, String> m2 = m.led3_dcc_open_led(imeis);
                Iterator<String> it2 = m2.keySet().iterator();
                while (it2.hasNext()) {
                    String imei = it2.next();
                    System.out.println(imei + m2.get(imei));
                }
                break;

            case 10:
                // 校准时钟
                Map<String, String> mm = m.led3_dcc_adjust_time(
                        new String[]{"240305001020001", "240305001023301"});
                Iterator<String> it = mm.keySet().iterator();
                while (it.hasNext()) {
                    String imei = it.next();
                    System.out.println(imei + mm.get(imei));
                }
                break;

            case 11:
                // 定时开关屏
                Map<String, String[]> map2 = new HashMap<String, String[]>();
                map2.put("240305001020001", new String[]{"15:18-15:21"});
                map2.put("240305001020002", new String[]{"15:18-15:21"});
                Map<String, String> m5 = m.led3_dcc_auto_opcl(map2);
                Iterator<String> it5 = m5.keySet().iterator();
                while (it5.hasNext()) {
                    String imei = it5.next();
                    System.out.println(imei + m5.get(imei));
                }
                break;
            case 12:
                // 定时亮度调节
                Map<String, String[]> map3 = new HashMap<String, String[]>();
                map3.put("240305001020001", new String[]{"12:00-13:00,1"});
                map3.put("240305001020002", new String[]{"12:00-13:00,1"});
                Map<String, String> m6 = m.led3_dcc_auto_bright(map3);
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
                Map<String, String> m7 = m.led3_dcc_set_bright(map);
                Iterator<String> it7 = m7.keySet().iterator();
                while (it7.hasNext()) {
                    String imei = it7.next();
                    System.out.println(imei + m7.get(imei));
                }
                break;
            case 14:
                String at_cmd = Constant.AT_QUERY_SIGNAL;
                Map<String, String> m9 = m.led3_dcc_at_cmd(imeis, at_cmd);
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
                Map<String, String> m10 = m.led3_dcc_driver_param(imeis, ddp);
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
                String urls[] = {content};
                Map<String, String> mp16 = m.led3_dcc_insert_bitmap_area(
                        imeis, urls, tip16, 0, 0, 31, 47, 32, 48);
                Iterator<String> it16 = mp16.keySet().iterator();
                while (it16.hasNext()) {
                    String imei = it16.next();
                    System.out.println(imei + mp16.get(imei));
                }
                break;
            default:
                m.led3_dcc_clear_led(new String[]{"240305001020001"});
                break;
        }
    }
}
