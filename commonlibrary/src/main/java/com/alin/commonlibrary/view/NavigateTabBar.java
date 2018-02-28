package com.alin.commonlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alin.commonlibrary.R;
import com.alin.commonlibrary.util.AppUtil;
import com.alin.commonlibrary.util.CommonUtil;
import com.alin.commonlibrary.util.ResourcesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 hailin
 * @创建时间 2017/11/28 17:38
 * @描述 ${}.
 */

public class NavigateTabBar extends LinearLayout implements View.OnClickListener {
    private static final String VIEW_HOLD_TAG_KEY = "view_hold_tag_key";      //默认字体到小
    private static final int DEFAULT_SIZE_TAB_BAR = 12;      //默认字体到小
    private static final int DEFAULT_MESSAGE_SIZE_TAB_BAR = 7;      //默认字体到小

    private LayoutInflater   mInflater;
    private float            mTextSize;
    private ColorStateList   mDefaultColor;
    private ColorStateList   mSelectColor;
    private View             mView;
    private ImageView        mNavigateTabIv;
    private TextView         mNavigateTabTv;
    private LinearLayout     mNavigateTabLlyt;
    private List<ViewHolder> mHoldersList;
    private TabBarParam      mTabBarParam;
    private ColorStateList   mDefaultList;
    private ColorStateList   mSelectList;
    private ViewHolder       mHolder;
    private FragmentActivity mFragmentActivity;
    private String           mCurrentTag;
    private int              mCurrentTabIndex;
    private int              mTabIndex;
    private int              mContentViewId;
    private ColorStateList mBackgroundList;
    private ColorStateList mBackgroundColor;
    private String mRestoreTag;
    private int mMessageCount;
    private float mMessageSize;
    private ColorStateList mMessageColorList;
    private ColorStateList mMessageColor;
    public Context mContext;

    public NavigateTabBar(Context context) {
        this(context,null);
    }

    public NavigateTabBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigateTabBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(attrs);
    }

    @SuppressLint("ServiceCast")
    private void initialize(AttributeSet attrs) {
        mContext = getContext();
        setClipChildren(false);
        mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.navigatetabbar);
        try {
            if (ta != null) {
                mDefaultList = ta.getColorStateList(R.styleable.navigatetabbar_navigateTabDefaultTextColor);
                mSelectList = ta.getColorStateList(R.styleable.navigatetabbar_navigateSelectTabTextColor);
                mTextSize = ta.getDimension(R.styleable.navigatetabbar_navigateTabTextSize, CommonUtil.dp2px(mContext,DEFAULT_SIZE_TAB_BAR));
                mContentViewId = ta.getResourceId(R.styleable.navigatetabbar_contentViewId, 0);
                mBackgroundList = ta.getColorStateList(R.styleable.navigatetabbar_backgroudColor);
                mMessageSize = ta.getDimension(R.styleable.navigatetabbar_messageSize, CommonUtil.dp2px(mContext,DEFAULT_MESSAGE_SIZE_TAB_BAR));
                mMessageColorList = ta.getColorStateList(R.styleable.navigatetabbar_messageColor);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            ta.recycle();
        }

        mHoldersList = new ArrayList<>();
    }
    public void addNavigateTab(Class fragmentClass,TabBarParam tabBarParam) {
        AppUtil.checkNotNull(tabBarParam,"TabBarParam == null,addNavigateTab before init TabBarParam!");
        mHolder = new ViewHolder();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.navigate_tab_layout, null);
        mHolder.imageView = mView.findViewById(R.id.navigate_tab_iv);
        mHolder.textView = mView.findViewById(R.id.navigate_tab_tv);
        mHolder.message = mView.findViewById(R.id.navigate_tab_no_read_message);
        initTabBarParam(tabBarParam);
        mHolder.fragmentClass = fragmentClass;
        mHolder.param = tabBarParam;
        mHolder.tag = tabBarParam.text;
        setDefaultNavigateTabView();
        mHolder.currentTabIndex = mHoldersList.size();
        mHoldersList.add(mHolder);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        addView(mView,layoutParams);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getContext() instanceof FragmentActivity)) {
            throw new RuntimeException("parent activity must is extends FragmentActivity");
        }
        if (mContentViewId == 0) {
            throw new IllegalArgumentException("ContentViewId = 0,ContentViewId is not initialize !");
        }
        mFragmentActivity = (FragmentActivity) getContext();
        if (mHoldersList.isEmpty()) {
            throw new ExceptionInInitializerError("HoldersList is empty");
        }
        hideAllFragment();
        ViewHolder defaultHolder = null;
        if (!TextUtils.isEmpty(mRestoreTag)) {
            if (mHoldersList.size() > mCurrentTabIndex) {
                for (ViewHolder holder : mHoldersList) {
                    if (TextUtils.equals(mRestoreTag,holder.tag)) {
                        defaultHolder = holder;
                    }
                }
            }
            mRestoreTag = null;
        }else {
            if (mHoldersList.size() > mCurrentTabIndex) {
                defaultHolder = mHoldersList.get(mCurrentTabIndex);
            }
        }
        showFragment(defaultHolder);
    }

//    横竖屏切换、内存溢出等异常
    public void onSaveInstanceState(Bundle outState){
        if (!TextUtils.isEmpty(mCurrentTag))
            outState.putString(VIEW_HOLD_TAG_KEY,mCurrentTag);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        mRestoreTag = savedInstanceState.getString(VIEW_HOLD_TAG_KEY);
    }

//    隐藏所有的fragment
    private void hideAllFragment() {
        for (ViewHolder holder : mHoldersList) {
            try {
                holder.fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean currentFragmentShowing(FragmentTransaction transaction, String newTag) {
        AppUtil.checkNotNull(newTag,"newTag == null,currentFragmentShowing() before set tag!");
        if (TextUtils.isEmpty(mCurrentTag)) {
            return false;
        }
        if (TextUtils.equals(newTag,mCurrentTag)) {
            return true;
        }

        Fragment fragment = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(mCurrentTag);
        if (fragment != null && !fragment.isHidden()) {
            transaction.hide(fragment);
        }
        return false;
    }

//    展示被点击后tab对应的fragment
    public void showFragment(ViewHolder holder){
        AppUtil.checkNotNull(holder,"ViewHolder == null,showFragment before init ViewHolder!");
        FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        if(currentFragmentShowing(transaction,holder.tag)){       //当前fragment是show的状态，则直接返回
            return;
        }

        FragmentManager fm = mFragmentActivity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(holder.tag);
        if (fragment != null) {
            transaction.show(fragment);
        }else {
            fragment = getFragmentInstance(holder);
            transaction.add(mContentViewId,fragment,holder.tag);
        }
        transaction.commit();
        setSelectNavigateTabView(holder);
        mCurrentTabIndex = holder.currentTabIndex;
        mCurrentTag = holder.tag;
    }

//    设置被点击选中的NavigateTab状态
    private void setSelectNavigateTabView(ViewHolder viewHolder) {
        AppUtil.checkNotNull(viewHolder,"ViewHolder == null,setSelectNavigateTabState() params is not null!");
        if (TextUtils.equals(viewHolder.tag,mCurrentTag)) {
            return;
        }
        if (mHoldersList.size() > 0) {
            for (ViewHolder holder : mHoldersList) {
                if (viewHolder.tag.equals(holder.tag)) {        //选中状态
                    if (holder.param.textSelectColor != null) {
                        holder.textView.setTextColor(holder.param.textSelectColor);
                    }
                    if (holder.param.iconSelectId != 0) {
                        holder.imageView.setImageResource(holder.param.iconSelectId);
                    }
                }else {
                    if (holder.param.textDefaultColor != null) {
                        holder.textView.setTextColor(holder.param.textDefaultColor);
                    }
                    if (holder.param.iconDefaultId != 0) {
                        holder.imageView.setImageResource(holder.param.iconDefaultId);
                    }

                    if (holder.param.imageHeight > 0 && holder.param.imageWidth > 0) {
                        LayoutParams params = new LayoutParams(CommonUtil.dp2px(mContext, holder.param.imageWidth), CommonUtil.dp2px(mContext, holder.param.imageHeight));
                        params.gravity = Gravity.BOTTOM;
                        holder.imageView.setLayoutParams(params);
                    }
                }
                generateShowRedDot(holder);
            }
        }
    }

    private Fragment getFragmentInstance(@Nullable ViewHolder holder) {
        AppUtil.checkNotNull(holder,"ViewHolder == null,getFragmentInstance() params is not null!");
        Fragment fragment = null;
        try {
            fragment = (Fragment) holder.fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }



    //设置NavigateTabView默认数据
    private void setDefaultNavigateTabView() {
        mHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mHolder.param.textSize);
        mHolder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX,mHolder.param.messageSize);
        mHolder.textView.setTextColor(mHolder.param.textDefaultColor);
        if (TextUtils.isEmpty(mHolder.param.text)) {
            mHolder.textView.setVisibility(View.GONE);
        }else {
            mHolder.textView.setVisibility(View.VISIBLE);
            mHolder.textView.setText(mHolder.param.text);
        }

        if (mHolder.param.iconDefaultId == 0) {
            mHolder.imageView.setVisibility(View.GONE);
        }else {
            mHolder.imageView.setVisibility(View.VISIBLE);
            mHolder.imageView.setImageResource(mHolder.param.iconDefaultId);
        }

        mView.setTag(mHolder);
        mView.setOnClickListener(this);
    }

    private void initTabBarParam(TabBarParam tabBarParam) {
        mBackgroundColor = mBackgroundList != null ? mBackgroundList : ResourcesUtil.getColorStateList(getContext(),R.color.grey_all_9);
        mDefaultColor = mDefaultList != null ? mDefaultList : ResourcesUtil.getColorStateList(getContext(),R.color.black);
        mSelectColor = mSelectList != null ? mSelectList : ResourcesUtil.getColorStateList(getContext(),R.color.blue);
        mMessageColor = mMessageColorList != null ? mMessageColorList : ResourcesUtil.getColorStateList(getContext(),R.color.red);
        tabBarParam.backGroundColor = mBackgroundColor;
        tabBarParam.textDefaultColor = mDefaultColor;
        tabBarParam.textSelectColor = mSelectColor;
        tabBarParam.messageColor = mMessageColor;
        tabBarParam.textSize = mTextSize;
        tabBarParam.messageSize = mMessageSize;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) tag;
            showFragment(holder);
            if (mListenter != null) {
                mListenter.onTabClick(holder);
            }
        }else {
            throw new RuntimeException("v.getTag() == null or v.getTag() is not belong ViewHolder!");
        }

    }

    //    当前被选中的索引
    public int getCurrentTabIndex() {
        return mCurrentTabIndex;
    }

    //    设置任意被选中的tab
    public void setNavigateTabSelect(int index) {
        if (index < 0 || index >= mHoldersList.size()) {
            throw new IllegalArgumentException("setNavigateTabSelect() param 'index' must be 'index > 0 && holdersList.size() > index!");
        }
        showFragment(mHoldersList.get(index));
    }

//    设置红点
    public NavigateTabBar setMessageCount(int messageCount,int index){
        if (index < 0 || index >= mHoldersList.size()) {
            throw new IllegalArgumentException("setMessageCount() param 'index' must be 'index > 0 && holdersList.size() > index!");
        }
        mHoldersList.get(index).param.messageDotType = false;
        mHoldersList.get(index).param.messageCount = messageCount;
        generateShowRedDot(mHoldersList.get(index));
        return this;
    }

    public NavigateTabBar setMessageDotType(boolean messageDotType){
        for (ViewHolder holder : mHoldersList) {
            holder.param.messageDotType = messageDotType;
        }
        generateShowRedDot(mHoldersList.get(0));
        return this;
    }


    public NavigateTabBar setMessageDotType(boolean messageDotType,int index){
        if (index < 0 || index >= mHoldersList.size()) {
            throw new IllegalArgumentException("setMessageDotType() param 'index' must be 'index > 0 && holdersList.size() > index!");
        }
        mHoldersList.get(index).param.messageDotType = messageDotType;
        mHolder.param.messageDotType = messageDotType;
        generateShowRedDot( mHoldersList.get(index));
        return this;
    }

    //显示消息
    private void generateShowRedDot(ViewHolder holder) {
        AppUtil.checkNotNull(holder,"generateShowFragment() params holder == null !");
        ViewGroup.LayoutParams params = holder.message.getLayoutParams();
        if (holder.param.messageDotType) {      //点的形式
            params.height = CommonUtil.dp2px(getContext(),7);
            params.width = CommonUtil.dp2px(getContext(),7);
            holder.message.setLayoutParams(params);
            holder.message.setVisibility(View.VISIBLE);
        }else {
            params.height = CommonUtil.dp2px(getContext(),18);
            params.width = CommonUtil.dp2px(getContext(),18);
            holder.message.setLayoutParams(params);
            if (holder.param.messageCount <= 0) {
                holder.message.setVisibility(View.GONE);
            }else if (holder.param.messageCount > 9){
                holder.message.setVisibility(View.VISIBLE);
                holder.message.setText("9+");
            }else {
                holder.message.setVisibility(View.VISIBLE);
                holder.message.setText(holder.param.messageCount+"");
            }
        }
    }

    public float getTextSize() {
        return mHolder.param.textSize;
    }

//    字体大小
    public void setTextSize(float textSize) {
        mHolder.param.textSize = CommonUtil.dp2px(getContext(),textSize);
    }

    public ColorStateList getDefaultList() {
        return mDefaultList;
    }

//    字体颜色
    public void setDefaultList(ColorStateList defaultList) {
        mDefaultList = defaultList;
    }

    public void setDefaultList(int defaultColorId) {
        mDefaultList = ColorStateList.valueOf(defaultColorId);
    }

    public ColorStateList getSelectList() {
        return mSelectList;
    }

    public void setSelectList(int selectColorId) {
        mSelectList = ColorStateList.valueOf(selectColorId);
    }

    public void setSelectList(ColorStateList selectList) {
        mSelectList = selectList;
    }


    public void setOnClickTabListenter(OnClickTabListenter listenter){
        this.mListenter = listenter;
    }

    public OnClickTabListenter mListenter;

    public interface OnClickTabListenter{
        void onTabClick(ViewHolder holder);
    }

    public class ViewHolder{
        public String tag;
        public TabBarParam param;
        public Class fragmentClass;
        public TextView textView;
        public TextView message;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public int currentTabIndex;
    }

    //    底部tab参数
    public static class TabBarParam{
        public int backGroundId;                        //背景色id
        public ColorStateList textDefaultColor;         //默认字体颜色
        public ColorStateList textSelectColor;          //选中字体颜色
        public float textSize;
        public String text;
        public int iconDefaultId;                      //默认图片id
        public int iconSelectId;                        //选中图片id
        public ColorStateList backGroundColor;
        public ColorStateList messageColor;
        public int messageCount;                        //消息条数
        public float messageSize;
        public boolean messageDotType = false;              //消息是点的形式
        public int imageWidth;                              //图片宽度
        public int imageHeight;                             //图片高度

        public TabBarParam(String text, int iconDefaultId, int iconSelectId) {
            this.text = text;
            this.iconDefaultId = iconDefaultId;
            this.iconSelectId = iconSelectId;
        }

        public TabBarParam(int iconDefaultId, int iconSelectId) {
            this.iconDefaultId = iconDefaultId;
            this.iconSelectId = iconSelectId;
        }

        public TabBarParam(String text,int backGroundId, int iconDefaultId, int iconSelectId) {
            this.backGroundId = backGroundId;
            this.iconDefaultId = iconDefaultId;
            this.iconSelectId = iconSelectId;
        }


        public TabBarParam(String text, int iconDefaultId) {
            this.text = text;
            this.iconDefaultId = iconDefaultId;
        }

        public TabBarParam(int backGroundId, String text, int iconDefaultId) {
            this.backGroundId = backGroundId;
            this.text = text;
            this.iconDefaultId = iconDefaultId;
        }

        public TabBarParam(int backGroundId, String text, int iconDefaultId, int iconSelectId) {
            this.backGroundId = backGroundId;
            this.text = text;
            this.iconDefaultId = iconDefaultId;
            this.iconSelectId = iconSelectId;
        }

        public TabBarParam setMessageCount(int messageCount){
            this.messageCount = messageCount;
            return this;
        }

        public TabBarParam setMessageSize(Context context,float messageSize){
            this.messageSize = CommonUtil.dp2px(context,messageSize);
            return this;
        }

        public TabBarParam setMessageSize(boolean messageDotType){
            this.messageDotType = messageDotType;
            return this;
        }

//        public void setImageWidth(int imageWidth) {
//            this.imageWidth = imageWidth;
//        }
//
//        public void setImageHeight(int imageHeight) {
//            this.imageHeight = imageHeight;
//        }
    }
}
