package com.easyway.fixinventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.easyway.fixinventory.base.BaseApplication;
import com.easyway.fixinventory.base.BaseConstants;
import com.easyway.fixinventory.dao.DBDeleteListener;
import com.easyway.fixinventory.dao.DbHelper;
import com.easyway.fixinventory.ui.inventory.FixedInventoryActivity;
import com.easyway.fixinventory.ui.inventory.TestActivity;
import com.easyway.fixinventory.ui.login.LoginActivity;
import com.hanks.frame.base.HjjActivity;
import com.hanks.frame.inf.DialogInf;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Uintent;
import com.hanks.frame.utils.UmengUtils;
import com.hanks.frame.view.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends HjjActivity {
    @BindView(R.id.menu_mine_hrcode)
    TextView mTvHrcode;
    @BindView(R.id.topbar)
    TopBar topbar;
    @BindView(R.id.fg_mine_employee_name)
    TextView mTvEmployeeName;
    @BindView(R.id.fg_mine_duty_name)
    TextView mTvDutyName;
    @BindView(R.id.fg_mine_hospital_id)
    TextView mTvHosID;

    @BindView(R.id.menu_mine_ll_detail)
    View llDetail;
    @BindView(R.id.main_tv_empty_note)
    View tvEmptyNote;


    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;//侧滑布局
    DbHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        mDBHelper = new DbHelper(this);

    }

    void initView() {
        topbar.setTitle(getTitleName());
        topbar.setIvBackButton(R.mipmap.menu, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judgeDrawerLayoutIsOpen();
            }
        });
        mTvHrcode.setText("Hrcode : " + BaseApplication.getInstance().userModel.getHRCode());
        mTvEmployeeName.setText("EmployeeName : " + BaseApplication.getInstance().userModel.getEmployeeName());
        mTvDutyName.setText("DeptCodeName : " + BaseApplication.getInstance().userModel.getDeptCodeName());
        mTvHosID.setText("HosID : " + BaseApplication.getInstance().userModel.getHosptialID());
    }

    @NonNull
    String getTitleName() {
        return BaseApplication.getInstance().getUserModel().getDeptCodeName()
                + " - " + BaseApplication.getInstance().getUserModel().getEmployeeName();
    }

    @OnClick({
            R.id.menu_mine_delete_db,
            R.id.menu_mine_quit,
            R.id.menu_mine_ll_detail,
            R.id.ac_main_item_one,
            R.id.ac_main_item_two
    })
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.menu_mine_delete_db:
                UToast.showDialog(this, "是否清除缓存,清除缓存后将影响部分功能", "清除", false, new DialogInf() {
                    @Override
                    public void commit() {
                        deleteTBAssetsCard(view);
                    }
                });

                break;
            case R.id.menu_mine_quit:
                showQuitDialog();//退出登录
                break;
            case R.id.ac_main_item_one:
                intentInventoryActivity();
                break;
            case R.id.ac_main_item_two:
                intentAssetsListActivity();
                break;

            case R.id.menu_mine_ll_detail:
                if (!BaseConstants.IS_RELEASE) {
                    Uintent.intentDIY(this, TestActivity.class);
                }
                break;
        }
    }

    /**
     * 清除数据库中卡片表
     * @param view
     */
    private void deleteTBAssetsCard(final View view) {
        mDBHelper.deleteTBAssetsCard(new DBDeleteListener() {
            @Override
            public void onStart() {
                showDialog();
            }

            @Override
            public void onResponse(int code) {
                if (code == 200) {
                    UToast.showSnackBar(view, "删除缓存成功", R.color.green, R.color.white);
                }else{
                    UToast.showSnackBar(view, "删除缓存失败", R.color.red, R.color.white);
                }
                dismissDialog();
            }
        });
    }

    void intentAssetsListActivity() {

    }

    void intentInventoryActivity() {
        Uintent.intentDIY(this, FixedInventoryActivity.class);
    }

    /**
     * 退出登录dialog
     */
    public void showQuitDialog() {
        UToast.showDialog(this, "退出登录将清除个人信息,是否继续", getString(R.string.confirm), true, new DialogInf() {
            @Override
            public void commit() {
                quitLogin();
            }
        });
    }

    /**
     * 退出登录
     */
    private void quitLogin() {
        BaseApplication.getInstance().quitLogin();
        UmengUtils.onLogout();
        Uintent.intentDIY(this, LoginActivity.class);
        finish();
    }

    /**
     * 判断DrawerLayout是否 打开
     * 如果侧滑打开就关闭,关闭就打开
     */
    void judgeDrawerLayoutIsOpen() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {//如果侧滑打开就关闭,关闭就打开
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private long mkeyTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                UToast.showSnackBar(topbar, "再按一次退出" + getResources().getString(R.string.app_name), R.color.colorPrimary, R.color.white);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
