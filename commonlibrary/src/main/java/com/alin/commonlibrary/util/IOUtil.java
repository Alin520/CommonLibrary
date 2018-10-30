package com.alin.commonlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.alin.commonlibrary.util.collection.Preconditions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IOUtil
{
    private static final int BUFFER_SIZE = 1024 * 1024;

    public static String readFileToString(InputStream inputStream) throws IOException
    {
        return readFileToString(inputStream, null);
    }

    public static String readFileToString(InputStream inputStream, String charsetName) throws IOException
    {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();

        try
        {
            if(charsetName == null)
            {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            }
            else
            {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            }

            char[] buffer = new char[BUFFER_SIZE];

            for(int bytesRead; (bytesRead = bufferedReader.read(buffer)) != -1; )
            {
                sb.append(Arrays.copyOf(buffer, bytesRead));
            }
        }
        finally
        {
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
        }

        return sb.toString();
    }

    public static String readFileToString(File file) throws IOException
    {
        return readFileToString(file, null);
    }

    public static String readFileToString(File file, String charsetName) throws IOException
    {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();

        try
        {
            if(charsetName == null)
            {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }

            char[] buffer = new char[BUFFER_SIZE];

            for(int bytesRead; (bytesRead = in.read(buffer)) != -1; )
            {
                sb.append(Arrays.copyOf(buffer, bytesRead));
            }
        }
        finally
        {
            if(in != null)
            {
                in.close();
            }
        }

        return sb.toString();
    }

    public static void writeToFile(File file, String content) throws IOException
    {
        writeToFile(file, content, null, false);
    }

    public static void writeToFile(File file, String content, String charsetName, boolean append) throws IOException
    {
        PrintWriter out = null;
        BufferedReader in = null;

        try
        {
            if(file.exists() || file.createNewFile())
            {
                char[] buffer = new char[BUFFER_SIZE];
                in = new BufferedReader(new StringReader(content));

                if(charsetName == null)
                {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append))));
                }
                else
                {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charsetName)));
                }

                for(int bytesRead; (bytesRead = in.read(buffer)) != -1; )
                {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
        finally
        {
            try
            {
                if(in != null)
                {
                    in.close();
                }
            }
            finally
            {
                if(out != null)
                {
                    out.close();
                }
            }
        }
    }

    public static boolean writeBitmap(Bitmap bitmap, String path, int quality, CompressFormat format)
    {
        boolean success = false;
        File file = new File(path);
        FileOutputStream outStream = null;

        try
        {
            outStream = new FileOutputStream(file);
            bitmap.compress(format, quality, outStream);
            outStream.flush();
            success = true;
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            if(outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch(Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }

        return success;
    }

    public static void copyAssetsFile(Context context, String assetsFileName, String destPath) throws IOException
    {
        InputStream in = null;
        FileOutputStream out = null;

        try
        {
            in = context.getAssets().open(assetsFileName);
            File destFile = new File(destPath);

            if((!destFile.exists() || destFile.delete()) && destFile.createNewFile())
            {
                out = new FileOutputStream(destFile);
                in2OutStream(in, out, BUFFER_SIZE);
            }
        }
        finally
        {
            try
            {
                if(in != null)
                {
                    in.close();
                }
            }
            finally
            {
                if(out != null)
                {
                    out.close();
                }
            }
        }
    }

    /**
     * 文件拷贝
     * @param src source {@link File}
     * @param dest destination {@link File}
     * @throws IOException
     */
    public static boolean copyFile(String src, String dest)
    {
        return copyFile(new File(src), new File(dest));
    }

    public static boolean copyFile(File src, File dest)
    {
        boolean result = false;
        FileInputStream in = null;
        FileOutputStream out = null;

        try
        {
            if((!dest.exists() || dest.delete()) && dest.createNewFile())
            {
                in = new FileInputStream(src);
                out = new FileOutputStream(dest);
                in2OutStream(in, out, BUFFER_SIZE);
                result = true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(in != null)
                {
                    in.close();
                }

                if(out != null)
                {
                    out.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void in2OutStream(InputStream in, OutputStream out, int bufferSize) throws IOException
    {
        byte[] buffer = new byte[bufferSize];

        for(int bytesRead; (bytesRead = in.read(buffer)) != -1; )
        {
            out.write(buffer, 0, bytesRead);
        }
    }

    public static String getAssetsContent(Context context, String fileName)
    {
        String content = null;
        InputStream in = null;

        try
        {
            in = context.getAssets().open(fileName);
            content = readFileToString(in);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            if(in != null)
            {
                try
                {
                    in.close();
                }
                catch(Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }

        return content == null? "": content;
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException
    {
        unzip(new BufferedInputStream(new FileInputStream(zipFile)), targetDirectory);
    }

    public static void unzip(InputStream in, File targetDirectory) throws IOException
    {
        final byte[] buffer = new byte[5120];
        ZipInputStream inputStream = new ZipInputStream(in);
        FileOutputStream outputStream;
        ZipEntry entry;
        int count;
        File file, dir;

        try
        {
            while((entry = inputStream.getNextEntry()) != null)
            {
                file = new File(targetDirectory, entry.getName());
                dir = entry.isDirectory()? file: file.getParentFile();

                if(!dir.exists())
                {
                    StorageUtil.mkdirs(dir);
                }

                if(!entry.isDirectory())
                {
                    outputStream = new FileOutputStream(file);

                    try
                    {
                        while((count = inputStream.read(buffer)) != -1)
                        {
                            outputStream.write(buffer, 0, count);
                        }
                    }
                    finally
                    {
                        outputStream.close();
                    }
                }
            }
        }
        finally
        {
            inputStream.close();
        }
    }


    /**
     * 从输入流读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws IOException{
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            if (len!=0) {
                outSteam.write(buffer, 0, len);
            }
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 从输入流读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String inputStream2String(InputStream inStream) throws IOException{

        return new String(readInputStream(inStream), "UTF-8");
    }

    /**
     *
     * @param is
     * @param os
     * @throws IOException
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        for (;;) {
            int count = is.read(bytes, 0, BUFFER_SIZE);
            if (count == -1)
                break;
            os.write(bytes, 0, count);
        }
    }


    /**
     * 将流写入文件
     * @param in
     * @param target
     * @throws IOException
     */
    public static void writeToFile(InputStream in, File target) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    /**
     * 安全关闭io流
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安全关闭io流
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables) {

        if (Preconditions.isNotBlank(closeables)) {

            for(Closeable closeable:closeables) {
                closeQuietly(closeable); // 系统先匹配确定参数的方法，没有再去匹配调用不定项参数的方法
            }
        }
    }
}