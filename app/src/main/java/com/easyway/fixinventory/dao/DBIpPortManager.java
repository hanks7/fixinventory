package com.easyway.fixinventory.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easyway.fixinventory.base.IPPortBean;
import com.hanks.frame.utils.Ulog;

import java.util.ArrayList;
import java.util.List;

/**
 * author Bro0cL on 2016/1/26.
 */
public class DBIpPortManager extends SQLiteOpenHelper {

    public static final String tb_ip_prot = "tb_ip_prot";
    public static final String tb_hosid = "tb_hosid";
    public static final String tb_login_account = "tb_login_account";
    Context context;

    public DBIpPortManager(Context context) {
        super(context, "HistoricRecordsManager.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db, tb_ip_prot);
        create(db, tb_hosid);
        create(db, tb_login_account);

    }

    private void create(SQLiteDatabase db, String tb_name) {
        db.execSQL("Create table if not exists " + tb_name + " ("
                + " id int auto_increment ,"
                + " param1 nvarchar(100) unique,"
                + " param2 nvarchar(100) unique)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 查询所有数据
     */
    public List<String> queryAll(String tb_name) {

        Cursor cursor = getReadableDatabase().rawQuery("select * from " + tb_name, new String[]{});
        List<String> list = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String param1 = cursor.getString(cursor.getColumnIndex("param1"));
                Ulog.i("param1", param1);
                list.add(param1);

            }
            cursor.close();
            getReadableDatabase().close();
        }
        return list;
    }


    /**
     * 向数据库中插入和更新数据
     *
     * @param content
     */
    public void insert(String content, String i) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("insert into " + i + " ( param1) " +
                    "values('" + content + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
    }

    /**
     * 向数据库中插入和更新数据
     */
    public void insert(IPPortBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        // 开启事务
        db.beginTransaction();
        try {
            db.execSQL("insert into " + tb_ip_prot + " ( param1) " +
                    "values('" + bean.getIpPort().replace("http://", "") + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL("insert into " + tb_hosid + " ( param1) " +
                    "values('" + bean.getHosID() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //设置事务标志为成功，当结束事务时就会提交事务
        db.setTransactionSuccessful();
        // 结束事务
        db.endTransaction();
        db.close();

    }

    /**
     * 向数据库中插入和更新数据(测试的)无用可删除
     */
    public void insertAll() {

        // 开启事务
        getWritableDatabase().beginTransaction();
        try {
            for (int i = 0; i < 100; i++) {
                getWritableDatabase().execSQL("insert into tb_test( param1,param2) " +
                        "values('" + i + "','" +
                        i + "')");
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            getWritableDatabase().setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // 结束事务
        getWritableDatabase().endTransaction();

        getWritableDatabase().close();
    }


}
