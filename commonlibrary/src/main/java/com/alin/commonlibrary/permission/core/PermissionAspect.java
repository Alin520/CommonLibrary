package com.alin.commonlibrary.permission.core;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.alin.commonlibrary.permission.annotation.Permission;
import com.alin.commonlibrary.permission.annotation.PermissionCancled;
import com.alin.commonlibrary.permission.annotation.PermissionDenied;
import com.alin.commonlibrary.permission.util.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/10 17:52
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
@Aspect
public class PermissionAspect {
    private static final String TAG = "PermissionAspect";

    //定义切面的规则
    //1.就在原来应用中哪些注释的地方放到当前切面进行处理
    //execution(注释名   注释用的地方)
    //  1、 execution( @com.dn.tim.lib_permission.annotation.Permission(切点函数)
    //              *(类名,*表示任意的类都可以使用切点函数)  *(方法名,*表示任意方法)(..(方法的参数，..表示任意参数)) )
    //  2、@annotation(permission)：传入切点函数需要传入的参数是注解类型的permission
    @Pointcut("execution(@com.alin.commonlibrary.permission.annotation.Permission * * (..)) && @annotation(permission)")
    public void requestPermission(Permission permission){
        Log.i(TAG,"Pointcut===>");
    }

    //2.对进入切面的内容如何处理
    //advice
    //@Before()  在切入点之前运行
    //@After()   在切入点之后运行
    //@Around()  在切入点前后都运行
    @Around("requestPermission(permission)")
    public void  aroundJointPoint(final ProceedingJoinPoint joinPoint, final Permission permission){
        final Object object = joinPoint.getThis();
        Context context = null;
        /**
         * 兼容Fragment、Service、Activity处理
         */
        if (object instanceof Context) {
            context = (Context) object;
        }else if (object instanceof Fragment){
            context = ((Fragment)object).getActivity();
        }else if (object instanceof android.app.Fragment){
            context = ((android.app.Fragment)object).getActivity();
        }

        if (context == null || permission == null) {
            Log.d(TAG, "aroundJonitPoint error ");
            return;
        }
        int statusBarColor = PermissionUtil.findStatusBarColor(object);
        final Context finalContext = context;

        /**
         * 跳转PermissionActivity,进行权限申请
         */
        PermissionActivity.requestPermission(context, permission.requestCode(), permission.values(),statusBarColor ,new IPermission() {
            /**
             * 成功
             */
            @Override
            public void granted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            /**
             * 拒绝
             */
            @Override
            public void denied(String[] permissions, int code) {
                Log.e(TAG,"denied===>" + code+ "");
                boolean dispatch = PermissionUtil.invokeAnnotation(object, PermissionDenied.class, code);
                if (dispatch) {
                    PermissionUtil.goToAppMenu(finalContext);
                }
            }

            /**
             * 取消
             */
            @Override
            public void canceled(String[] permissions, int code) {
                Log.e(TAG,"canceled===>" + code+ "");
                boolean dispatch = PermissionUtil.invokeAnnotation(object, PermissionCancled.class,code);
                if (dispatch) {
                    PermissionUtil.goToAppMenu(finalContext);
                }
            }
        });
    }

}
