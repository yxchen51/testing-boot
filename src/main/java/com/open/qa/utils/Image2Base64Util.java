package com.open.qa.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 图片转Base64的工具类
 * Created by liang.chen on 2017/3/22.
 */
public class Image2Base64Util {

    /**
     * 将本地图片读取为Base64
     * @param imgPath
     * @return
     */
    public static String image2Base64(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 将Base64存为本地图片
     * @param imgStr
     * @param imgFilePath
     * @returnb
     */
    public static boolean base64ToImage(String imgStr, String imgFilePath) {
        if ("".equals(imgStr) || imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
