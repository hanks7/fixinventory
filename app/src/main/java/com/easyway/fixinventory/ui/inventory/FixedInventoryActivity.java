package com.easyway.fixinventory.ui.inventory;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.adapter.InventoryStateAdapter;
import com.easyway.fixinventory.base.BaseActivity;
import com.easyway.fixinventory.base.BaseApplication;
import com.easyway.fixinventory.model.AssetsTakeInventoryForPDABean;
import com.easyway.fixinventory.model.InventoryInfoBean;
import com.easyway.fixinventory.utils.http.HttpAdapter;
import com.easyway.fixinventory.utils.http.OnResponseListener2;
import com.easyway.fixinventory.view.BarCodeEditView;
import com.easyway.fixinventory.view.MyTextView;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.view.GridViewForScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixedInventoryActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.ac_inventory_gv_state)
    GridViewForScrollView mGvState;
    @BindView(R.id.ac_inventory_edit_main)
    BarCodeEditView mEditMain;


    InventoryStateAdapter adapter;
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
    InventoryInfoBean mSaveBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_inventory);
        ButterKnife.bind(this);
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
    }

    /**
     * 初始化主次条码控件
     */
    private void initEditMain() {
        mEditMain.setOnEditorBarCodeTypeListener(new BarCodeEditView.AsdfdsListener() {
            @Override
            public void barCodeType(int barCodeType) {

                getNetData();
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
    private void getNetData() {
        HttpAdapter.getService().AssetsTakeInventoryInfoForPDA(
                mEditMain.strBarCodeSource

        )
                .enqueue(new OnResponseListener2<InventoryInfoBean>(mActivity) {
                    @Override
                    public void onSuccess(InventoryInfoBean bean) {
                        mEditMain.selectAll();
                        mNetBean = bean;
                        initView();
                    }

                    @Override
                    public void onError(int code, String strToast) {
                        UToast.showSnackBar(mEditMain, strToast, R.color.red, R.color.white);
                        mEditMain.setSelectAll();
                    }


                });
    }

    private void initView() {
        if (mNetBean == null) {
            UToast.showText("成员变量为空,请联系管理员");
            return;
        }
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
        adapter.setCheckItem(mNetBean.getUseStatus() - 1);
    }

    private void setViewEmpty() {
        mEditMain.setText("");
        mEditMain.setBarCodeEmpty();
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
        adapter.setCheckItem(-1);
        IsAddress = 1;
        mRbIsOriginPlaceTrue.setChecked(true);
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
        HttpAdapter.getService().AssetsTakeInventoryForPDA(
                mNetBean.getAssetID(),
                adapter.checkItemPosition + 1,
                IsAddress, mEdtAddress.getText().toString().trim(),
                BaseApplication.getInstance().getUserModel().getHRCode()
        )
                .enqueue(new OnResponseListener2<AssetsTakeInventoryForPDABean>(mActivity) {
                    @Override
                    public void onSuccess(AssetsTakeInventoryForPDABean bean) {
                        UToast.showSnackBar(mEditMain, "提交盘点信息成功", R.color.green, R.color.white);
                        setViewEmpty();
                    }

                    @Override
                    public void onError(int code, String strToast) {
                        UToast.showSnackBar(mEditMain, strToast, R.color.red, R.color.white);
                        mEditMain.setSelectAll();
                    }


                });
    }

    /**
     * 是否在原地址 1:是  0:否
     * 默认为0;
     */
    int IsAddress = 1;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.ac_inventory_rb_is_origin_place_true:
                IsAddress = 1;
                mEdtAddress.setEnabled(false);
                mEdtAddress.setFocusable(false);
                mEdtAddress.setFocusableInTouchMode(false);
                mEdtAddress.setCursorVisible(false);
                if (mNetBean != null) {
                    mEdtAddress.setText(mNetBean.getPlaceAddress());
                }
                break;
            case R.id.ac_inventory_rb_is_origin_place_false:
                IsAddress = 0;
                mEdtAddress.setSelection(mEdtAddress.getText().length());//将光标移至文字末尾
                mEdtAddress.setEnabled(true);
                mEdtAddress.setFocusable(true);
                mEdtAddress.setFocusableInTouchMode(true);
                mEdtAddress.setCursorVisible(true);
                mEdtAddress.requestFocus();

                break;
        }
    }
}
