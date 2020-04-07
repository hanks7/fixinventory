package com.easyway.fixinventory.ui.login;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.easyway.fixinventory.MainActivity;
import com.easyway.fixinventory.MainOffActivity;
import com.easyway.fixinventory.R;
import com.easyway.fixinventory.base.BaseApplication;
import com.easyway.fixinventory.dao.DBIpPortManager;
import com.easyway.fixinventory.dao.DbHelper;
import com.easyway.fixinventory.model.UserBean;
import com.easyway.fixinventory.utils.UtilBase64;
import com.easyway.fixinventory.utils.http.HttpAdapter;
import com.easyway.fixinventory.utils.http.OnResponseListener2;
import com.easyway.fixinventory.utils.http.OnResponseListener3;
import com.hanks.frame.base.HjjActivity;
import com.hanks.frame.utils.UPermissionsTool;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Uintent;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.utils.UtilSystem;
import com.hanks.frame.utils.permission.PermissionReq;
import com.hanks.frame.utils.permission.PermissionResultTask;
import com.hanks.frame.view.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.easyway.fixinventory.dao.DBIpPortManager.tb_login_account;

public class LoginActivity extends HjjActivity {

    @BindView(R.id.login_edt_username)
    AutoCompleteTextView edtUsername;
    @BindView(R.id.login_edt_pwd)
    AutoCompleteTextView edtPassword;

    @BindView(R.id.login_tv_version)
    TextView tvVersion;
    @BindView(R.id.login_check_box_off_line)
    CheckBox mCbOffline;

    @BindView(R.id.topbar)
    TopBar topbar;

    private DBIpPortManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getPermission();
        initView();
        initAdapter();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        topbar.setIvRightClick(R.drawable.ic_settings_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uintent.intentDIYLeftToRight(LoginActivity.this, ChangeIPActivity.class);
            }
        });
        String strVersion = String.format("版本号: %s  版本名称: %s", UtilSystem.getVersionCode(), UtilSystem.getVersionName());
        tvVersion.setText(strVersion);
    }

    /**
     * 新建适配器，适配器数据为搜索历史文件内容
     */
    private void initAdapter() {
        db = new DBIpPortManager(this);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, db.queryAll(tb_login_account));
        edtUsername.setAdapter(adapter);
    }

    /**
     * 网络请求前判断
     */
    private void initPermissionReq() {
        String strAccount = edtUsername.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            UToast.showText("账户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            UToast.showText("密码不能为空");
            return;
        }

        PermissionReq.with(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionResultTask(Manifest.permission.READ_EXTERNAL_STORAGE, this) {
                    @Override
                    public void onGranted() {
                        loginControl();//离线登录的业务
                    }
                })
                .request();
    }

    /**
     * 登录的网络请求
     */
    private void loginControl() {
        final String strAccount = edtUsername.getText().toString().trim();
        final String strPassword = edtPassword.getText().toString().trim();
        if (mCbOffline.isChecked()) {
            //离线
            DbHelper.getbean(this).queryEmployee(strAccount, new OnResponseListener3<UserBean>() {
                @Override
                public void onSuccess(UserBean bean) {
                    Ulog.i("aa-UtilBase64", UtilBase64.decode(bean.getPassword()));
                    Ulog.i("aa-strPassword", strPassword);
                    if (!strPassword.equals(UtilBase64.decode(bean.getPassword()))) {
                        UToast.showSnackBar(edtUsername, "账号密码错误", R.color.red, R.color.white);
                        return;
                    }
                    BaseApplication.getInstance().setUserModel(bean, 2);//将个人信息存到sharedperence和内存中
                    db.insert(strAccount, tb_login_account);
                    Uintent.intentDIY(mActivity, MainOffActivity.class);
                    finish();
                }

                @Override
                public void onError(int code, String strToast) {
                    UToast.showSnackBar(edtUsername, strToast, R.color.red, R.color.white);
                }
            });
        } else {
            //在线
            HttpAdapter.getService().login(strAccount, strPassword).enqueue(new OnResponseListener2<UserBean>(this) {
                @Override
                public void onSuccess(UserBean bean) {
                    BaseApplication.getInstance().setUserModel(bean, 1);//将个人信息存到sharedperence和内存中
                    db.insert(strAccount, tb_login_account);
                    Uintent.intentDIY(mActivity, MainActivity.class);
                    finish();
                }

                @Override
                public void onError(int code, String strToast) {
                    UToast.showSnackBar(edtUsername, strToast, R.color.red, R.color.white);
                }
            });
        }

    }

    /**
     * 请求用户获取所有权限
     */
    private void getPermission() {
        UPermissionsTool.
                getIntance(this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    @OnClick({R.id.userlogin, R.id.login_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userlogin:
                initPermissionReq();
                break;
            case R.id.login_ll:
                break;
        }
    }

}
