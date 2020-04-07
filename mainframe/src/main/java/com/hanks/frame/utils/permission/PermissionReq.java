package com.hanks.frame.utils.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

import com.hanks.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Android运行时权限申请
 * <p>
 * 需要申请的权限列表，<a "href=https://developer.android.google.cn/guide/topics/security/permissions.html?hl=zh-cn#normal-dangerous">Google Doc</a>
 * <p>
 * -CALENDAR<br>
 * {@link android.Manifest.permission#READ_CALENDAR}<br>
 * {@link android.Manifest.permission#WRITE_CALENDAR}<br>
 * <p>
 * -CAMERA<br>
 * {@link android.Manifest.permission#CAMERA}<br>
 * <p>
 * -CONTACTS<br>
 * {@link android.Manifest.permission#READ_CONTACTS}<br>
 * {@link android.Manifest.permission#WRITE_CONTACTS}<br>
 * {@link android.Manifest.permission#GET_ACCOUNTS}<br>
 * <p>
 * -LOCATION<br>
 * {@link android.Manifest.permission#ACCESS_FINE_LOCATION}<br>
 * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION}<br>
 * <p>
 * -MICROPHONE<br>
 * {@link android.Manifest.permission#RECORD_AUDIO}<br>
 * <p>
 * -PHONE<br>
 * {@link android.Manifest.permission#READ_PHONE_STATE}<br>
 * {@link android.Manifest.permission#CALL_PHONE}<br>
 * {@link android.Manifest.permission#READ_CALL_LOG}<br>
 * {@link android.Manifest.permission#WRITE_CALL_LOG}<br>
 * {@link android.Manifest.permission#ADD_VOICEMAIL}<br>
 * {@link android.Manifest.permission#USE_SIP}<br>
 * {@link android.Manifest.permission#PROCESS_OUTGOING_CALLS}<br>
 * <p>
 * -SENSORS<br>
 * {@link android.Manifest.permission#BODY_SENSORS}<br>
 * <p>
 * -SMS<br>
 * {@link android.Manifest.permission#SEND_SMS}<br>
 * {@link android.Manifest.permission#RECEIVE_SMS}<br>
 * {@link android.Manifest.permission#READ_SMS}<br>
 * {@link android.Manifest.permission#RECEIVE_WAP_PUSH}<br>
 * {@link android.Manifest.permission#RECEIVE_MMS}<br>
 * <p>
 * -STORAGE<br>
 * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}<br>
 * {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE}<br>
 */
public class PermissionReq {
    private static int sRequestCode = 0;
    private static SparseArray<PermissionResult> sResultArray = new SparseArray<>();

    private Object mObject;
    private String[] mPermissions;
    private PermissionResult mResult;

    private PermissionReq(Object object) {
        mObject = object;
    }

    public static PermissionReq with(@NonNull Activity activity) {
        return new PermissionReq(activity);
    }

    public static PermissionReq with(@NonNull Fragment fragment) {
        return new PermissionReq(fragment);
    }

    public PermissionReq permissions(@NonNull String... permissions) {
        mPermissions = permissions;
        return this;
    }

    public PermissionReq result(@Nullable PermissionResult result) {
        mResult = result;
        return this;
    }

    public void request() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mResult != null) {
                mResult.onNext();
                mResult.onGranted();
            }
            return;
        }

        Activity activity = getActivity(mObject);
        if (activity == null) {
            throw new IllegalArgumentException(mObject.getClass().getName() + " is not supported");
        }

        List<String> deniedPermissionList = getDeniedPermissions(activity, mPermissions);
        if (deniedPermissionList.isEmpty()) {
            if (mResult != null) {
                mResult.onNext();
                mResult.onGranted();
            }
            return;
        }

        int requestCode = genRequestCode();
        String[] deniedPermissions = deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
        requestPermissions(mObject, deniedPermissions, requestCode);
        sResultArray.put(requestCode, mResult);
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionResult result = sResultArray.get(requestCode);

        if (result == null) {
            return;
        }

        sResultArray.remove(requestCode);
        result.onNext();
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                result.onDenied();
                return;
            }
        }
        result.onGranted();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, String[] permissions, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).requestPermissions(permissions, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(permissions, requestCode);
        }
    }

    private static List<String> getDeniedPermissions(Context context, String[] permissions) {
        List<String> deniedPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission);
            }
        }
        return deniedPermissionList;
    }

    private static Activity getActivity(Object object) {
        if (object != null) {
            if (object instanceof Activity) {
                return (Activity) object;
            } else if (object instanceof Fragment) {
                return ((Fragment) object).getActivity();
            }
        }
        return null;
    }

    /**
     * 判断是否有权限
     *
     * @param context
     * @param permissionType 以下是用法
     *                       PermissionUtil.needPermission(mActivity, code, Manifest.permission.CAMERA);
     *                       private final int code = 3;
     * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     * PermissionUtil.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
     * }
     * <p>
     * if (!PermissionUtil.judgePermisson(this,"ACCESS_FINE_LOCATION")) return;
     */

    public static boolean judgePermisson(final Context context, String permissionType) {
        String str = null;
        PackageManager packageManager = context.getPackageManager();
        int permission = packageManager.checkPermission(permissionType, context.getPackageName());
        if (PackageManager.PERMISSION_GRANTED == permission) {
            return true; //有这个权限
        } else {
            if (permissionType.contains("READ_PHONE_STATE")) {
                str = "访问电话状态";
            }
            if (permissionType.contains("WRITE_EXTERNAL_STORAGE")) {
                str = "允许程序写入外部存储";
            }
            if (permissionType.contains("CAMERA")) {
                str = "相机权限";
            }
            if (permissionType.contains("ACCESS_FINE_LOCATION")) {
                str = "定位";
            }
            dialog(context, context.getString(R.string.permission_note, str));
            return false; //没有这个权限
        }
    }

    /**
     * 不显示dialog
     * @param context
     * @param permissionType
     * @return
     */
    public static boolean judgePermisson2(final Context context, String permissionType) {
        String str = null;
        PackageManager packageManager = context.getPackageManager();
        int permission = packageManager.checkPermission(permissionType, context.getPackageName());
        if (PackageManager.PERMISSION_GRANTED == permission) {
            return true; //有这个权限
        } else {
            return false; //没有这个权限
        }
    }

    /**
     * 弹出设置权限dialog
     *
     * @param context
     * @param message
     */
    public static void dialog(final Context context, String message) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName())); // 根据包名打开对应的设置界面
                context.startActivity(intent);
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();

    }

    private static int genRequestCode() {
        return ++sRequestCode;
    }

}
