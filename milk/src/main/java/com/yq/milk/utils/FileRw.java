package com.yq.milk.utils;

import android.util.Log;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.trubuzz.trubuzz.constant.Env;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.trubuzz.trubuzz.constant.Config.ad_log_cookie_key;
import static com.trubuzz.trubuzz.constant.Config.ad_log_default_cookie;
import static com.trubuzz.trubuzz.constant.Env.TAG;
import static com.trubuzz.trubuzz.utils.God.getResources;

/**
 * Created by king on 2017/6/26.
 */

public class FileRw {

    /**
     * 判断文件是否存在或是否创建成功
     * @param file
     * @return not null : 存在 或 创建成功
     * @throws IOException
     */
    public static File getFile(File file) throws IOException {
        if (!file.exists()){
            if(!file.createNewFile()){
                Log.e(TAG, String.format("writeFileData: 创建文件 %s 失败",file.getAbsolutePath() ));
                return null;
            }
        }
        return file;
    }
    public static File getFile(String fileDir) throws IOException {
        File file = new File(fileDir);
        return getFile(file);
    }

    /**
     * 将字符串写入文件 , 文件不存在则自动创建
     * @param data
     * @param fileDir
     * @return
     */
    public static String writeFileData(String data , String fileDir){
        BufferedWriter writer = null;
        File file = null;
        try {
            file = getFile(fileDir);
            // 若文件不存在或创建不成功
            if (file == null)  return "";

            writer = new BufferedWriter(new FileWriter(file ,true));
            writer.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeFileDataError: ",e );
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }
    /**
     * 将内容写入指定的属性文件 ,
     * 此方法为覆盖性写入 , 不支持更改或更新值
     *
     * @param fileName
     * @param property
     */
    public static void saveProperties(String fileName, List<Kvp<String, String>> property) {
        String propertiesPath = Env.filesDir + fileName;
        Properties prop = new Properties();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(propertiesPath);
            for (Kvp<String, String> p : property) {
                prop.setProperty(p.getKey(), p.getValue());
            }
            prop.store(out, null);
        } catch (IOException e) {
            System.err.println("Failed to open app.properties file");
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从属性文件中读取指定的key 的内容
     *
     * @param fileName
     * @param key
     * @return
     */
    public static String readProperty(String fileName, String key) {
        String propertiesPath = Env.filesDir + fileName;
        Properties prop = new Properties();
        FileInputStream in = null;
        String value = "";
        try {
            in = new FileInputStream(propertiesPath);
            prop.load(in);
            value = prop.getProperty(key);
            Log.i(TAG, String.format("readProperty: %s = %s", key, value));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 获取预置的cookie , 先从设备上的配置文件上取 ,
     *      若没取到则从预置类中取
     * @param fileName
     * @return
     */
    public static Kvp<String, String> getLogCookie(String fileName){
        String cvalue = readProperty(fileName, ad_log_cookie_key);
        if (cvalue.isEmpty()) {
            return ad_log_default_cookie;
        }
        return new Kvp<>(ad_log_cookie_key, cvalue);
    }

    /**
     * 读取 Assets 资源中的 yml 文件
     * @param in
     * @return Object 对象
     */
    public static List readYamlFile(InputStream in) {
        List list = new ArrayList();
        try {
            YamlReader reader = new YamlReader(new InputStreamReader(in));
            while (true) {
                Object contact = reader.read();
                if (contact == null) break;
                list.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readYamlFile: read error .", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static List readYamlFile(String relativePath){
        InputStream in = null;
        try {
            in = getResources().getAssets().open(relativePath);
            return readYamlFile(in);
        } catch (IOException e) {
            e.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void changeYamlValue(InputStream in , String path , Object value){
        try {
            YamlReader reader = new YamlReader(new InputStreamReader(in));
            while (true) {
                Object contact = reader.read();
                if (contact == null) break;

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readYamlFile: read error .", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean copyFile(InputStream in , String outPath){
        OutputStream output = null;
        try {
            File outFile = getFile(outPath);
            if (outFile == null)    return false;

            output = new FileOutputStream(outFile);

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null)
                    in.close();
                if(output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
