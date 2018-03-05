# NavigateTabBar
> NavigateTabBar是一通用的tab切换View。

# 一、使用说明
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