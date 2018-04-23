package voc.cn.cnvoccoin.network;

import java.io.IOException;

import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;

public abstract class HttpSubscriber<T> extends ResourceSubscriber<ResponseBody> {

    @Override
    public void onNext(ResponseBody t) {

        try {

            _onNext(t.string());

        } catch (IOException e) {
            _onError(e);
        }

    }

    @Override
    public void onError(Throwable t) {
        _onError(t);
    }

    @Override
    public void onComplete() {
        _onComplete();
    }

    public abstract void _onNext(String t);

    public abstract void _onError(Throwable t);

    public abstract void _onComplete();

}
