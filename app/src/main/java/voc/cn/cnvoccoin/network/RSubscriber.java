package voc.cn.cnvoccoin.network;

import android.content.Context;

import io.reactivex.subscribers.ResourceSubscriber;

public abstract class RSubscriber<T> extends ResourceSubscriber<T> {

    private Context context;

    protected RSubscriber(Context context) {
        this.context = context;
    }

    protected RSubscriber() {
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        _onError(t);
    }

    @Override
    public void onComplete() {
        _onComplete();
    }

    public abstract void _onNext(T t);

    public abstract void _onError(Throwable t);

    public abstract void _onComplete();

}
