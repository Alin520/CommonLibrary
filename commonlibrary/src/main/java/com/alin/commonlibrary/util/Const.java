package com.alin.commonlibrary.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @创建者 hailin
 * @创建时间 2017/8/21 10:36
 * @描述 ${}.
 */

public class Const {
    public static DateFormat DATE_FORMAT_SIMPLE = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);

    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm:ss");

    public final static long  CurrentTime = System.currentTimeMillis();
}
