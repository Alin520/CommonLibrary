package com.alin.commonlib.dialig;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alin.commonlib.R;
import com.alin.commonlibrary.base.CommonDialog;


/**
 * @创建者 hailin
 * @创建时间 2017/9/13 11:25
 * @描述 ${数据加载进度loading}.
 */

@SuppressLint("ValidFragment")
public class LoadingProgressDialog extends CommonDialog {

    ImageView mImgLoading;

    TextView mLoadingInfo;

    private String mInfo;
    private boolean mCancelable = false;
    private final ProgressCancleListener mProgressCancleListener;

    @SuppressLint("ValidFragment")
    public LoadingProgressDialog(AppCompatActivity activity, boolean cancelable, ProgressCancleListener cancleListener) {
        super(activity, R.layout.loading_dialog, true,null);
        this.settCallback(new LoadingProgressCallback());
        mCancelable = cancelable;
        mProgressCancleListener = cancleListener;
    }



    public class LoadingProgressCallback implements DialogCallback{
        @Override
        public void initChildren(CommonDialog commonDialog, Dialog dialog, View contentView) {
            dialog.setCanceledOnTouchOutside(mCancelable);
            dialog.setCancelable(mCancelable);
            mImgLoading = contentView.findViewById(R.id.imgLoading);
            mLoadingInfo = contentView.findViewById(R.id.txtLoadingInfo);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (mProgressCancleListener != null) {
                        mProgressCancleListener.progressCancle(dialogInterface);
                    }
                }
            });

        }

        @Override
        public void afterCreated(AppCompatDialogFragment fragment, Dialog dialog) {
            if (!TextUtils.isEmpty(mInfo)) {
                mLoadingInfo.setText(mInfo);
                mLoadingInfo.setVisibility(View.VISIBLE);
            }else {
                mLoadingInfo.setVisibility(View.GONE);
            }

            if (mImgLoading != null) {
                ((AnimationDrawable)mImgLoading.getDrawable()).start();
            }
        }

        @Override
        public void onDismiss() {
            ((AnimationDrawable)mImgLoading.getDrawable()).stop();
        }
    }

    public void show(int infoId){
        this.setInfo(infoId);
        super.show();
    }

    public void show(String info){
        this.setInfo(info);
        super.show();
    }

    public String getInfo(){
        return this.mInfo;
    }

    private void setInfo(String info){
        this.mInfo = info;
    }

    private void setInfo(int infoId){
        this.setInfo(getResources().getString(infoId));
    }

}
