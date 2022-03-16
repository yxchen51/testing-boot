package com.open.qa.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 * Created by liang.chen on 2017/3/22.
 */
public class FileUtil {
    public static Map<String, String> getMapFromFile(File f) {
        Map<String, String> maps = null;
        try {
            maps = getMapFromStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return maps;
    }

    public static Map<String, String> getMapFromStream(InputStream in) {
        BufferedReader br = null;
        Map<String, String> maps = null;
        try {
            maps = new HashMap<String, String>();
            br = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                if (line.contains("=")) {
                    String[] ss = line.split("=");
                    String key = ss[0] == null ? "" : ss[0].trim();
                    String value = ss[1] == null ? "" : ss[1].trim();
                    maps.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return maps;
    }

    /**
     * 用于获取打包中的resources文件
     * @param filePath
     * @return
     */
    public static InputStream getFileInputStream(String filePath) {
        try {
            ClassLoader loader = FileUtil.class.getClassLoader();
            InputStream is = loader.getResourceAsStream(filePath);
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于获取执行时的resources文件(可读取test/resources/下的文件,相对路径)
     * @param filePath
     * @return
     */
    public static InputStream getTestFileInputStream(String filePath) {
        try {
            InputStream is = FileUtil.class.getResourceAsStream (filePath);
            return is;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从文件中读取字符串
     * @param filePath
     * @return
     */
    public static String readFileString(String filePath,String encode) {
        String str="";
        File file=new File(filePath);
        try {
            FileInputStream in = new FileInputStream(file.getAbsoluteFile());
            // size  为字串的长度 ，这里一次性读完
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            str=new String(buffer,encode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        System.out.println(str);
        return str;
    }


    public static void main(String args[]){
        String ster = readFileString("src\\main\\java\\com\\msxf\\qa\\factory\\FileTemplets.java","UTF-8");
    }
}
