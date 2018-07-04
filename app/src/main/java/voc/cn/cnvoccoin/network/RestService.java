package voc.cn.cnvoccoin.network;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import voc.cn.cnvoccoin.entity.DetailedClass;

/**
 * Author    Jie.He
 * Function  网络接口 Retrofit().create（）
 */

public interface RestService {


    @Headers("Cache-Control: max-age=20000")
    @GET
    Flowable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> options);

    @GET
    Flowable<ResponseBody> get(@Url String url);

    @POST
    Flowable<ResponseBody> post(@Url String url, @QueryMap Map<String, Object> options, @Body RequestBody body);

    @POST
    Flowable<ResponseBody> post(@Url String url, @Body RequestBody body);

    @Streaming
    @GET()
    Flowable<ResponseBody> downLoad(@Url String url);

    @Multipart
    @POST()
    Flowable upload(@Url String url, @Part("body") RequestBody body, @Part MultipartBody.Part file);

}
