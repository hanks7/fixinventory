package com.easyway.fixinventory.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.base.BaseActivity;
import com.easyway.fixinventory.base.BaseConstants;
import com.easyway.fixinventory.base.IPPortBean;
import com.easyway.fixinventory.dao.DBIpPortManager;
import com.easyway.fixinventory.utils.UTools;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.utils.Usp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;

/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/5/28
 * @description 设置IP端口号界面
 */
public class ChangeIPActivity extends BaseActivity {

    @BindView(R.id.change_ip_edt_ip_port)
    AutoCompleteTextView mEdtIpIpPort;
    @BindView(R.id.change_ip_edt_hos_id)
    AutoCompleteTextView mEdtIpHosId;

    private IPPortBean ipPortBean;
    private DBIpPortManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip);
        ButterKnife.bind(this);
        ipPortBean = UTools.getIPPortBean();


        mEdtIpIpPort.setText(ipPortBean.getIpPort().replace("http://", ""));
        mEdtIpHosId.setText(ipPortBean.getHosID() + "");

        db = new DBIpPortManager(this);
        mEdtIpIpPort.setAdapter(initAdapter(db.queryAll(db.tb_ip_prot)));
        mEdtIpHosId.setAdapter(initAdapter(db.queryAll(db.tb_hosid)));

    }

    /**
     * 新建适配器，适配器数据为搜索历史文件内容
     */
    private ArrayAdapter initAdapter(List<String> list) {
        ArrayAdapter  adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, list);
        return adapter;
    }




    /**
     * 保存的业务逻辑
     * http://172.16.1.81:7000
     *
     * @param view
     */
    public void clickSave(View view) {
        ipPortBean.setIpPort("http://" + mEdtIpIpPort.getText().toString().trim());
        ipPortBean.setHosID(mEdtIpHosId.getText().toString().trim());
        if (checkIpPort()) return;
        Usp.put(this,IPPortBean.class.getSimpleName(), ipPortBean);
        BaseConstants.FORMAL_URL = ipPortBean.getIpPort();
        db.insert(ipPortBean);
        finish();
    }

    /**
     * 检查ip端口号的格式
     * @return
     */
    private boolean checkIpPort() {
        try {
            HttpUrl baseURL = HttpUrl.parse(ipPortBean.getIpPort());
            Ulog.i("ipPortBean.getIpPort()", ipPortBean.getIpPort());
            //重建新的HttpUrl，需要重新设置的url部分
            baseURL.newBuilder()
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();
        } catch (Exception e) {
            UToast.showText("您输入的ip端口不符合规范(请不要输入中文\":\"符号");
            return true;
        }
        return false;
    }
}
