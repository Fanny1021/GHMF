package com.fanny.ghmf.net;

import com.fanny.ghmf.bean.ResponseInfo;
import com.fanny.ghmf.util.ConstantUtil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Fanny on 18/4/2.
 */

public interface ResponseInfoApi {

    @GET(ConstantUtil.BED)
    Call<ResponseInfo> getBedInfo(@Query("motion") String motion);

    @GET(ConstantUtil.ICA)
    Call<ResponseInfo> getIcaInfo(@Query("mode") String mode,
                                  @Query("paramWater") long paramWater,
                                  @Query("paramWind") long paramWind);

    @GET(ConstantUtil.LOGIN)
    Call<ResponseInfo> getLoginInfo(@Query("username") String username,
                                    @Query("password")String password,
                                    @Query("phone")String phone,
                                    @Query("type")int type);
    @GET(ConstantUtil.HEALTH)
    Call<ResponseInfo> getHealthInfo(@Query("username") String username,
                                     @Query("category") String catrgory);

    @GET(ConstantUtil.LOCATION)
    Call<ResponseInfo> getDeviceLocationInfo(@Query("username") String username,
                                         @Query("DeviceMessage") String DeviceMessage);

}
