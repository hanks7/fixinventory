package com.easyway.fixinventory.model;

/**
 * @author 侯建军 QQ:474664736
 * @time 2019/9/23 16:13
 * @class describe
 */
public class TakeInventoryCodeBean {

    String TakeInventoryCode;
    String DepartmentName;

    public TakeInventoryCodeBean(String takeInventoryCode, String departmentName) {
        TakeInventoryCode = takeInventoryCode;
        DepartmentName = departmentName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getTakeInventoryCode() {
        return TakeInventoryCode;
    }

    public void setTakeInventoryCode(String takeInventoryCode) {
        TakeInventoryCode = takeInventoryCode;
    }

}
