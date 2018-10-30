package com.alin.commonlibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建者 hailin
 * @创建时间 2017/8/18 14:02
 * @描述 ${文件工具}.
 */

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    private static HashMap<String,String> map;

    public static boolean exists(File file) {
        return file!=null && file.exists();
    }

    /**
     * 判断是否文件
     * @param file
     * @return
     */
    public static boolean isFile(File file) {
        return exists(file) && file.isFile();
    }

    /**
     * 判断是否目录
     * @param file
     * @return
     */
    public static boolean isDirectory(File file) {
        return exists(file) && file.isDirectory();
    }


    /**
     *  收集手机设备信息
     * @param context
     * @return
     */
    public static HashMap<String,String> collectDeviceInfo(Context context){
        map = new HashMap<>();
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = packageInfo.versionName;
            String versionCode = packageInfo.versionCode + "";
            map.put("versionName",versionName);
            map.put("versionCode",versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "an error occured when collect package info", e);
        }

        Field[] fields = Build.class.getFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(),field.get(null).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "an error params when collect package info", e);
                }

            }
        }
        return map;
    }


    public static String saveCrashInfo2File(Throwable throwable,Map<String, String> infos) throws IOException {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            buffer.append(key + "=" +value + "\n");
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        Throwable cause = throwable.getCause();
//        通过while循环，将所有的异常信息都用PrintWriter存储
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = throwable.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        buffer.append(result);

//        保存错误日志
        String time = Const.DATE_FORMAT.format(new Date());
        String fileName = "crash-" + time + "-" + Const.CurrentTime + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(buffer.toString().getBytes());
            fos.close();
        }

        return fileName;
    }

//    保存log文件到sd卡
    public static void saveFileToSdCrad(String filePath,String tag, String msg, Throwable t){
        if(!TextUtils.isEmpty(filePath))
        {
            Date date = new Date();
            String logFileName = Const.DATE_FORMAT_SIMPLE.format(date) + ".log";
            File dirFile = new File(filePath);
            File logFile = new File(filePath, logFileName);

            if(!dirFile.exists() || !dirFile.isDirectory())
            {
                StorageUtil.deleteFile(dirFile);
                StorageUtil.mkdirs(dirFile);
            }

            if(!logFile.exists() || !logFile.isFile())
            {
                StorageUtil.deleteFile(logFile);
            }

            StringBuilder log = new StringBuilder();
            log.append(Const.DATE_FORMAT.format(date));
            log.append(' ');
            log.append(tag);
            log.append(": ");
            log.append(msg);
            log.append('\n');

            if(t != null)
            {
                log.append(t.getMessage());
                log.append('\n');
            }

            try
            {
                IOUtil.writeToFile(logFile, log.toString(), null, true);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
