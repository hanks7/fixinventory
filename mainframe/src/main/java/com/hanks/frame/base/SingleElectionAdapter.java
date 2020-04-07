package com.hanks.frame.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选的apapter
 */
public class SingleElectionAdapter<T> extends BaseMyAdapter<T> {

    public SingleElectionAdapter(Context mContext, List<T> list) {
        super(mContext, list);
    }

    public SingleElectionAdapter(Context context) {
        super(context);

    }

    @Override
    public void updateList(List<T> listBean) {
        if (listBean != null) {
            this.list = listBean;
        } else {
            this.list = new ArrayList();
        }
        notifyDataSetChanged();
    }

    /**
     * 初始默认值
     */
    public int checkItemPosition = -1;

    /**
     * 设置被选中的index
     *
     * @param position
     */
    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 复位
     */
    public void setCheckItemDefault() {
        checkItemPosition = -1;
        notifyDataSetChanged();
    }


}
