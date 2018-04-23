package voc.cn.cnvoccoin.network;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

public final class HttpPostCreate<T> extends HttpCreate {

    public HttpPostCreate(String url, Map<String, Object> options, T model) {
        super(url, options, model);
    }

    public HttpPostCreate(String url, T model) {
        super(url, model);
    }

    @Override
    protected void createRestService() {
        HttpManager.checkNotNull(model, "http post body == null");
        RequestBody postBody  ;
        if (model instanceof RequestBody) {
            postBody = (RequestBody) model;
        } else {
            postBody = new RequestBodyWrapper(model);
        }

        Flowable flowable;
        RetrofitWrap retrofitWrap = RetrofitWrap.getInstance() ;

        if (options != null && !options.isEmpty()) {
            Map map = filterNullValue(options) ;
            flowable = retrofitWrap.create(RestService.class).post(url, map, postBody);
        } else {
            flowable = retrofitWrap.create(RestService.class).post(url, postBody);
        }
        requestActual(flowable);
    }




}