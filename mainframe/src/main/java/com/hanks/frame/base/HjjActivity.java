package com.hanks.frame.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.hanks.frame.utils.UStatusBar;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.utils.permission.PermissionReq;

import static com.hanks.frame.utils.UmengUtils.onPauseToActivity;
import static com.hanks.frame.utils.UmengUtils.onResumeToActivity;

public class HjjActivity extends AppCompatActivity implements DialogInterface {
    public HjjActivity mActivity;
    private ProgressDialog dialog;//显示等待的dialog

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setOnTranslucent();
        super.onCreate(savedInstanceState);
        mActivity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initDialog();
    }


    /**
     * 启用 透明状态栏(重写此方法可以取消透明)
     */
    protected void setOnTranslucent() {
        UStatusBar.enableTranslucentStatusbar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeToActivity(this);
        Log.i(Ulog.TAG + "ui-activity", "(" + getClass().getSimpleName() + ".java:0)");

    }

    @Override
    protected void onPause() {
        onPauseToActivity(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        dealOnBackPressed();
        super.onBackPressed();
    }

    public void dealOnBackPressed() {
    }

    /**
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在加载，请稍后……");

    }

    @Override
    public void showDialog() {
        if (dialog == null) return;
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog == null) return;
        dialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
