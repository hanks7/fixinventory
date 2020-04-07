package com.easyway.fixinventory;

import android.support.annotation.NonNull;

import com.easyway.fixinventory.base.BaseApplication;
import com.easyway.fixinventory.ui.inventory.AssetsListActivity;
import com.easyway.fixinventory.ui.inventory.FixedOffInventoryActivity;
import com.hanks.frame.utils.Uintent;

public class MainOffActivity extends MainActivity {

    @NonNull
    @Override
    String getTitleName() {
        return "离线模式  " + BaseApplication.getInstance().getUserModel().getDeptCodeName()
                + " - " + BaseApplication.getInstance().getUserModel().getEmployeeName();
    }

    @Override
    void intentInventoryActivity() {
        Uintent.intentDIY(this, FixedOffInventoryActivity.class);
    }

    //跳转到资产列表页
    @Override
    void intentAssetsListActivity() {
        Uintent.intentDIY(this, AssetsListActivity.class);
    }
}
