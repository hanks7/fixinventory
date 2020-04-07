package com.easyway.fixinventory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyway.fixinventory.R;
import com.hanks.frame.base.SingleElectionAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/17.
 * 注册证
 */
public class SimpleLsvAdapter extends SingleElectionAdapter<String> {


    public SimpleLsvAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lsv_simple_adapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.fillValue(position, checkItemPosition, list.get(position), mContext);
        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.item_lsv_adapter_id_text)
        TextView mText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }


        public void fillValue(int position, int checkItemPosition, String content, Context context) {
            mText.setText(content);
            if (checkItemPosition == position) {
                mText.setTextColor(context.getResources().getColor(R.color.input_color));
//                mText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_aaab), null);
            } else {
                mText.setTextColor(context.getResources().getColor(R.color.gray));
//                mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

}
