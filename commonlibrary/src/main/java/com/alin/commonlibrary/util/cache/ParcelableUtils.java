package com.alin.commonlibrary.util.cache;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2018/6/30 17:40
 * 版    本   ： ${TODO}
 * 描    述   ：  parcelable 序列化、反序列化
 * ================================================
 */
public class ParcelableUtils {

    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
