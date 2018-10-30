package com.alin.commonlibrary.permission.annotation;


import com.alin.commonlibrary.permission.util.PermissionUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/10 17:51
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionDenied {
    int requestCode() default PermissionUtil.DEFAULT_DENIED_CODE;
}
