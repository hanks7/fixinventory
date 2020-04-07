package com.easyway.fixinventory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.model.InventoryInfoBean;
import com.hanks.frame.base.SingleElectionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hanks7 on 2019年8月7日15:23:18.
 * 资产列表
 */
public class AssetsLsvAdapter extends SingleElectionAdapter<InventoryInfoBean> {


    public AssetsLsvAdapter(Context mContext, List<InventoryInfoBean> list) {
        super(mContext, list);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lsv_assets_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.fillValue(list.get(position), mContext);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_assets_tv_device_name)
        TextView vhTvDeviceName;
        @BindView(R.id.item_assets_tv_space_name)
        TextView vhTvSpaceName;
        @BindView(R.id.item_assets_tv_inventory_result)
        TextView vhTvInventoryResult;
        @BindView(R.id.item_assets_tv_inventory_state)
        TextView vhTvState;

        ArrayList<String> strList;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            strList = new ArrayList<>();
            strList.add("购置");
            strList.add("在用");
            strList.add("报废");
            strList.add("维修");
            strList.add("保养");
            strList.add("停用");
            strList.add("封存");
            strList.add("退货");
            strList.add("报损");
            strList.add("待报损");
            strList.add("待报废");
        }


        public void fillValue(InventoryInfoBean bean, Context context) {
            vhTvDeviceName.setText(new StringBuilder().append("设备名称 : ").append(bean.getName()).toString());
            vhTvSpaceName.setText(new StringBuilder().append("规格型号 : ").append(bean.getSpecName()).toString());
            if (bean.getUseStatus() - 1 < strList.size()&&bean.getInventoryUseStatus()!=0) {
                vhTvState.setText(strList.get(bean.getInventoryUseStatus() - 1));
            }else{
                vhTvState.setText("");
            }

            if (bean.getInventoryResult() == 1) {
                vhTvInventoryResult.setText("已盘点");
                vhTvInventoryResult.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else {
                vhTvInventoryResult.setText("未盘点");
                vhTvInventoryResult.setTextColor(context.getResources().getColor(R.color.frame_textColor));
            }
        }
    }


}
