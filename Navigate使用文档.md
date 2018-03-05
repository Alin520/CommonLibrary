# NavigateTabBar
> NavigateTabBar是一通用的tab切换View。

#一、效果图
![这里写图片描述](http://img.blog.csdn.net/20180305174910393?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaGFpbGluMTIzMTIz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

# 二、使用说明
## step1:添加依赖（必选）

```
dependencies {
   implementation 'com.alin:common-library:1.0.1'
}
    
```
## step2:布局文件中添加（必选）
```
<com.alin.commonlibrary.view.NavigateTabBar
        android:id="@+id/navigate_tab_bar"
        android:layout_height="56dp"
        android:layout_width="match_parent"
        alin:navigateTabDefaultTextColor="@color/grey_all_9"
        alin:navigateSelectTabTextColor="@color/black"
        alin:navigateTabTextSize="14sp"
        alin:contentViewId="@+id/content_view_flyt"
        android:gravity="bottom"
        ></com.alin.commonlibrary.view.NavigateTabBar>
    
```

## step3:代码中添加（必选）

```
mNavigateTabBar.addNavigateTab(VideoFragment.class, new NavigateTabBar.TabBarParam("视频",
                R.mipmap.video, R.mipmap.video_selected));
```

## step4:添加其他属性（可选）
```
	//添加NavigateTabBar点击监听
     mNavigateTabBar.setOnClickTabListenter(new NavigateTabBar.OnClickTabListenter() {
            @Override
            public void onTabClick(NavigateTabBar.ViewHolder holder) {
                ToastUtil.showBottomToast(NavigateTabActivity.this,"点击的是" + holder.currentTabIndex + "个，内容是：" + holder.tag);
             

            }
        }); 

	//设置每个NavigateTabBar上的消息个数
        mNavigateTabBar.setMessageDotType(true,2);
	//设置NavigateTabBar被选中的位置
        mNavigateTabBar.setNavigateTabSelect(2);

```

#三、属性说明
```
    <declare-styleable name="navigatetabbar">
        <!--默认的字体颜色-->
        <attr name="navigateTabDefaultTextColor" format="reference|color"></attr>
        <!--选中的字体颜色-->
        <attr name="navigateSelectTabTextColor" format="color|reference"></attr>
        <!--文字的字体大小-->
        <attr name="navigateTabTextSize" format="reference|dimension"></attr>
        <!--fragment需要显示位置的布局id-->
        <attr name="contentViewId" format="reference|integer"></attr>
        <!--背景颜色-->
        <attr name="backgroudColor" format="color|reference"></attr>
        <!--消息字体大小-->
        <attr name="messageSize" format="dimension|reference"></attr>
        <!--消息字体颜色-->
        <attr name="messageColor" format="color|reference"></attr>
    </declare-styleable>
```