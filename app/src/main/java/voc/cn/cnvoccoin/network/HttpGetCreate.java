package voc.cn.cnvoccoin.network;


import java.util.Map;

import io.reactivex.Flowable;

public final class HttpGetCreate<T> extends HttpCreate {


    public HttpGetCreate(String url, Map<String, Object> options) {
        super(url, options);
    }

    public HttpGetCreate(String url, T model) {
        super(url, model);
    }

    public HttpGetCreate(String url) {
        super(url);
    }

//    public HttpGetCreate(HttpOnSubscribe source) {
//        super(source);
//    }

    @Override
    protected void createRestService() {
        if (model != null) {
            options = RequestCommonParams.convertModelToMap(model);
        }
        HttpManager.checkNotNull(url, "http get url == null");
        Flowable flowable;
        RetrofitWrap retrofitWrap = RetrofitWrap.getInstance() ;
        if (options != null && !options.isEmpty()) {
            Map map  = filterNullValue(options);
            flowable = retrofitWrap.create(RestService.class).get(url, map);
        } else {
            flowable = retrofitWrap.create(RestService.class).get(url);
        }
        requestActual(flowable);
    }
}