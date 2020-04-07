package com.easyway.fixinventory.model;

import com.easyway.fixinventory.base.BaseApplication;

import java.io.Serializable;

public class InventoryInfoBean implements Serializable {


    /**
     * AssetID : 201808140003
     * CardNo : GZKP201808140003
     * Name : 贮槽架
     * DeviceCode : 3222405
     * SpecName : HPDX-2310/2
     * DepreciationBy : 5.0
     * TotalMoney : 750.0
     * StartUseDate : 2018-08-14 00:00:00
     * CustodyPeople : null
     * CustodyPeopleName : null
     * DeptCode : 100005
     * DepartmentName : 口腔科
     * IsAddress : null
     * PlaceAddress : null
     * UseStatus : 5
     * LastInventoryDate : 2018-08-14 13:20:00
     */

    private String AssetID;
    private String TakeInventoryCode;
    private String CardNo;
    private String Name;
    private String DeviceCode;
    private String SpecName;
    private double DepreciationBy;
    private double TotalMoney;
    private String StartUseDate;
    private String CustodyPeople;
    private String CustodyPeopleName;
    private String DeptCode;
    private String DepartmentName;
    private int IsAddress;
    private String PlaceAddress;
    private int UseStatus;
    private String LastInventoryDate;
    private String ManageDepartment;
    private String InventorySite;
    /**
     * 1:已盘点 其他:未盘点
     */
    private int InventoryResult;

    public int getInventoryUseStatus() {
        return InventoryUseStatus;
    }

    public void setInventoryUseStatus(int inventoryUseStatus) {
        InventoryUseStatus = inventoryUseStatus;
    }

    private int InventoryUseStatus;

    public String getCompany() {
        return BaseApplication.getInstance().getUserModel().getHosptialName();
    }

    public int getIsAddress() {
        return IsAddress;
    }

    public void setIsAddress(int isAddress) {
        IsAddress = isAddress;
    }

    public int getInventoryResult() {
        return InventoryResult;
    }

    public void setInventoryResult(int inventoryResult) {
        InventoryResult = inventoryResult;
    }

    public String getTakeInventoryCode() {
        return TakeInventoryCode;
    }

    public void setTakeInventoryCode(String takeInventoryCode) {
        TakeInventoryCode = takeInventoryCode;
    }

    public String getAssetID() {
        return AssetID;
    }

    public void setAssetID(String AssetID) {
        this.AssetID = AssetID;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDeviceCode() {
        return DeviceCode;
    }

    public void setDeviceCode(String DeviceCode) {
        this.DeviceCode = DeviceCode;
    }

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String SpecName) {
        this.SpecName = SpecName;
    }

    public double getDepreciationBy() {
        return DepreciationBy;
    }

    public void setDepreciationBy(double DepreciationBy) {
        this.DepreciationBy = DepreciationBy;
    }

    public double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(double TotalMoney) {
        this.TotalMoney = TotalMoney;
    }

    public String getStartUseDate() {
        return StartUseDate;
    }

    public void setStartUseDate(String StartUseDate) {
        this.StartUseDate = StartUseDate;
    }

    public String getCustodyPeople() {
        return CustodyPeople;
    }

    public void setCustodyPeople(String CustodyPeople) {
        this.CustodyPeople = CustodyPeople;
    }

    public String getCustodyPeopleName() {
        return CustodyPeopleName;
    }

    public void setCustodyPeopleName(String CustodyPeopleName) {
        this.CustodyPeopleName = CustodyPeopleName;
    }

    public String getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(String DeptCode) {
        this.DeptCode = DeptCode;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }


    public String getPlaceAddress() {
        return PlaceAddress;
    }

    public void setPlaceAddress(String PlaceAddress) {
        this.PlaceAddress = PlaceAddress;
    }

    public int getUseStatus() {
        return UseStatus;
    }

    public void setUseStatus(int UseStatus) {
        this.UseStatus = UseStatus;
    }

    public String getLastInventoryDate() {
        return LastInventoryDate;
    }

    public void setLastInventoryDate(String LastInventoryDate) {
        this.LastInventoryDate = LastInventoryDate;
    }

    public String getManageDepartment() {
        return ManageDepartment;
    }

    public void setManageDepartment(String manageDepartment) {
        ManageDepartment = manageDepartment;
    }

    public String getInventorySite() {
        return InventorySite;
    }

    public void setInventorySite(String inventorySite) {
        InventorySite = inventorySite;
    }

    @Override
    public String toString() {
        return "InventoryInfoBean{" +
                "AssetID='" + AssetID + '\'' +
                ", TakeInventoryCode='" + TakeInventoryCode + '\'' +
                ", CardNo='" + CardNo + '\'' +
                ", Name='" + Name + '\'' +
                ", DeviceCode='" + DeviceCode + '\'' +
                ", SpecName='" + SpecName + '\'' +
                ", DepreciationBy=" + DepreciationBy +
                ", TotalMoney=" + TotalMoney +
                ", StartUseDate='" + StartUseDate + '\'' +
                ", CustodyPeople='" + CustodyPeople + '\'' +
                ", CustodyPeopleName='" + CustodyPeopleName + '\'' +
                ", DeptCode='" + DeptCode + '\'' +
                ", DepartmentName='" + DepartmentName + '\'' +
                ", IsAddress=" + IsAddress +
                ", PlaceAddress='" + PlaceAddress + '\'' +
                ", UseStatus=" + UseStatus +
                ", LastInventoryDate='" + LastInventoryDate + '\'' +
                ", ManageDepartment='" + ManageDepartment + '\'' +
                ", InventorySite='" + InventorySite + '\'' +
                ", InventoryResult=" + InventoryResult +
                ", InventoryUseStatus=" + InventoryUseStatus +
                '}';
    }
}
