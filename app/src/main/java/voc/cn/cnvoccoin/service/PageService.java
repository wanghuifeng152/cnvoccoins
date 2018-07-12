package voc.cn.cnvoccoin.service;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import voc.cn.cnvoccoin.entity.UploadVoiceBean;
import voc.cn.cnvoccoin.network.ResBaseModel;

public interface PageService {
//    刷币
    @FormUrlEncoded
    @POST("api/portal/voc/uploadVocCoinV2")
    Observable<ResBaseModel<UploadVoiceBean>> getPage(@FieldMap Map<String,Object> parameter);

    //    刷币
    @FormUrlEncoded
    @POST("api/user/Verification_code/locakVoc")
    Observable<ResponseBody> getMon(@Field("") String s);
}
