package voc.cn.cnvoccoin.util;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static RetrofitUtils retrofitUtils;
//    使用单利模式写的Retrofit工具类
    private static Retrofit retrofit;
    public RetrofitUtils() {
        retrofit = new Retrofit.Builder().baseUrl("http://172.11.20.162/voc/public/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static RetrofitUtils getInstance(){
        if (retrofitUtils == null){
            synchronized (RetrofitUtils.class){
                if (retrofitUtils == null){
                    retrofitUtils = new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }
//    利用泛型返回Service
    public <T> T getService(Class<T> service){
        return retrofit.create(service);
    }
}
