package com.hanks.frame.utils.permission;

import android.app.Activity;

public class PermissionResultTask implements PermissionResult {
    private String msg;
    private Activity activity;

    public PermissionResultTask(String msg, Activity activity) {
        this.msg = msg;
        this.activity = activity;
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied() {
        PermissionReq.judgePermisson(activity,msg);

    }

    @Override
    public void onNext() {

    }
}
