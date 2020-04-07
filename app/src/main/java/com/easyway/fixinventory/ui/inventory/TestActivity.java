package com.easyway.fixinventory.ui.inventory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easyway.fixinventory.R;
import com.easyway.fixinventory.base.BaseActivity;
import com.hanks.frame.base.SingleElectionAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @BindView(R.id.ac_test_lsv)
    ListView mLsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        InventoryStateAdapter adapter = new InventoryStateAdapter(this);
        mLsv.setAdapter(adapter);
    }


    class InventoryStateAdapter extends SingleElectionAdapter<String> {

        public InventoryStateAdapter(Context context) {

            super(context);
            this.list = new ArrayList<>();

            list.add("购置");
            list.add("在用");
            list.add("报废");
            list.add("维修");
            list.add("保养");
            list.add("停用");
            list.add("封存");
            list.add("退货");
            list.add("报损");
            list.add("待报损");
            list.add("待报废");

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_radio_button, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder.fillValue(position, checkItemPosition, (list.get(position)), mContext);
            return convertView;
        }


    }

    static class ViewHolder {
        @BindView(R.id.item_gv_tv)
        TextView tv;
        @BindView(R.id.item_gv_iv)
        ImageView iv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void fillValue(int position, int checkItemPosition, String content, Context context) {
            tv.setText(content);
            if (checkItemPosition == position) {
                tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                iv.setBackground(context.getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
            } else {
                tv.setTextColor(context.getResources().getColor(R.color.colorTextPrimary));
                iv.setBackground(context.getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
            }
        }
    }
}
