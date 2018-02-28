package com.alin.commonlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
}