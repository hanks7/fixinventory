package com.easyway.fixinventory.utils.http;


import com.easyway.fixinventory.model.AssetsTakeInventoryForPDABean;
import com.easyway.fixinventory.model.InventoryInfoBean;
import com.easyway.fixinventory.model.UserBean;
import com.hanks.frame.base.BaseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/6/4
 * @description 所有请求的地址 和 请求的参数
 */
public interface HttpApis {


    /**
     * 登陆
     *
     * @param HRCode
     * @param Password
     * @return
     */
    @FormUrlEncoded
    @POST("/api/EmployeeForPDA")
    Call<BaseModel<UserBean>> login(
            @Field("hrCode") String HRCode,
            @Field("pwd") String Password
    );

    /**
     * 卡片信息获取
     *
     * @param CardNo 主码
     * @return
     */
    @FormUrlEncoded
    @POST("/api/AssetsTakeInventoryInfoForPDA")
    Call<BaseModel<InventoryInfoBean>> AssetsTakeInventoryInfoForPDA(
            @Field("CardNo") String CardNo
    );

    /**
     * 提交盘点信息
     *
     * @param AssetID
     * @param UseStatus
     * @param IsAddress
     * @param PlaceAddress
     * @return
     */
    @FormUrlEncoded
    @POST("/api/AssetsTakeInventoryForPDA")
    Call<BaseModel<AssetsTakeInventoryForPDABean>> AssetsTakeInventoryForPDA(
            @Field("AssetID") String AssetID,
            @Field("UseStatus") int UseStatus,
            @Field("IsAddress") int IsAddress,
            @Field("PlaceAddress") String PlaceAddress,
            @Field("HRCode") String HRCode
    );
}
