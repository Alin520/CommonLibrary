package com.alin.commonlibrary.permission.core;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/15 14:34
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public interface IPermission {

    /**
     * 已经授权
     */
    void granted();

    /**
     *被拒绝 点击了不再提示
     */
    void denied(String[] permissions, int code);

    /**
     * 取消授权
     */
    void canceled(String[] permissions, int code);
}
