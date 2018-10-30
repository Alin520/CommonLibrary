/**
 * 
 */
package com.alin.commonlibrary.util.reflect;

/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2018/7/30 17:40
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class ReflectException extends RuntimeException {

	private static final long serialVersionUID = -6654702552823551870L;

	public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
