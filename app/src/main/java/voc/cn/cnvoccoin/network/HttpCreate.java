package voc.cn.cnvoccoin.network;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import com.orhanobut.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.util.ConstantsKt;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.YHLog;

public abstract class HttpCreate<T> implements Publisher {
    public final static int NEED_PRE_HANDLE = 100;
    public final static int NO_NEED_PRE_HANDLE = 200;

    private HttpLoading loading;
    private HttpSubscriber subscriber;
    private CompositeDisposable mCompositeSubscription;

//    private HttpOnSubscribe source;
    protected String url;
    protected Map<String, ?> options;
    protected T model;

    // TODO: 5/18/17 todo  add ....
    private final int[] INTERCEPTER_CODES = new int[]{};
    private final String DEFAULT_ERR_MSG = "出错了，请稍后重试";

    public HttpCreate(String url) {
        this.url = url;
        mCompositeSubscription = new CompositeDisposable();
    }

    public HttpCreate(String url, Map<String, ?> options, T model) {
        this(url);
        this.options = options;
        this.model = model;
    }

    public HttpCreate(String url, Map<String, ?> options) {
        this(url);
        this.options = options;
    }

    public HttpCreate(String url, T model) {
        this(url);
        this.model = model;
    }

//    public HttpCreate(HttpOnSubscribe source) {
//        this.source = source;
//    }

    @Override
    public HttpCreate subscribe(final Subscriber sub) {
        return subscribe(sub, true);
    }

    public HttpCreate subscribe(final Subscriber sub, int optionCode) {
        return subscribe(sub, null, null, false, optionCode);
    }

    public HttpCreate subscribe(final Subscriber sub, boolean isShowToast) {
        return subscribe(sub, null, isShowToast);
    }

    public HttpCreate subscribe(final Subscriber sub, final Type dataOfType) {
        return subscribe(sub, dataOfType, true);
    }


    public HttpCreate subscribe(final Subscriber sub, final Type dataOfType, boolean isShowToast) {
        return subscribe(sub, dataOfType, null, isShowToast, NEED_PRE_HANDLE);
    }

    public HttpCreate subscribe(final Subscriber sub, final Type dataOfType, final Type baseOfType) {
        return subscribe(sub, dataOfType, baseOfType, true, NEED_PRE_HANDLE);
    }

    public HttpCreate subscribe(final Subscriber sub, final Type dataOfType, final Type baseOfType, final boolean isShowToast, final int optionCode) {
        final HttpSubscriber httpSubscriber = new HttpSubscriber() {

            @Override
            public void _onNext(String s) throws JSONException {
                YHLog.e("---response666---> " + s);
                if (s == null) {
                    sub.onError(new ErrorCodeThrowable(-1,"response is null"));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        String tip = jsonObject.has("msg") ? jsonObject.getString("msg") : "";
                        int code = jsonObject.has("code") ? jsonObject.getInt("code") : -1;
                        if(code == 10001){
                            ToastUtil.showToast(tip);
//                          PreferenceUtil.Companion.getInstance().set(ConstantsKt.TOKEN,"");
                            SharedPreferences flag = VocApplication.Companion.getInstance().getSharedPreferences("voccoin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = flag.edit();
                            editor.putString(ConstantsKt.TOKEN,"");
                            editor.commit();
                            sub.onComplete();
                            return;
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.showToast(e.getMessage());
                    e.printStackTrace();
                    sub.onError(new ErrorCodeThrowable(-1, DEFAULT_ERR_MSG));
                    sub.onComplete();
                    if (isShowToast) {
//                        ToastUtil.showToast(DEFAULT_ERR_MSG);
                        ToastUtil.showToast(e.toString());
                        Log.e("TAG","----错误信息="+e.toString());
                    }
                }


                if (NO_NEED_PRE_HANDLE == optionCode) {
                    sub.onNext(s);
                    return;
                }


                if (baseOfType == null || ((Class) baseOfType).getSimpleName().equals(ResBaseModel.class.getSimpleName()) ) { //default response type（code，message，data）
                    if (!preHandleResponse(s, sub, isShowToast)) { //failed for pre handle
                        return;
                    }
                }

                if (dataOfType == null) {
                    sub.onNext(s);
                } else {
                    if (baseOfType != null) {
                        try {
                            Object resBaseModel = parseResult(s, dataOfType, baseOfType);
                            sub.onNext(resBaseModel);
                        } catch (Exception e) {
                            sub.onError(new ErrorCodeThrowable(-1, e.getMessage()));
                            sub.onComplete();
                        /*    if (AppBuildConfig.isDebug()) {
                                ToastUtil.showToast(e.getMessage());
                            }*/
                        }
                    } else {
                        try {
                            ResBaseModel resBaseModel = (ResBaseModel) parseResult(s, dataOfType, null);
                            if (isShowToast && resBaseModel != null && !TextUtils.isEmpty(resBaseModel.msg)) {
                                ToastUtil.showToast(resBaseModel.msg);
                            }
                            if (resBaseModel.data == null) {
                               sub.onError(new ErrorCodeThrowable(-1, resBaseModel.msg));
                            } else {
                                sub.onNext(resBaseModel.data);
                            }

                        } catch (Exception e) {
                          /*  if (AppBuildConfig.isDebug()) {
                                ToastUtil.showToast(e.getMessage());
                            }*/
                        }
                    }
                }
            }

            @Override
            public void _onError(Throwable t) {
                if (NO_NEED_PRE_HANDLE != optionCode) {
                    HttpResponseParser.parse(t);
                }
                sub.onError(new ErrorCodeThrowable(-1, t.getMessage()));
                sub.onComplete();
                YHLog.i("---response444---> error : " + t.getMessage() + "; cause : " + t.getCause());
            }

            @Override
            public void _onComplete() {
                sub.onComplete();
                YHLog.i("retrofit---->onComplete--");
            }

        };
        this.subscriber = httpSubscriber;
        addDisposable(subscriber);
        createRestService();
        return this;
    }

    private boolean preHandleResponse(String s, Subscriber sub, boolean isShowTaost) {
        boolean intercepter = false ;
        JSONObject jsonObject = null;
        String tip = "";
        int code = -1;

        try {
            jsonObject = new JSONObject(s);
            if (jsonObject != null) {
                tip = jsonObject.has("msg") ? jsonObject.getString("msg") : "";
                code = jsonObject.has("code") ? jsonObject.getInt("code") : -1;
            }
        } catch (Exception e) {
            Log.e("aaa",e.getMessage());
            e.printStackTrace();
            sub.onError(new ErrorCodeThrowable(-1, DEFAULT_ERR_MSG));
            sub.onComplete();
            if (isShowTaost) {
                ToastUtil.showToast(DEFAULT_ERR_MSG);
            }
        }
        if (!HttpRestService.intercept(code, tip)) {
            if (isShowTaost) {
                ToastUtil.showToast(tip);
            }
            intercepter = true ;
        }


        if (code != 1) {
            if (isShowTaost && !TextUtils.isEmpty(tip)) {
                ToastUtil.showToast(tip);
            }
            intercepter = true ;
        }
        if (code == 10002 ) {
            if (isShowTaost && !TextUtils.isEmpty(tip)) {
                ToastUtil.showToast(tip);
            }
        }
        String message = TextUtils.isEmpty(tip) ? DEFAULT_ERR_MSG : tip;
        if (needIntercepter(code) || intercepter) {
            sub.onError(new ErrorCodeThrowable(code, message));
            sub.onComplete();
            /*   if (isShowTaost) {
                ToastUtil.showToast(message);
            }*/
            return false;
        }
        return true;
    }

    @Override
    public HttpCreate setHttpLoading(HttpLoading loading) {
        this.loading = loading;
        return this;
    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.add(disposable);
        }
    }

    @Override
    public void detach() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    protected void requestActual(final Flowable flowable) {
       requestActual(flowable, true);
    }

    protected void requestActual(final  Flowable flowable, boolean needCompose) {
        if (!needCompose) {
            flowable.subscribeWith(subscriber);
        } else {
            flowable.compose(Transformer.<String>schedulers())
                    .subscribeWith(subscriber);
        }
    }

    protected abstract void createRestService();

    private Object parseResult(String response, final Type typeOfData, final Type typeOfBase) throws Exception

    {
        Gson gson = new Gson();
        Type resultType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{typeOfData};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                if (typeOfBase != null) {
                    return typeOfBase;
                }
                return ResBaseModel.class;
            }
        };
        return gson.fromJson(response, resultType);
    }

    private boolean needIntercepter(int code) {
        for (int c : INTERCEPTER_CODES) {
            if (c == code) {
                return true;
            } else {
                break;
            }
        }
        return false;
    }

    protected <K, V> Map filterNullValue(Map<K, V> options) {
        if (options == null) return null;
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : options.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getValue() instanceof String && TextUtils.isEmpty((String) entry.getValue()))
                    continue;
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}