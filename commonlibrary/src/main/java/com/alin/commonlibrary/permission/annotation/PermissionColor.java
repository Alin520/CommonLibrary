package com.alin.commonlibrary.permission.annotation;


import com.alin.commonlibrary.permission.util.PermissionUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/15 17:44
 * 版    本   ： ${TODO}
 * 描    述   ：  ${状态栏颜色值}
 * ================================================
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PermissionColor {

    String color() default PermissionUtil.DEFAULT_PERMISSION_COLOR;
}
