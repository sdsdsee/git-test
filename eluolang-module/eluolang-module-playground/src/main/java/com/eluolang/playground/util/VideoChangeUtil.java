package com.eluolang.playground.util;

import java.io.File;

public class VideoChangeUtil implements Runnable {
    private static String aviPath;
    private static String mp4Path;

    public VideoChangeUtil(String aviPath, String mp4Path) {
        this.aviPath = aviPath;
        this.mp4Path = mp4Path;
    }

    @Override
    public void run() {
        AviTransitionMp4Util.aviToMp4(aviPath, mp4Path);
        File file = new File(aviPath);
        //删除avi文件
        if (file.exists()) {
            file.delete();
        }
    }
}
