package com.hanks.frame.model;

import com.hanks.frame.base.BaseModel;

import java.io.Serializable;

/**
 * Created by CC on 2016/12/11.
 */

public class BasePageModel<Data extends Serializable> extends BaseModel {

    private PageModel<Data> page;


    public PageModel<Data> getPage() {
        return page;
    }

    public void setPage(PageModel<Data> page) {
        this.page = page;
    }

}
