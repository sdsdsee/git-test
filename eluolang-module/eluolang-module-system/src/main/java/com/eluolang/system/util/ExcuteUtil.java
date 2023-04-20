package com.eluolang.system.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ExcuteUtil {
    public static String exeCmd() {
        String result = null;
        try {
            String[] cmd = new String[]{"/bin/sh", "-c", " ls -lt /home/nginx-download/html/file/sql | grep sql | head -n 1 |awk '{print $9}' "};
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static boolean deleteFile(String path){
        File file = new File(path);
        boolean isOK = file.exists();
        if (isOK){
            file.delete();
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
        return isOK;
    }
/*    public static String exeCommand(String host, int port, String user, String password, String command) throws  JSchException, IOException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        //    java.util.Properties config = new java.util.Properties();
        //   config.put("StrictHostKeyChecking", "no");

        session.setPassword(password);
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");

        channelExec.disconnect();
        session.disconnect();

        return out;
    }*/
    public static void main(String[] args){
//        String host = "192.168.110.235";
//        int port = 22;
//        String user = "root";
//        String password = "root";
//        String command = "ls -lt /home/nginx-download/html/file/sql | grep sql | head -n 1 |awk '{print $9}'";
//        String res = exeCommand(host,port,user,password,command);
//
//        System.out.println(res.trim());

        String result = exeCmd();
        System.out.println("获取的结果是"+"\n"+result);
    }
}
