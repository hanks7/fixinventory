package com.easyway.fixinventory.base;


import android.os.AsyncTask;

import com.easyway.fixinventory.dao.DbHelper;
import com.easyway.fixinventory.model.UserBean;
import com.easyway.fixinventory.utils.http.HttpAdapter;
import com.hanks.frame.base.HjjApplication;
import com.hanks.frame.utils.Ugson;
import com.hanks.frame.utils.Usp;

import static com.hanks.frame.utils.UmengUtils.initUmeng;

public class BaseApplication extends HjjApplication {
    public static final String USER_MODEL = "UserModel";
    public UserBean userModel;

    @Override
    public void onCreate() {
        super.onCreate();
        init();//在异步任务中初始化要初始化的组件
        getUserModel();//app启动后把个人信息存放在内存中
    }

    /**
     * 和HjjApplication 写法重复.
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return (BaseApplication) application;
    }

    /**
     * 在异步任务中初始化要初始化的组件
     */
    private void init() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HttpAdapter.init();
                initUmeng();
                DbHelper.getbean(getApplicationContext());
                return null;
            }
        }.execute();
    }


    /**
     * 将个人信息存到sharedperence和内存中
     *
     * @param userModel
     * @param loginState 1:在线 2:离线
     */
    public void setUserModel(UserBean userModel, int loginState) {
        userModel.setLoginState(loginState);
        this.userModel = userModel;
        Usp.put(this, "UserModel", Ugson.toJson(userModel));
    }

    /**
     * 从sharedperence取出个人信息存到内存中
     */
    public UserBean getUserModel() {
        String json = (String) Usp.get(this, USER_MODEL, Ugson.toJson(new UserBean()));
        userModel = Ugson.toBean(json, UserBean.class);
        return userModel;
    }

    /**
     * 退出登录,清除个人信息
     */
    public void quitLogin() {
        Usp.remove(getApplicationContext(), "UserModel");
        getUserModel();
    }
}
