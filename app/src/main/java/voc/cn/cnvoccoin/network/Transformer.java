package voc.cn.cnvoccoin.network;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Transformer {

    public static <T> FlowableTransformer<T, T> schedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> f) {
                return f.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

   /* public static <T> FlowableTransformer<ResBaseModel<T>, T> retrofit() {
        return new FlowableTransformer<ResBaseModel<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<ResBaseModel<T>> f) {
                return f
                        .flatMap(new Function<ResBaseModel<T>, Publisher<T>>() {
                            @Override
                            public Publisher<T> apply(ResBaseModel<T> t) throws Exception {
                                if (t == null) {
                                    return Flowable.error(new ResponseNullException("response's model is null"));
                                } else {
                                    if (t.now > 0) {
                                        ServerTime.getDefault().syncTimeStamp(t.now);
                                    }
                                    if (t.isSuccess()) {
                                        return Flowable.just(t.data);
                                    } else {
                                        return Flowable.error(new ServerException(t.code, t.message));
                                    }
                                }
                            }
                        })
                        .compose(Transformer.<T>schedulers());//线程切换
            }
        };
    }*/
}
