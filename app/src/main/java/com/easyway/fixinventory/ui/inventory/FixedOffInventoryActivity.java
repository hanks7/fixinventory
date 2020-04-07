package com.easyway.fixinventory.ui.inventory;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.adapter.InventoryStateAdapter;
import com.easyway.fixinventory.base.BaseActivity;
import com.easyway.fixinventory.dao.DbHelper;
import com.easyway.fixinventory.model.InventoryInfoBean;
import com.easyway.fixinventory.utils.http.OnResponseListener3;
import com.easyway.fixinventory.utils.http.OnResponseListener4;
import com.easyway.fixinventory.view.BarCodeEditView;
import com.easyway.fixinventory.view.MyTextView;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.view.GridViewForScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Package: com.easyway.fixinventory.ui.inventory.FixedOffInventoryActivity
 * @Author: 侯建军
 * @CreateDate: 2019/9/16 16:21
 * @Description: 固定资产离线盘点页面
 */
public class FixedOffInventoryActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.ac_inventory_gv_state)
    GridViewForScrollView mGvState;
    @BindView(R.id.ac_inventory_edit_main)
    BarCodeEditView mEditMain;


    @BindView(R.id.ac_inventory_edit_device_name)
    MyTextView mEditDeviceName;
    @BindView(R.id.ac_inventory_edt_device_num)
    MyTextView mEdtDeviceNum;
    @BindView(R.id.ac_inventory_edt_device_model)
    MyTextView mEdtDeviceModel;
    @BindView(R.id.ac_inventory_edt_depreciation_years)
    MyTextView mEdtDepreciationYears;
    @BindView(R.id.ac_inventory_edt_original_value)
    MyTextView mEdtOriginalValue;
    @BindView(R.id.ac_inventory_edt_use_date)
    MyTextView mEdtUseDate;
    @BindView(R.id.ac_inventory_edt_depositary)
    MyTextView mEdtDepositary;
    @BindView(R.id.ac_inventory_edt_use_department)
    MyTextView mEdtUseDepartment;
    @BindView(R.id.ac_inventory_edt_admin_department)
    MyTextView mEdtAdminDepartment;
    @BindView(R.id.ac_inventory_edt_company)
    MyTextView mEdtCompany;
    @BindView(R.id.ac_inventory_rb_is_origin_place_true)
    RadioButton mRbIsOriginPlaceTrue;
    @BindView(R.id.ac_inventory_rb_is_origin_place_false)
    RadioButton mRbIsOriginPlaceFalse;
    @BindView(R.id.ac_inventory_rg_is_origin_place)
    RadioGroup mRgIsOriginPlace;
    @BindView(R.id.ac_inventory_edt_address)
    EditText mEdtAddress;
    @BindView(R.id.ac_inventory_scroll_view)
    ScrollView mSv;

    InventoryInfoBean mNetBean;
    InventoryStateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_off_inventory);
        ButterKnife.bind(this);
        setEditTextEnable(false);//设置地址栏不可编辑
        adapter = new InventoryStateAdapter(this);
        mGvState.setAdapter(adapter);

        mGvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckItem(position);
            }
        });
        initEditMain();
        mRgIsOriginPlace.setOnCheckedChangeListener(this);

        getIntentData();
    }


    /**
     * 得到上一页传来的数据
     */
    private void getIntentData() {
        if (getIntent().getExtras() == null) {
            return;
        }
        InventoryInfoBean bean = (InventoryInfoBean) getIntent().getExtras().getSerializable("AssetsCard");
        getNetData2(bean.getAssetID(),bean.getTakeInventoryCode());
    }

    private void getNetData2(String strAssetID, String strTakeInventoryCode) {
        DbHelper.getbean(mActivity).queryInventory2(strAssetID,strTakeInventoryCode, new OnResponseListener3<InventoryInfoBean>() {
            @Override
            public void onSuccess(InventoryInfoBean bean) {
                mNetBean = bean;
//                Ulog.i("mNetBean",mNetBean.toString());
                initView();
                mEditMain.selectAll();
                svGoToTop();
            }

            @Override
            public void onError(int code, String strToast) {

                mEditMain.setSelectAll();
                svGoToTop();
                setViewEmpty2();
                UToast.showDialog(mActivity, strToast, "确定", false, null);
            }
        });
    }

    /**
     * 初始化主次条码控件
     */
    private void initEditMain() {
        mEditMain.setOnEditorBarCodeTypeListener(new BarCodeEditView.AsdfdsListener() {
            @Override
            public void barCodeType(int barCodeType) {
                getNetData(mEditMain.barcodeObject.getBarcode());
                svGoToTop();
            }

            @Override
            public void doClose() {

                setViewEmpty();
            }


        });
    }


    /**
     * 卡片信息获取
     */
    private void getNetData(String strAssetID) {

        DbHelper.getbean(mActivity).queryInventory(strAssetID, new OnResponseListener3<InventoryInfoBean>() {
            @Override
            public void onSuccess(InventoryInfoBean bean) {
                mNetBean = bean;
                initView();
                mEditMain.selectAll();
                svGoToTop();
            }

            @Override
            public void onError(int code, String strToast) {

                mEditMain.setSelectAll();
                svGoToTop();
                setViewEmpty2();
                UToast.showDialog(mActivity, strToast, "确定", false, null);
            }
        });

    }

    private void initView() {
        if (mNetBean == null) {
            UToast.showText("成员变量为空,请联系管理员");
            return;
        }
        mEditMain.setText(mNetBean.getAssetID());
        mEditDeviceName.setText(mNetBean.getName());
        mEdtDeviceNum.setText(mNetBean.getDeviceCode());
        mEdtDeviceModel.setText(mNetBean.getSpecName());
        mEdtDepreciationYears.setText(mNetBean.getDepreciationBy() + "");
        mEdtOriginalValue.setText(mNetBean.getTotalMoney() + "");
        mEdtUseDate.setText(mNetBean.getStartUseDate());
        mEdtDepositary.setText(mNetBean.getCustodyPeople());
        mEdtUseDepartment.setText(mNetBean.getDepartmentName());
        mEdtAdminDepartment.setText(mNetBean.getDepartmentName());
        mEdtCompany.setText(mNetBean.getCompany());
        mEdtAddress.setText(mNetBean.getPlaceAddress());
        if (mNetBean.getInventoryResult() == 1) {
            UToast.showDialog(this, "已完成的盘点单", "确定", false, null);
            adapter.setCheckItem(mNetBean.getInventoryUseStatus() - 1);
            mEdtAddress.setText(mNetBean.getInventorySite());
        } else {
            adapter.setCheckItem(mNetBean.getUseStatus() - 1);
        }
    }

    private void setViewEmpty() {
        mEditMain.setText("");
        mEditMain.setBarCodeEmpty();
        setViewEmpty2();


    }

    /**
     * 设置界面为空,除了扫码框
     */
    private void setViewEmpty2() {

        mNetBean = null;
        mEditDeviceName.setText("");
        mEdtDeviceNum.setText("");
        mEdtDeviceModel.setText("");
        mEdtDepreciationYears.setText("");
        mEdtOriginalValue.setText("");
        mEdtUseDate.setText("");
        mEdtDepositary.setText("");
        mEdtUseDepartment.setText("");
        mEdtAdminDepartment.setText("");
        mEdtCompany.setText("");
        mEdtAddress.setText("");
        adapter.setCheckItemDefault();
        mRbIsOriginPlaceTrue.setChecked(true);

        svGoToTop();
    }

    /**
     * ScrollView置顶
     */
    private void svGoToTop() {
        mSv.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSv.fullScroll(View.FOCUS_UP);
            }
        }, 105);
    }

    /**
     * 点击确定
     *
     * @param view
     */
    public void netCommit(View view) {
        if (mNetBean == null) {
            UToast.showText("请先获取信息");
            return;
        }

        DbHelper.getbean(mActivity).confirmInventory(
                adapter.checkItemPosition + 1,
                mEdtAddress.getText().toString().trim(),
                mNetBean.getAssetID(),
                new OnResponseListener4() {
                    @Override
                    public void onSuccess() {
                        UToast.showSnackBar(mEditMain, "提交盘点信息成功", R.color.green, R.color.white);
                        setViewEmpty();
                    }

                    @Override
                    public void onError(int code, String strToast) {
                        UToast.showSnackBar(mEditMain, strToast, R.color.red, R.color.white);
                        mEditMain.setSelectAll();
                    }
                }
        );

    }

    /**
     * 是否在原地址的RadioGroup的监听
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.ac_inventory_rb_is_origin_place_true:
                setEditTextEnable(false);
                if (mNetBean != null) {
                    if (mNetBean.getInventoryUseStatus() != 0) {
                        mEdtAddress.setText(mNetBean.getInventorySite());
                    } else {
                        mEdtAddress.setText(mNetBean.getPlaceAddress());
                    }

                }
                break;

            case R.id.ac_inventory_rb_is_origin_place_false:
                setEditTextEnable(true);
                mEdtAddress.setSelection(mEdtAddress.getText().length());//将光标移至文字末尾
                mEdtAddress.setCursorVisible(true);
                mEdtAddress.requestFocus();
                break;
        }
    }

    /**
     * 设置EditText可编辑和不可编辑
     *
     * @param mode
     */
    private void setEditTextEnable(boolean mode) {
        mEdtAddress.setFocusable(mode);
        mEdtAddress.setFocusableInTouchMode(mode);
        mEdtAddress.setLongClickable(mode);
        mEdtAddress.setInputType(mode ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }

}
