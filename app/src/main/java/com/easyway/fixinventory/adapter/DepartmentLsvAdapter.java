package com.easyway.fixinventory.adapter;

import android.content.Context;

import com.easyway.fixinventory.model.DepartmentBean;

import java.util.List;

/**
 * Created by admin on 2018/4/17.
 * 注册证
 */
public class DepartmentLsvAdapter extends BaseSingleElectionAdapter<DepartmentBean> {


    public DepartmentLsvAdapter(Context mContext, List<DepartmentBean> list) {
        super(mContext, list);
    }

    @Override
    String getString(int position) {
        return list.get(position).getDepartmentName();
    }


}
