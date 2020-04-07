package com.easyway.fixinventory.dao;

/**
 * @author 侯建军 47466436@qq.com
 * @class com.easyway.mismclient.dao.DBContants
 * @time 2018/7/23 16:42
 * @description 请填写描述
 */
public class DBContants {

    //用户登录表
    public static final String TB_Employee = "Create table if not exists TB_Employee ("
            + " HRCode  char(10) primary key,"
            + " DeptCode char(6),"
            + "WorkDeptCode  char(6),"
            + "EmployeeName  nvarchar(50),"
            + "DutyName  nvarchar(20),"
            + "Password  varchar(50) ,"
            + "HosptialID  integer ,"
            + "HosptialName nvarchar(50),"
            + "DepartmentName nvarchar(1000),"
            + "PinYin nvarchar(10),"
            + "WuBi  nvarchar(10),"
            + "Status char(10) ,"
            + "IsVoid  char(1) ,"
            + "HisCode  varchar(20))";
    //用户科室
    public static final String TB_UserDepartment = "Create table if not exists TB_UserDepartment ("
            + "DeptCode               char(6)  ,"
            + "DepartmentName       nvarchar(1000) )";
    //科室
    public static final String TB_Department = "Create table if not exists TB_Department ("
            + "DeptCode             char(6)  ,"
            + "DepartmentName       nvarchar(50) ,"
            + "Property             char(2)       ,"
            + "MedInsureDeptCode    varchar(10)   ,"
            + "MedInsureDeptName    nvarchar(50)  ,"
            + "HisDepCode           nvarchar(50)  ,"
            + "HisDepType           nvarchar(50)  ,"
            + "PinYin               varchar(10)   ,"
            + "WuBi                 varchar(10)   ,"
            + "OrderNum             int           ,"
            + "AliasName            nvarchar(10)  ,"
            + "AliasPinYin          nvarchar(10)  ,"
            + "AliasWuBi            char(10)      ,"
            + "Remark               nvarchar(50)  ,"
            + "IsStorehouse         char(1)       ,"
            + "IsVoid               char(1)       ,"
            + "IsWarehouse          char(1)       ,"
            + "IsFollowSurgical     char(1)       ,"
            + "ParentDeptCode       char(6)       ,"
            + "IsInOutCoincident    char(1)       ,"
            + "IsAudit              char(1)       ,"
            + "ReportCode           varchar(20)   ,"
            + "WarningTime          nvarchar(20)  ,"
            + "DepartmentType       int           )";
    //标签表
    public static final String TB_AssetsCard = "CREATE TABLE TB_AssetsCard (" +
            "AssetID              char(12)  ," +
            "ProductInfoID        char(12)      ," +
            "DeptCode             char(6)       ," +
            "ValueAddedWay        int           ," +
            "RequisitionID        int           ," +
            "CardNo               nvarchar(50)   ," +
            "Code                 nvarchar(50)  ," +
            "Name                 nvarchar(100)  ," +
            "SpecName             nvarchar(100)  ," +
            "ModelName            nvarchar(100)   ," +
            "TotalMoney           decimal(18,3)   ," +
            "DepreciationBy       int           ," +
            "Depreciated          decimal(18,3)   ," +
            "Remaining            decimal(18,3)   ," +
            "WhileSharingMonth    int           ," +
            "AddDate              nvarchar(100)      ," +
            "DepreciatedPM        decimal(18,3)   ," +
            "CumulativeDepreciate decimal(18,3)   ," +
            "NetResidualValue     decimal(18,2)   ," +
            "SupplierID           int           ," +
            "SupplierName         nvarchar(100)   ," +
            "ContactMan           nvarchar(100)   ," +
            "ContactPhone         nvarchar(100)  ," +
            "ContactAddr          nvarchar(500)   ," +
            "RepareCompanyID      int           ," +
            "RepareCompanyName    nvarchar(100)   ," +
            "RepareContactMan     nvarchar(100)  ," +
            "RepareContactPhone   nvarchar(100)  ," +
            "RepareContactAddr    nvarchar(500)  ," +
            "UseStatus            int           ," +
            "StartUseDate         text    ," +
            "PlaceAddress         nvarchar(500) ," +
            "IsUseBeforeAdd       int           ," +
            "CreateTime           text      ," +
            "UpdateTime           text      ," +
            "ApplicationID        int           ," +
            "AssetsAcceptanceId   char(12)      ," +
            "AttachmentMoney      decimal(18,2)   ," +
            "BiddingItem          nvarchar(100)  ," +
            "CancelDate           text      ," +
            "ClassCode            nvarchar(100)  ," +
            "ClearCost            decimal(18,2)   ," +
            "ContractCode         nvarchar(100)  ," +
            "CustodyDepartments   char(6)       ," +
            "CustodyPeople        char(10)      ," +
            "FileNumber           nvarchar(100)  ," +
            "FundSource           int           ," +
            "Inspection           int           ," +
            "InspectionNum        nvarchar(50)   ," +
            "InstallPlace         nvarchar(100)  ," +
            "IsForceInspection    bit           ," +
            "IsInspection         bit           ," +
            "IsMeasurement        bit           ," +
            "IsMedicalEquipment   bit           ," +
            "MaintenanceAddress3  nvarchar(100)   ," +
            "MaintenanceID        int           ," +
            "MaintenanceMan       char(10)      ," +
            "MaintenanceMan3      nvarchar(200)   ," +
            "MaintenanceName      nvarchar(100)   ," +
            "MaintenancePhone3    nvarchar(100)   ," +
            "MaintenanceRequisitionStatus int    ," +
            "MajorMaintenanceCycle int           ," +
            "ManageDepartment     int           ," +
            "ManageGrade          nvarchar(50)  ," +
            "MeanMaintenanceCycle int           ," +
            "MeasurementCategory  nvarchar(50)  ," +
            "Original             nvarchar(100)  ," +
            "PurchaseDepartment   char(6)       ," +
            "RegistrationNumber   nvarchar(200)   ," +
            "RepairDepartment     char(6)       ," +
            "RequisitionSource    int           ," +
            "ScrappedDate         text      ," +
            "SerialNumber         nvarchar(100)  ," +
            "SharingType          nvarchar(10)   ," +
            "SmallMaintenanceCycle int           ," +
            "UseWay               nvarchar(50)  ," +
            "Barcode              nvarchar(100)   ," +
            "Unit                 nvarchar(100)  ," +
            "Price                decimal(18,2)   ," +
            "MaintenanceDate      text      ," +
            "Memo                 nvarchar(100)   ," +
            "Handler              nvarchar(100)   ," +
            "RepairMan            char(10)      ," +
            "MaintenanceDepartment char(6)       ," +
            "AccountingCategories int           ," +
            "StartBrokeDate       int           ," +
            "ManufacturerID       int           ," +
            "ManufacturerName     nvarchar(100)   ," +
            "PinYin               varchar(10)   ," +
            "WuBi                 varchar(10)   ," +
            "StoreID              char(6)       ," +
            "StoreName            nvarchar(400)   ," +
            "DepartmentName     nvarchar(100)   ," +
            "TakeInventoryCode  nvarchar(20)," +
            "InventoryUseStatus            int   ," +
            "InventorySite            nvarchar(400)  ," +
            "InventoryResult              int   ," +
            "UpLoad                       int   ," +
            "WarehouseVoucherCode varchar(50)    ) ";

    //存储用户名密码
    public static final String User_Table = "Create table if not exists User_Table ("
            + "Account    text,"
            + "Password   text,"
            + "HosptialID  text,"
            + "HosptialName text,"
            + "EmployeeName text,"
            + "DepartmentName text)";


}
