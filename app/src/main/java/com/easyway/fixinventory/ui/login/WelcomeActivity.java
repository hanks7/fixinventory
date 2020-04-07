package com.easyway.fixinventory.ui.login;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyway.fixinventory.MainActivity;
import com.easyway.fixinventory.MainOffActivity;
import com.easyway.fixinventory.R;
import com.easyway.fixinventory.base.BaseApplication;
import com.hanks.frame.base.HjjActivity;
import com.hanks.frame.utils.UPermissionsTool;
import com.hanks.frame.utils.Uintent;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/5/28
 * @description 欢迎界面
 */
public class WelcomeActivity extends HjjActivity {

    @BindView(R.id.ac_welcome_iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.ac_welcome_iv_company_name)
    TextView mTvCompanyName;

    int DURATION = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        getPermission();//获取权限


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentActivity();
            }
        }, 1100);
        initAnimation();//初始化动画

    }

    /**
     * 初始化属性动画并执行
     */
    private void initAnimation() {
        float curTranslationY = mIvLogo.getTranslationY();
        ObjectAnimator OAtranslationY = ObjectAnimator.ofFloat(mIvLogo, "translationY", curTranslationY + 150f, curTranslationY);
        OAtranslationY.setDuration(DURATION);
        OAtranslationY.start();

        ObjectAnimator OAalpha = ObjectAnimator.ofFloat(mIvLogo, "alpha", 0.1f, 1f);
        OAalpha.setDuration(DURATION);
        OAalpha.start();

        float tVcurTranslationY2 = mTvCompanyName.getTranslationY();
        ObjectAnimator OAtVtranslationY = ObjectAnimator.ofFloat(mTvCompanyName, "translationY", tVcurTranslationY2 + 20, tVcurTranslationY2);
        OAtVtranslationY.setDuration(DURATION);
        OAtVtranslationY.start();

        ObjectAnimator OAtValpha = ObjectAnimator.ofFloat(mTvCompanyName, "alpha", 0.1f, 1f);
        OAtValpha.setDuration(DURATION);
        OAtValpha.start();

        ObjectAnimator OAtVscaleY = ObjectAnimator.ofFloat(mTvCompanyName, "scaleY", 0.1f, 1f);
        OAtVscaleY.setDuration(DURATION);
        OAtVscaleY.start();

        ObjectAnimator OAtVscaleX = ObjectAnimator.ofFloat(mTvCompanyName, "scaleX", 0.1f, 1f);
        OAtVscaleX.setDuration(DURATION);
        OAtVscaleX.start();

    }

    /**
     * 获取权限
     */
    private void getPermission() {

        UPermissionsTool.
                getIntance(this).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    /**
     * 跳转到下一个界面
     */
    private void intentActivity() {
        switch (BaseApplication.getInstance().userModel.getLoginState()) {
            case 1://登录在线
                Uintent.intentDIYLeftToRight(this, MainActivity.class);
                break;
            case 2://登录离线
                Uintent.intentDIYLeftToRight(this, MainOffActivity.class);
                break;
            default:
                Uintent.intentDIYLeftToRight(this, LoginActivity.class);

        }
        finish();
    }

}

