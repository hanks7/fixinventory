package com.easyway.fixinventory.model;

public class DepartmentBean {

    private String DeptCode;
    private String DepartmentName;
    private String countNum;

    public DepartmentBean() {
    }

    public DepartmentBean(String deptCode, String departmentName, String countNum) {
        DeptCode = deptCode;
        DepartmentName = departmentName;
        this.countNum = countNum;
    }

    public String getCountNum() {
        return countNum;
    }

    public void setCountNum(String countNum) {
        this.countNum = countNum;
    }



    public String getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(String deptCode) {
        DeptCode = deptCode;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }
}
