package voc.cn.cnvoccoin.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.YHLog;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static voc.cn.cnvoccoin.util.ConstantsKt.TOKEN;

public class RetrofitWrap {

    //设置连接超时的值
    private static final int TIMEOUT = 15;
    protected static volatile RetrofitWrap retrofitWrap = null;
    private Retrofit retrofit;

    public static RetrofitWrap getInstance() {
        if (retrofitWrap == null) {
            synchronized (RetrofitWrap.class) {
                if (retrofitWrap == null) {
                    retrofitWrap = new RetrofitWrap.Builder().build();
                }
            }
        }
        return retrofitWrap;
    }

    private static RetrofitWrap sPayInstance;

    public static RetrofitWrap getPayInstance() {
        if (sPayInstance == null) {
            if (sPayInstance == null) {
                sPayInstance = new RetrofitWrap.Builder().setProxyClientBuild().build();
            }

        }
        return sPayInstance;
    }


    public RetrofitWrap(Builder builder) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(builder.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.clientBuilder.build())
                .build();

    }

    public static final class Builder {
        private OkHttpClient.Builder clientBuilder;
        private String baseUrl;
        private boolean needSign;
        private SSLSocketFactory sslSocketFactory;
        private HostnameVerifier hostnameVerifier;

        public Builder() {
            clientBuilder = defaultClientBuilder();
            baseUrl = HttpConfig.DEFAULT_HOST;
            needSign = true;
        }

        public Builder clientBuilder(OkHttpClient.Builder clientBuilder) {
            this.clientBuilder = clientBuilder;
            return this;
        }

        public Builder setProxyClientBuild() {
            this.clientBuilder = proxyPayClientBuilder();
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder needSign(boolean needSign) {
            this.needSign = needSign;
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }


        public RetrofitWrap build() {
            if (hostnameVerifier != null) {
                clientBuilder.hostnameVerifier(hostnameVerifier);
            }
            if (sslSocketFactory != null) {
                clientBuilder.sslSocketFactory(sslSocketFactory);
            }
            if (needSign) {
                clientBuilder.addInterceptor(new CommonParamInterceptor());
            }
            clientBuilder.addInterceptor(new BaseUrlInterceptor());
            return new RetrofitWrap(this);
        }

        public OkHttpClient.Builder defaultClientBuilder() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor())//添加拦截器
                    .addInterceptor(logging)//添加打印日志
                    .addNetworkInterceptor(new StethoInterceptor())
//                    .addInterceptor(new CommonParamInterceptor())
//                .addInterceptor(new CacheInterceptor())
                    //设置连接超时
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    //设置从主机读信息超时
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    //设置写信息超时
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);//设置出现错误进行重新连接。
            // .cache(new Cache(YHShopApplication.getInstance() ,10 * 1024 * 1024)) //10M cache

            return builder;
        }

        public OkHttpClient.Builder proxyPayClientBuilder() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor())//添加拦截器
                    .addInterceptor(logging)//添加打印日志
                    .addNetworkInterceptor(new StethoInterceptor())
//                    .addInterceptor(new CommonParamInterceptor())
//                .addInterceptor(new CacheInterceptor())
                    //设置连接超时
                    .connectTimeout(HttpConfig.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    //设置从主机读信息超时
                    .readTimeout(120, TimeUnit.SECONDS)
                    //设置写信息超时
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);//设置出现错误进行重新连接。
            // .cache(new Cache(YHShopApplication.getInstance() ,10 * 1024 * 1024)) //10M cache

            return builder;
        }

    }

    //拦截器
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String token = PreferenceUtil.Companion.getInstance().getString(TOKEN);
            Request authorised;
            if(!TextUtils.isEmpty(token)){
                 authorised = request
                        .newBuilder()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .header("XX-Device-Type","android")
                         .header("XX-Token",token)
//                    .header("User-Agent", UserAgentUtil.generateUserAgent())
//                    .header("X-YH-Biz-Params", CustomHttpHeaderUtil.addHeaderParams())
                        .build();
            }else{
                 authorised = request
                        .newBuilder()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .header("XX-Device-Type","android")
//                    .header("User-Agent", UserAgentUtil.generateUserAgent())
//                    .header("X-YH-Biz-Params", CustomHttpHeaderUtil.addHeaderParams())
                        .build();
            }

            return chain.proceed(authorised);
        }
    }

    private static class CommonParamInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest == null) {
                return null;
            }

            Map<String, String> commonParamsMap = RequestCommonParams.getCommonParameters();
            Map<String, String> signMap = new HashMap<>(commonParamsMap);
            for (String key : originalRequest.url().queryParameterNames()) {
                String value = originalRequest.url().queryParameter(key);
                if (!TextUtils.isEmpty(value)) {
                    signMap.put(key, value);
                }
            }
            StringBuilder stringBuilder = RequestCommonParams.getParamBySort(signMap);
            if (chain.request().body() != null && chain.request().body() instanceof RequestBodyWrapper) { //post request
                RequestBodyWrapper requestBody = (RequestBodyWrapper) chain.request().body();
                if (requestBody != null && !TextUtils.isEmpty(requestBody.getBody())) {
                    stringBuilder.append(requestBody.getBody());
                }
            }

            HttpUrl.Builder build = originalRequest.url().newBuilder();
            for (Map.Entry<String, String> entry : commonParamsMap.entrySet()) {
                build.addQueryParameter(entry.getKey(), entry.getValue());
            }
//            build.addQueryParameter("sign", Security.signParams(stringBuilder.toString()));

            HttpUrl modifiedUrl = build.build();
            Request request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        }
    }


    private static class BaseUrlInterceptor implements Interceptor {
        public volatile String host;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (!TextUtils.isEmpty(host)) {
                HttpUrl newUrl = originalRequest.url().newBuilder()
                        .host(host)
                        .build();
                originalRequest = originalRequest.newBuilder()
                        .url(newUrl)
                        .build();
            }
            Request newRequest = chain.request();
            YHLog.i("---url--->" + newRequest.url().toString());
            if (newRequest.body() != null && newRequest.body() instanceof RequestBodyWrapper) {
                YHLog.i("---body---> " + ((RequestBodyWrapper) chain.request().body()).getBody());
            }

            return chain.proceed(originalRequest);
        }
    }

    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetWorkActive(VocApplication.Companion.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                YHLog.i("no network");
            }
            Response response = chain.proceed(request);

            if (NetWorkUtil.isNetWorkActive(VocApplication.Companion.getInstance())) {
                int maxAge = 0 * 60; // force get data from network
                YHLog.i("has network maxAge=" + maxAge);
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")// clear pragma , in case of error
                        .build();
            } else {
                YHLog.i("network error");
                int maxStale = 60 * 60 * 24; // no network,cache one day
                YHLog.i("has maxStale=" + maxStale);
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
                YHLog.i("response build maxStale=" + maxStale);
            }
            return response;

        }
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    public ResourceSubscriber subscribeWith(Flowable observable, ResourceSubscriber subscriber) {
        return (ResourceSubscriber) observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnLifecycle(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {

                    }
                }, new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribeWith(subscriber);
    }


}

