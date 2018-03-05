
# CommonLibrary
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![Download](https://api.bintray.com/packages/alinlibrary/AlinMaven/common-library/images/download.svg) ](https://bintray.com/alinlibrary/AlinMaven/common-library/_latestVersion)

> [CommonLibrary](https://github.com/Alin520/CommonLibrary)是一个工具类通用框架，旨在为开发者提供各类开发工具，以提高开发效率。本库也将持续更新


## 版本更新记录

[版本更新记录](https://github.com/Alin520/CommonLibrary/blob/master/CHANGELOG.md)

# 联系方式</br>
      github地址：https://github.com/Alin520/CommonLibrary。
      掘金地址：https://juejin.im/post/5a7941c96fb9a0633f0dfe64
      CSDN地址：http://blog.csdn.net/hailin123123/article/details/79266112
      联系方式:
      欢迎加入QQ群：707202045</br>
  ![indicator样式风格](https://user-gold-cdn.xitu.io/2018/2/6/1616a124eff7cae3?w=412&h=562&f=png&s=56165)


# 一、使用说明
## 添加依赖即可使用（必选）

```
dependencies {
   implementation 'com.alin:common-library:1.0.1'
}
    
```


# 二、CommonLibrary库内容、特点说明
## 1、日志log输出
###### （1）第一步：在当前类添加注解@TargetLog(类名.class)
###### （2）第二步：打印日志,LogUtil.showLog(MainActivity.class,"xxxx打印的内容");
######  注意：默认输出的是Log.d格式的日志，如果想输出其他格式（例如Log.e），则LogUtil.showLog(MainActivity.class,"xxxx打印的内容", LogUtil.Logs.e);
具体示例代码如下：
    
```
@TargetLog(MainActivity.class)
public class MainActivity extends CommonActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.text_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_tv:
                LogUtil.showLog(MainActivity.class,"xxxx打印的内容");
                LogUtil.showLog(MainActivity.class,"xxxx打印的内容", LogUtil.Logs.i);
                break;
        }
    }
}

```

## 2、倒计时控件CountDownView
>这个控件是针对于闪屏页的倒计时自定义的。
#### 使用方式
###### step1:

```
<com.alin.hourse.common.view.CountDownView
        android:id="@+id/countdownview"
        android:layout_margin="20dp"
        android:layout_alignParentEnd="true"
        android:layout_width="45dp"
        android:layout_height="45dp"
        alin:hasAnimator="true"
        />

```
       
###### step2:
```
mCountdownView.setOnCountDownFinishListener(this);	//设置倒计时，计时结束和取消倒计时监听
mCountdownView.start();	//开始倒计时

mCountdownView.cancle();	//取消倒计时

```

###### step3:（如果设置了setOnCountDownFinishListener监听，必须实现下面的方法）
```
    @Override
    public void onFinishListener() {
  		//TODO: 倒计时计时结束回调
    }

    @Override
    public void onCancleListener() {
      //TODO: 取消倒计时回调
    }
```

## 3、可以设置圆角的自定义View
>分类：</br>
>CommonFrameLayout</br>
>CommonRelativeLayout</br>
>CommonLinearLayout</br>

## 4、根据数据加载状态，显示不同的状态页面（成功、失败、数据为空等页面）
>分类：</br>
>RoundReactFrameLayout</br>
>RoundReactRelativeLayout</br>
>RoundRectLinearLayout</br>

#####使用方式
```
<com.alin.commonlibrary.view.RoundReactFrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/test_rllyt"
    app:emptyView="@layout/layout_empty"	//数据为空页面
    app:errorView="@layout/layout_error"	//失败页面
    app:loadingView="@layout/layout_loading"	//正在加载中页面
    android:orientation="vertical"
    android:background="@color/white"
    app:isShowContentView="true"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/number_tv"
        android:text="TestActivity开启presenter"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:textColor="@color/Blue"
        android:tag="我是number"
        />
</com.alin.commonlibrary.view.RoundReactFrameLayout>
```

## 5、通用的底部NavigateTabBar
[使用方式](https://github.com/Alin520/CommonLibrary/blob/master/CHANGELOG.md)

#####使用方式
```
<com.alin.commonlibrary.view.RoundReactFrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/test_rllyt"
    app:emptyView="@layout/layout_empty"	//数据为空页面
    app:errorView="@layout/layout_error"	//失败页面
    app:loadingView="@layout/layout_loading"	//正在加载中页面
    android:orientation="vertical"
    android:background="@color/white"
    app:isShowContentView="true"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/number_tv"
        android:text="TestActivity开启presenter"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:textColor="@color/Blue"
        android:tag="我是number"
        />
</com.alin.commonlibrary.view.RoundReactFrameLayout>
```
如果你觉得[CommonLibrary](https://github.com/Alin520/CommonLibrary)能帮到你真正解决项目中的问题，就在博客中个我点个赞，或者去我的[CommonLibrary](https://github.com/Alin520/CommonLibrary)中star。
如果项目中有问题，可以直接给我留言。 </br>  


# 联系方式</br>
      github地址：https://github.com/Alin520/CommonLibrary。
      掘金地址：https://juejin.im/post/5a7941c96fb9a0633f0dfe64
      CSDN地址：http://blog.csdn.net/hailin123123/article/details/79266112
      联系方式:
      欢迎加入QQ群：707202045
  ![indicator样式风格](https://user-gold-cdn.xitu.io/2018/2/6/1616a124eff7cae3?w=412&h=562&f=png&s=56165)
