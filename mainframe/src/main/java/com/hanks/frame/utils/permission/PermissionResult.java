package com.hanks.frame.utils.permission;

public interface PermissionResult {
    void onGranted();

    void onDenied();
    void onNext();
}
