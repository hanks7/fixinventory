package com.easyway.fixinventory.ui.inventory;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.adapter.AssetsLsvAdapter;
import com.easyway.fixinventory.adapter.DepartmentLsvAdapter;
import com.easyway.fixinventory.adapter.SimpleLsvAdapter;
import com.easyway.fixinventory.base.BaseActivity;
import com.easyway.fixinventory.dao.DbHelper;
import com.easyway.fixinventory.model.DepartmentBean;
import com.easyway.fixinventory.utils.http.OnResponseListener3;
import com.easyway.fixinventory.view.PopMenu;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Uintent;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.view.TopBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetsListActivity extends BaseActivity {

    @BindView(R.id.ac_assets_lst_topbar)
    TopBar mTopbar;

    @BindView(R.id.ac_assets_lst_tv_department_code)
    TextView mDepartmentTvCode;
    @BindView(R.id.ac_assets_lst_department_iv_close)
    ImageView mDeparmentIvClose;
    @BindView(R.id.ac_assets_lst_rl_select_keshi2)
    RelativeLayout mDepartmentRl;


    @BindView(R.id.ac_assets_lst_array_num_tv_content)
    TextView mTvTakeInventoryCode;
    @BindView(R.id.ac_assets_lst_department_tv_count)
    TextView mTvCount;
    @BindView(R.id.ac_assets_lst_array_num_iv_close)
    ImageView mArrayNumIvClose;
    @BindView(R.id.ac_assets_lst_array_num_rl_select)
    RelativeLayout mArrayNumRlSelect;

    @BindView(R.id.ac_assets_lst_lsv)
    ListView mLsv;

    DepartmentBean deptBean;
    AssetsLsvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_list);
        ButterKnife.bind(this);
        mTopbar.setTvRight("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopDepartment();
            }
        });
        deptBean = new DepartmentBean();
        initLsv();
        initPopDepartment();
    }

    /**
     * 初始化资产列表
     */
    private void initLsv() {
        adapter = new AssetsLsvAdapter(this, null);
        mLsv.setAdapter(adapter);
        mLsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("AssetsCard", adapter.getList().get(position));
                Uintent.intentDIY(mActivity, FixedOffInventoryActivity.class, bundle);
            }
        });
    }

    /**
     * 初始化科室popWindow
     */
    private void initPopDepartment() {
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                initData();
                dismissDialog();
            }
        }, 1000);


    }

    private void initData() {
        final DepartmentLsvAdapter departmentAdapter = new DepartmentLsvAdapter(mActivity, null);
        final PopMenu popDepartment = new PopMenu(this, mDeparmentIvClose, mDepartmentRl, departmentAdapter);
        takeInventoryCodeAdapter = new SimpleLsvAdapter(mActivity, null);
        popInventoryArrayNum = new PopMenu(this, mArrayNumIvClose, mArrayNumRlSelect, takeInventoryCodeAdapter);

        DbHelper.getbean(mActivity).queryDepartment(new OnResponseListener3<List<DepartmentBean>>() {
            @Override
            public void onSuccess(final List<DepartmentBean> list) {
                if (!list.isEmpty() && list.size() == 1) {
                    departmentAdapter.setCheckItem(0);

                    clickedDepartment(list.get(0));
                }
                popDepartment.setOnItemClickListener(list, new PopMenu.ItemClickListener() {
                    @Override
                    public void itemClickListener(int position) {
                        //当选择科室之后 单据号复位
                        takeInventoryCodeAdapter.setCheckItemDefault();
                        mTvTakeInventoryCode.setText("");
                        mTvCount.setText("");

                        clickedDepartment(list.get(position));
                    }
                });
            }
        });
    }

    SimpleLsvAdapter takeInventoryCodeAdapter;
    PopMenu popInventoryArrayNum;

    /**
     * 点击 单据号 item之后处理的业务
     *
     * @param message
     */
    private void clickedTakeInventoryCode(String message) {
        mTvTakeInventoryCode.setText(message);
        adapter.updateList(null);
        mLsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                seLsvUpdate();
            }
        }, 102);

    }

    /**
     * listview 更新 adapter 并显示总条数
     */
    private void seLsvUpdate() {
        String strDeptCode = deptBean.getDeptCode();
        String strTakeInventoryCode = mTvTakeInventoryCode.getText().toString().trim();

        Ulog.i("strDeptCode", strDeptCode);
        Ulog.i("strTakeInventoryCode", strTakeInventoryCode);

        if (TextUtils.isEmpty(strDeptCode)) {
            UToast.showText("请选择科室");
            return;
        }
        if (TextUtils.isEmpty(strTakeInventoryCode)) {
            UToast.showText("请选择单据号");
            return;
        }

        adapter.updateList(DbHelper.getbean(mActivity).queryInventoryInfoList(strDeptCode, strTakeInventoryCode));
        mTvCount.setText("总条数  : " + adapter.getList().size() +
                "   已盘点  : " + DbHelper.getbean(mActivity).queryNoInventoryNum(strDeptCode, strTakeInventoryCode) +
                "   未盘点 : " + (adapter.getList().size() - DbHelper.getbean(mActivity).queryNoInventoryNum(strDeptCode, strTakeInventoryCode)));
    }

    /**
     * 点击 科室 item之后处理的业务
     *
     * @param bean
     */
    private void clickedDepartment(final DepartmentBean bean) {
        mDepartmentTvCode.setText(bean.getDepartmentName());
        deptBean = bean;

        seLsvUpdate();


        DbHelper.getbean(mActivity).getbean(mActivity).queryTakeInventoryCode(deptBean.getDeptCode(), new OnResponseListener3<List<String>>() {
            @Override
            public void onSuccess(final List<String> list) {
                if (!list.isEmpty() && list.size() == 1) {
                    takeInventoryCodeAdapter.setCheckItem(0);

                    clickedTakeInventoryCode(list.get(0));
                }
                popInventoryArrayNum.setOnItemClickListener(list, new PopMenu.ItemClickListener() {
                    @Override
                    public void itemClickListener(int position) {

                        clickedTakeInventoryCode(list.get(position));
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        seLsvUpdate();
    }
}
