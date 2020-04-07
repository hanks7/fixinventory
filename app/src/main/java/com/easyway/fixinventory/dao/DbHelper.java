package com.easyway.fixinventory.dao;

/**
 * @author 侯建军 47466436@qq.com
 * @class com.easyway.mismclient.dao.DbHelper
 * @time 2018/7/23 15:54
 * @description 请填写描述
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.easyway.fixinventory.model.DepartmentBean;
import com.easyway.fixinventory.model.InventoryInfoBean;
import com.easyway.fixinventory.model.TakeInventoryCodeBean;
import com.easyway.fixinventory.model.UserBean;
import com.easyway.fixinventory.utils.http.OnResponseListener3;
import com.easyway.fixinventory.utils.http.OnResponseListener4;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Ugson;
import com.hanks.frame.utils.Ulog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.easyway.fixinventory.dao.DBContants.TB_AssetsCard;
import static com.easyway.fixinventory.dao.DBContants.TB_Department;
import static com.easyway.fixinventory.dao.DBContants.TB_Employee;
import static com.easyway.fixinventory.dao.DBContants.TB_UserDepartment;
import static com.easyway.fixinventory.dao.DBContants.User_Table;


/**
 * @Package: com.easyway.fixinventory.dao.DbHelper
 * @Author: 侯建军
 * @CreateDate: 2019/9/11 14:09
 * @Description: 请填写描述
 */
public class DbHelper extends SQLiteOpenHelper {

    Context context;

    public DbHelper(Context context) {
        super(context, getMySqliteName(context, "FAIDB.db"), null, 1);
        this.context = context;
    }

    public static DbHelper getbean(Context context) {
        return new DbHelper(context);
    }

    /**
     * 存放数据文件目录
     */
    public static final String DB_FILE_PATH = "/EAZYWAY/";

    /**
     * 生成数据名称 完整路径
     *
     * @param context
     * @param dbName
     * @return
     */
    public static String getMySqliteName(Context context, String dbName) {

        boolean isSdCardEnble = false;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {//SD卡是否存在
            isSdCardEnble = true;
        }
        String dbPath = "";
        if (isSdCardEnble) {
            dbPath = Environment.getExternalStorageDirectory().getPath() + DB_FILE_PATH;
        } else {//不存在sd卡，存入内存中
            dbPath = context.getFilesDir().getPath() + DB_FILE_PATH;
        }
        File file = new File(dbPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Ulog.i("getMySqliteName", dbPath + dbName);
        return dbPath + dbName;
    }

    /**
     * eg:DbHelper DbHelper = new DbHelper(getApplicationContext(),"FAIDB.db",null,1);//创建一个空的数据库数据库
     * com.easyway.mismclient/DbHelper.db
     *
     * @param context
     * @param name
     * @param factory 可以设置为空
     * @param version 升级时的版本号
     */
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, getMySqliteName(context, name), factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createDB(db);//创建数据库
    }

    /**
     * 删除所有表
     *
     * @param db
     */
    void dropDb(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Ulog.i("dropDb", "删除表 = " + cursor.getString(0));

                db.execSQL("DROP TABLE " + cursor.getString(0));

            }
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    private void createDB(SQLiteDatabase db) {
        db.execSQL(TB_Employee);//登录
        db.execSQL(TB_UserDepartment); //用户科室
        db.execSQL(TB_Department);//科室
        db.execSQL(TB_AssetsCard);//标签表
        db.execSQL(User_Table); //存储用户名密码
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UToast.showText("数据库更新");
        Ulog.i("onUpgrade-oldVersion", oldVersion);
        Ulog.i("onUpgrade-newVersion", newVersion);
        if (oldVersion < newVersion) {
            dropDb(db);//删除所有表
            createDB(db);//创建数据库
        }

    }

    /**
     * 清除数据库中卡片表
     */
    public void deleteTBAssetsCard(final DBDeleteListener listener) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                listener.onStart();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                SQLiteDatabase db = getWritableDatabase();
                int code;
                try {
                    db.execSQL("delete from TB_AssetsCard");
                    code = 200;
                } catch (SQLException e) {
                    code = 400;
                } finally {
                    db.close();
                }
                return code;
            }

            @Override
            protected void onPostExecute(Integer code) {
                super.onPostExecute(code);
                listener.onResponse(code);
            }
        }.execute();


    }


    /**
     * 查询已经存在的一条信息
     * <p>
     * HRCode char (10) PRIMARY KEY,
     * DeptCode char (6),
     * WorkDeptCode char (6),
     * EmployeeName nvarchar (50),
     * DutyName nvarchar (20),
     * Password varchar (50),
     * HosptialID integer,
     * HosptialName nvarchar (50),
     * DepartmentName nvarchar (1000),
     * HisCode varchar (20)
     * <p>
     * SELECT
     * b.DepartmentName,	a.*
     * FROM
     * TB_Employee a
     * left JOIN TB_Department b ON a.DeptCode = b.DeptCode
     * <p>
     * WHERE
     * a.HRCode =?
     */
    public void queryEmployee(String strHRCode, OnResponseListener3<UserBean> listener) {
        strHRCode = getStrHRCode(strHRCode);
        String sqlQueryEmployee = "SELECT b.DepartmentName AS DeptCodeName, a.*," +
                " ( SELECT DepartmentName FROM TB_Department LIMIT 1 ) AS 医院名称" +
                " FROM TB_Employee a " +
                "LEFT JOIN TB_Department b " +
                "ON a.DeptCode = b.DeptCode " +
                "WHERE a.HRCode like ? ";
        Ulog.i("sqlQueryEmployee", sqlQueryEmployee);
        Ulog.i("strHRCode", strHRCode);
        Cursor cursor = getReadableDatabase().rawQuery(sqlQueryEmployee, new String[]{strHRCode});

        if (cursor != null && cursor.moveToFirst()) {
            UserBean bean;
            do {
                String HRCode = cursor.getString(cursor.getColumnIndex("HRCode"));
                String DeptCode = cursor.getString(cursor.getColumnIndex("DeptCode"));
                String EmployeeName = cursor.getString(cursor.getColumnIndex("EmployeeName"));
                String DutyName = cursor.getString(cursor.getColumnIndex("DutyName"));
                String DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
                String HosptialName = cursor.getString(cursor.getColumnIndex("医院名称"));
                String HisCode = cursor.getString(cursor.getColumnIndex("HisCode"));
                String Password = cursor.getString(cursor.getColumnIndex("Password"));
                int HosptialID = cursor.getInt(cursor.getColumnIndex("HosptialID"));
                String DeptCodeName = cursor.getString(cursor.getColumnIndex("DeptCodeName"));

                bean = new UserBean();
                bean.setEmployeeName(EmployeeName);
                bean.setHRCode(HRCode);
                bean.setDeptCode(DeptCode);
                bean.setDutyName(DutyName);
                bean.setDepartmentName(DepartmentName);
                bean.setPassword(Password);
                bean.setHosptialID(HosptialID);
                bean.setHosptialName(HosptialName);
                bean.setHisCode(HisCode);
                bean.setDeptCodeName(DeptCodeName);


            } while (cursor.moveToNext());
            listener.onSuccess(bean);
            cursor.close();
            getReadableDatabase().close();

        } else {
            listener.onError(401, "没有数据");
        }
    }

    /**
     * 查询单据号
     *
     * @param listener
     */
    public void queryTakeInventoryCode(String strDeptCode, OnResponseListener3<List<String>> listener) {
        if (TextUtils.isEmpty(strDeptCode)) {
            listener.onError(401, "请先选择科室");
            return;
        }
        Cursor cursor = getReadableDatabase().rawQuery("select TakeInventoryCode from TB_AssetsCard where  DeptCode = ?  GROUP BY TakeInventoryCode",
                new String[]{strDeptCode});
        List<String> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            Ulog.i("cursor", cursor.getPosition());
            do {
                String strTakeInventoryCode = cursor.getString(cursor.getColumnIndex("TakeInventoryCode"));
                list.add(strTakeInventoryCode);
            } while (cursor.moveToNext());

            listener.onSuccess(list);
            cursor.close();
            getReadableDatabase().close();

        } else {
            listener.onError(401, "没有单据号");
        }
    }

    /**
     * 查询单据号
     *
     * @param listener
     */
    public void queryTakeInventoryCode(OnResponseListener3<List<TakeInventoryCodeBean>> listener) {

        Cursor cursor = getReadableDatabase().rawQuery(
                "select TakeInventoryCode,DepartmentName from TB_AssetsCard   where DepartmentName not null  GROUP BY TakeInventoryCode",
                null);
        List<TakeInventoryCodeBean> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            Ulog.i("cursor", cursor.getPosition());
            do {
                String TakeInventoryCode = cursor.getString(cursor.getColumnIndex("TakeInventoryCode"));
                String DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
                list.add(new TakeInventoryCodeBean(TakeInventoryCode, DepartmentName));
            } while (cursor.moveToNext());

            listener.onSuccess(list);
            cursor.close();
            getReadableDatabase().close();

        } else {
            listener.onError(401, "没有单据号");
        }
    }

    /**
     * 根据卡片号查询盘点单
     * SELECT a.name,a.TakeInventoryCode, a.CardNo, SpecName + ModelName AS 规格型号, DepreciationBy AS 折旧年限, TotalMoney AS 原值, date(AddDate) AS 使用日期, DepartmentName AS 使用科室, ManageDepartment AS 管理部门, UseStatus AS 状态
     * FROM TB_AssetsCard a WHERE a.TakeInventoryCode = 'GZPDD201811120001' and a.CardNo = 'GZKP201809200001' LIMIT 1
     *
     * @param listener
     */
    public void queryInventory(String strAssetID, OnResponseListener3<InventoryInfoBean> listener) {
        String sql = "SELECT " +
                "a.TakeInventoryCode as 单据号 , " +
                "a.CardNo as 产品码, " +
                "a.AssetID  , " +
                "a.name as 设备名称, " +
                "SpecName , " +
                "ModelName , " +
                "DepreciationBy AS 折旧年限, " +
                "TotalMoney AS 原值, " +
                "date(AddDate) AS 使用日期, " +
                "DepartmentName AS 使用科室, " +
                "ManageDepartment AS 管理部门, " +
                "UseStatus AS 状态 , " +
                "InventoryUseStatus AS InventoryUseStatus状态 , " +
                " InventorySite AS 盘点地点 , " +
                " PlaceAddress AS 原先地址 , " +
                "InventoryResult as 盘点结果 " +
                "FROM TB_AssetsCard a " +
                "WHERE   a.AssetID = ? LIMIT 1";

        Ulog.i("queryInventory-sql", sql);
        Ulog.i("queryInventory-strCardNo", strAssetID);

        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{strAssetID});

        if (cursor != null && cursor.moveToFirst()) {
            InventoryInfoBean bean;
            do {
                bean = getInventoryInfoBean(cursor);
            } while (cursor.moveToNext());
            listener.onSuccess(bean);
            cursor.close();
            getReadableDatabase().close();

        } else {
            listener.onError(401, "没有数据");
        }
    }

    /**
     * 根据卡片号和单据号查询盘点单
     * SELECT a.name,a.TakeInventoryCode, a.CardNo, SpecName + ModelName AS 规格型号, DepreciationBy AS 折旧年限, TotalMoney AS 原值, date(AddDate) AS 使用日期, DepartmentName AS 使用科室, ManageDepartment AS 管理部门, UseStatus AS 状态
     * FROM TB_AssetsCard a WHERE a.TakeInventoryCode = 'GZPDD201811120001' and a.CardNo = 'GZKP201809200001' LIMIT 1
     *
     * @param listener
     */
    public void queryInventory2(String strAssetID,String strTakeInventoryCode, OnResponseListener3<InventoryInfoBean> listener) {
        String sql = "SELECT " +
                "a.TakeInventoryCode as 单据号 , " +
                "a.CardNo as 产品码, " +
                "a.AssetID  , " +
                "a.name as 设备名称, " +
                "SpecName , " +
                "ModelName , " +
                "DepreciationBy AS 折旧年限, " +
                "TotalMoney AS 原值, " +
                "date(AddDate) AS 使用日期, " +
                "DepartmentName AS 使用科室, " +
                "ManageDepartment AS 管理部门, " +
                "UseStatus AS 状态 , " +
                "InventoryUseStatus AS InventoryUseStatus状态 , " +
                " InventorySite AS 盘点地点 , " +
                " PlaceAddress AS 原先地址 , " +
                "InventoryResult as 盘点结果 " +
                "FROM TB_AssetsCard a " +
                "WHERE   a.AssetID = ? and a.TakeInventoryCode = ?  LIMIT 1";

        Ulog.i("queryInventory-sql", sql);
        Ulog.i("queryInventory-strCardNo", strAssetID);
        Ulog.i("queryInventory-strTakeInventoryCode", strTakeInventoryCode);

        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{strAssetID,strTakeInventoryCode});

        if (cursor != null && cursor.moveToFirst()) {
            InventoryInfoBean bean;
            do {
                bean = getInventoryInfoBean(cursor);
            } while (cursor.moveToNext());
            listener.onSuccess(bean);
            cursor.close();
            getReadableDatabase().close();

        } else {
            listener.onError(401, "没有数据");
        }
    }


    /**
     * 提交盘点信息
     *
     * @param strInventoryUseStatus
     * @param strInventorySite
     * @param strAssetID
     * @param listener
     */
    public void confirmInventory(int strInventoryUseStatus,
                                 String strInventorySite,
                                 String strAssetID,
                                 OnResponseListener4 listener) {

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("UPDATE TB_AssetsCard SET  InventoryUseStatus = ?, InventorySite = ?, InventoryResult = 1 WHERE AssetID = ?  ",
                    new Object[]{strInventoryUseStatus, strInventorySite, strAssetID});
            listener.onSuccess();
        } catch (SQLException e) {
            listener.onError(OnResponseListener4.ERROR_CODE, e.getMessage());
        } finally {
            db.close();
        }
    }

    /**
     * 查询单据号
     *
     * @param listener
     */
    public void queryDepartment(OnResponseListener3<List<DepartmentBean>> listener) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT  count(a.DeptCode) as countNum, b.DepartmentName, b.DeptCode FROM TB_AssetsCard a JOIN ( SELECT * FROM TB_Department c GROUP BY c.DeptCode ) b ON a.DeptCode = b.DeptCode GROUP BY a.DeptCode ", null);
        List<DepartmentBean> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            Ulog.i("cursor", cursor.getPosition());
            do {
                String strDeptCode = cursor.getString(cursor.getColumnIndex("DeptCode"));
                String strDepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
                String countNum = cursor.getString(cursor.getColumnIndex("countNum"));
                list.add(new DepartmentBean(strDeptCode, strDepartmentName, countNum));
            } while (cursor.moveToNext());
            cursor.close();
            getReadableDatabase().close();

            listener.onSuccess(list);


        } else {
            listener.onError(401, "没有单据号");
        }
    }

    /**
     * 查询单据号
     */
    public List<InventoryInfoBean> queryInventoryInfoList(String strDeptCode, String strTakeInventoryCode) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT " +
                        "a.TakeInventoryCode as 单据号 , " +
                        "a.CardNo as 产品码, " +
                        "a.AssetID  , " +
                        "a.name as 设备名称, " +
                        "a.SpecName , " +
                        "a.ModelName , " +
                        "a.DepreciationBy AS 折旧年限, " +
                        "a.TotalMoney AS 原值, " +
                        "date(a.AddDate) AS 使用日期, " +
                        "a.DepartmentName AS 使用科室, " +
                        "a.ManageDepartment AS 管理部门, " +
                        "a.UseStatus AS 状态 , " +
                        "InventoryUseStatus AS InventoryUseStatus状态 , " +
                        " a.InventorySite AS 盘点地点 , " +
                        " a.PlaceAddress AS 原先地址 , " +
                        "a.InventoryResult as 盘点结果 " +
                        "FROM TB_AssetsCard a JOIN ( SELECT * FROM TB_Department c GROUP BY c.DeptCode ) b ON a.DeptCode = b.DeptCode  " +
                        "where b.DeptCode = ? and a.TakeInventoryCode= ? ",
                new String[]{strDeptCode, strTakeInventoryCode});
        List<InventoryInfoBean> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {

            do {
                InventoryInfoBean bean = getInventoryInfoBean(cursor);
                list.add(bean);

            } while (cursor.moveToNext());
            cursor.close();
            getReadableDatabase().close();
            return list;
        } else {
            return null;
        }
    }

    /**
     * 查看已盘点数目
     *
     * @param strDeptCode          科室号
     * @param strTakeInventoryCode 单据号
     */
    public int queryNoInventoryNum(String strDeptCode, String strTakeInventoryCode) {
        Ulog.i("strDeptCode", strDeptCode);
        Ulog.i("strTakeInventoryCode", strTakeInventoryCode);
        if (TextUtils.isEmpty(strDeptCode)) {
            UToast.showText("请选择科室");
            return 0;
        }
        if (TextUtils.isEmpty(strTakeInventoryCode)) {
            UToast.showText("请选择单据号");
            return 0;
        }
        Cursor cursor = getReadableDatabase().rawQuery("SELECT count(*) as 已盘点数目 " +
                        "FROM TB_AssetsCard a JOIN ( SELECT * FROM TB_Department c GROUP BY c.DeptCode ) b ON a.DeptCode = b.DeptCode  " +
                        "where b.DeptCode = ? and a.TakeInventoryCode= ? and InventoryResult = 1 ",
                new String[]{strDeptCode, strTakeInventoryCode});

        int InventoryTotalNum;
        if (cursor != null && cursor.moveToFirst()) {

            do {
                InventoryTotalNum = cursor.getInt(cursor.getColumnIndex("已盘点数目"));

            } while (cursor.moveToNext());
            cursor.close();
            getReadableDatabase().close();
            return InventoryTotalNum;
        } else {
            return 0;
        }
    }

    @NonNull
    private InventoryInfoBean getInventoryInfoBean(Cursor cursor) {
        String TakeInventoryCode = cursor.getString(cursor.getColumnIndex("单据号"));
        String CardNo = cursor.getString(cursor.getColumnIndex("产品码"));
        String AssetID = cursor.getString(cursor.getColumnIndex("AssetID"));
        String Name = cursor.getString(cursor.getColumnIndex("设备名称"));
        //这里其实是规格型号(由SpecName 和 ModelName)
        String SpecName = cursor.getString(cursor.getColumnIndex("SpecName"));
        String ModelName = cursor.getString(cursor.getColumnIndex("ModelName"));
        Double DepreciationBy = cursor.getDouble(cursor.getColumnIndex("折旧年限"));
        Double TotalMoney = cursor.getDouble(cursor.getColumnIndex("原值"));
        String StartUseDate = cursor.getString(cursor.getColumnIndex("使用日期"));
        String DepartmentName = cursor.getString(cursor.getColumnIndex("使用科室"));
        String ManageDepartment = cursor.getString(cursor.getColumnIndex("管理部门"));
        String InventorySite = cursor.getString(cursor.getColumnIndex("盘点地点"));
        String PlaceAddress = cursor.getString(cursor.getColumnIndex("原先地址"));
        int UseStatus = cursor.getInt(cursor.getColumnIndex("状态"));
        int InventoryUseStatus = cursor.getInt(cursor.getColumnIndex("InventoryUseStatus状态"));
        int InventoryResult = cursor.getInt(cursor.getColumnIndex("盘点结果"));

        InventoryInfoBean bean = new InventoryInfoBean();
        bean.setTakeInventoryCode(TakeInventoryCode);
        bean.setCardNo(CardNo);
        bean.setAssetID(AssetID);
        bean.setName(Name);
        bean.setDeviceCode(CardNo);
        bean.setSpecName(SpecName + (TextUtils.isEmpty(ModelName) ? "" : "/" + ModelName));
        bean.setDepreciationBy(DepreciationBy);
        bean.setTotalMoney(TotalMoney);
        bean.setStartUseDate(StartUseDate);
        bean.setDepartmentName(DepartmentName);
        bean.setManageDepartment(ManageDepartment);
        bean.setUseStatus(UseStatus);
        bean.setInventoryUseStatus(InventoryUseStatus);
        bean.setInventorySite(InventorySite);
        bean.setPlaceAddress(PlaceAddress);
        bean.setInventoryResult(InventoryResult);
        Ulog.i("getInventoryInfoBean", Ugson.toJson(bean));
        return bean;
    }

    /**
     * 补"0"操作,凑到12位
     *
     * @param strHRCode
     * @return
     */
    @NonNull
    private String getStrHRCode(String strHRCode) {
        if (strHRCode.contains("admin")) {
            return "%" + strHRCode + "%";
        }
        int indexTotal = 10;//应该是10位数
        if (strHRCode.length() >= indexTotal) {
            return "%" + strHRCode;
        } else {
            int t = indexTotal - strHRCode.length();//应该补多少个"0"
            for (int i = 0; i < t; i++) {
                strHRCode = 0 + strHRCode;
            }
            return "%" + strHRCode;
        }

    }
}

