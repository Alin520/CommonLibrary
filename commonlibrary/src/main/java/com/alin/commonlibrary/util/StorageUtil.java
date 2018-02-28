package com.alin.commonlibrary.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 hailin
 * @创建时间 2017/8/29 11:18
 * @描述 ${}.
 */

public class StorageUtil {
    private static Boolean hasSDCard;

    public static boolean hasSDCard()
    {
        if(hasSDCard == null)
        {
            hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.isExternalStorageRemovable();
        }

        return hasSDCard;
    }

    public static File getCacheDir(Context context)
    {
        File dir;

        if(hasSDCard())
        {
            dir = context.getExternalCacheDir();
        }
        else
        {
            dir = context.getCacheDir();
        }

        return dir;
    }

    /**
     * 创建文件
     * java.io.File中的mkdirs方法在Android中是失灵的
     */
    public static void mkdirs(File file)
    {
        List<File> files = new ArrayList<>();
        File parent;

        if(!file.exists())
        {
            files.add(file);
        }

        while((parent = file.getParentFile()) != null && !parent.exists())
        {
            files.add(0, parent);
            file = parent;
        }

        for(File dir: files)
        {
            dir.mkdir();
        }
    }

    /**
    *
    * 删除文件
    */
    public static void deleteFile(File file)
    {
        if(file.isDirectory())
        {
            List<File> files = new ArrayList<>();
            collectFiles(file, files);

            for(File childFile: files)
            {
                childFile.delete();
            }
        }
        else
        {
            file.delete();
        }
    }

    /**
     * 获取内部缓存使用量
     */
    public static long getInnerCacheSize(Context context)
    {
        long size = 0;
        File cacheDir = context.getCacheDir();

        if(cacheDir != null && cacheDir.exists() && cacheDir.isDirectory())
        {
            List<File> files = new ArrayList<>();
            collectFiles(cacheDir, files);

            for(File file: files)
            {
                if(file.isFile())
                {
                    size += file.length();
                }
            }
        }

        return size;
    }

    /**
     * 清除内部缓存
     */
    public static void clearInnerCaches(Context context)
    {
        File cacheDir = context.getCacheDir();

        if(cacheDir != null && cacheDir.exists() && cacheDir.isDirectory())
        {
            List<File> files = new ArrayList<>();
            collectFiles(cacheDir, files);

            for(File file: files)
            {
                file.delete();
            }
        }
    }

    private static void collectFiles(File dir, List<File> fileList)
    {
        fileList.add(0, dir);

        for(File file: dir.listFiles())
        {
            if(file.isDirectory())
            {
                collectFiles(file, fileList);
            }
            else
            {
                fileList.add(0, file);
            }
        }
    }

    @NonNull
    public static Uri getPictureRootPath(String defaultPath)
    {
        Uri pictureRootPath = null;
        File sdRoot = Environment.getExternalStorageDirectory();
        File cameraRoot = new File(sdRoot, "DCIM/Camera");

        if(cameraRoot.exists() && cameraRoot.isDirectory())
        {
            pictureRootPath = Uri.fromFile(cameraRoot);
        }
        else
        {
            File picturesRoot = new File(sdRoot, "Pictures");

            if(picturesRoot.exists() && picturesRoot.isDirectory())
            {
                pictureRootPath = Uri.fromFile(picturesRoot);
            }
        }

        if(pictureRootPath == null)
        {
            File defaultRoot = new File(sdRoot, defaultPath);

            if(!defaultRoot.exists())
            {
                mkdirs(defaultRoot);
            }

            pictureRootPath = Uri.fromFile(defaultRoot);
        }

        return pictureRootPath;
    }


}
