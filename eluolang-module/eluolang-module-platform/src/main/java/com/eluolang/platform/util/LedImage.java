package com.eluolang.platform.util;

import com.alibaba.fastjson.JSONObject;
import com.eluolang.common.core.constant.LinuxSite;
import com.eluolang.common.core.constant.WindowsSite;
import com.eluolang.common.core.util.FileUploadUtil;
import com.eluolang.platform.vo.EllGarageVo;
import com.eluolang.platform.vo.ResultVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.formula.functions.T;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.List;

public class LedImage /*extends JFrame*/ {
    private static BufferedImage image = null;//接收图片的缓存对象
    //private static Graphics mainGraphics = null, graphics = null;//得到主面板的画笔和图片的画笔
    private static Graphics2D graphics2D = null;//得到主面板的画笔和图片的画笔
    //private static Container panel = null;//得到面板对象
    private static ByteArrayOutputStream outputStream = null;//字节缓冲流对象

    public static void main(String[] args) {
        /* LedImage window = new LedImage();*/
        //readImage(300, 700, "赵京龙");

        RemainSpaceNumRequest remainSpaceNumRequest = new RemainSpaceNumRequest();
        remainSpaceNumRequest.setParkSyscode("");
        ResultVo resultCar = JSONObject.parseObject(ArtemisPostTest.remainSpaceNum(
                remainSpaceNumRequest), ResultVo.class);
        if (resultCar.msg.equals("success!")) {
            Gson gson = new Gson();
            List<EllGarageVo> ellGarageVo = gson.fromJson(String.valueOf(resultCar.getData()), new TypeToken<List<EllGarageVo>>() {
            }.getType());
            //坐标图片
            int coord[][] = {{260, 690}, {1300, 750}};
            readImage(coord, ellGarageVo.get(0).getLeftPlace() + ",000");
        }
    }

    /**
     * 通过构造方法完成窗口的初始化
     */
 /*   public LedImage() {
        this.setSize(281, 277);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = this.getContentPane();
        this.setVisible(true);
        readImage();
        this.addMouseListener(new MouseListen());
        this.addWindowListener(new WindowListen());
        mainGraphics = panel.getGraphics();//得到面板的画笔
        print(mainGraphics);//绘图方法
    }*/

    //将图片从指定的路径中读入程序
    //如果成功就返回真，否则就报错并且结束程序
    public static boolean readImage(int coord[][], String text) {
        File file = null;
        if (FileUploadUtil.isLinux() == false) {
            file = new File(WindowsSite.FILE_PATH + "template.png");
        } else {
            file = new File(LinuxSite.FILE_PATH + "LED/template.png");
        }
        //路径生成
        PathGeneration.createPath(file);
        try {
            image = ImageIO.read(file);
            String[] TEXT;
            if (text.contains(",")) {
                TEXT = text.split(",");
            } else {
                TEXT = new String[1];
                TEXT[0] = text;
            }
            //更改图片
            for (int i = 0; i < coord.length; i++) {
                drawImage(coord[i][0], coord[i][1], TEXT[i]);
            }

            //输出图片
            writeImage();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //将图片写出到计算机指定的位置
    private static void writeImage() {
        outputStream = new ByteArrayOutputStream();//初始化缓冲字节流对象
        try {
            ImageIO.write(image, "png", outputStream);//将字节流进行后缀的修饰，并将图片放入字节流中
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        byte[] content = outputStream.toByteArray();//得到字节流中的所有字节
        String path = "";
        if (FileUploadUtil.isLinux() == false) {
            path = WindowsSite.FILE_PATH + "truckSpace.png";
        } else {
            path = LinuxSite.FILE_PATH + "LED/truckSpace.png";
        }
        File file = new File(path);
        //路径生成
        PathGeneration.createPath(file);
        try (BufferedOutputStream inputStream = new BufferedOutputStream(new FileOutputStream(path))) {
            //将所传过来的字节流中的字节设置为输出流
            inputStream.write(content);//将字节写出到指定位置
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //实现对图片的绘画
    private static void drawImage(int x, int y, String text) {
        graphics2D = image.createGraphics();//得到图片的画笔
        graphics2D.setFont(new Font("华文财运", 0, 150));//设置画笔的样式
        graphics2D.setColor(new Color(255, 192, 0));//设置画笔的颜色
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawString(text, x, y);//设置画笔在响应事件的时候画的是什么
        graphics2D.drawImage(image, 0, 0, null);//最后通过主面板的画笔将绘画后的图片显示在出窗口中
    }

     /*  @Override
       public void print(Graphics g) {
           //通过重写SWING中的绘画方法来调用自定义的图片绘画方法
           g.drawImage(image, 0, 0, null);
       }
    //通过定义一个私有的内部类完成鼠标的监听
    private class MouseListen extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            //print(mainGraphics);//每次绘画都重绘图片
            mainGraphics.drawImage(image, 0, 0, null);
            //drawImage(e.getX(), e.getY());//将鼠标的X、Y坐标作为参数传给绘画方法
        }
    }

    //通过定义一个私有的内部类完成窗口的监听（主要是对关闭时做操作）
    //作用是：在窗口关闭的时候将图片从程序中写出
    private static class WindowListen extends WindowAdapter {
        //实现窗口关闭时的事件
        @Override
        public void windowClosing(WindowEvent e) {
            outputStream = new ByteArrayOutputStream();//初始化缓冲字节流对象
            try {
                ImageIO.write(image, "png", outputStream);//将字节流进行后缀的修饰，并将图片放入字节流中
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            byte[] content = outputStream.toByteArray();//得到字节流中的所有字节
            writeImage(content);//写出的方法
        }


        //将图片写出到计算机指定的位置
        private void writeImage(byte[] content) {
            try (BufferedOutputStream inputStream = new BufferedOutputStream(new FileOutputStream("D:\\EllTestImg\\22.png"))) {
                //将所传过来的字节流中的字节设置为输出流
                inputStream.write(content);//将字节写出到指定位置
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}

