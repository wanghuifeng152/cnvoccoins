package voc.cn.cnvoccoin.util;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    private  static OkHttpUtils okHttpUtils;
    private OkHttpClient okHttpClient;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpUtils() {
        okHttpClient =  new OkHttpClient.Builder().build();
    }

    public static OkHttpUtils getInstens(){
        if (okHttpUtils == null){
            synchronized (OkHttpUtils.class){
                if (okHttpUtils == null){
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }

    public Call postHttp(String url,String json){
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).build();
        return okHttpClient.newCall(request);
    }
}
