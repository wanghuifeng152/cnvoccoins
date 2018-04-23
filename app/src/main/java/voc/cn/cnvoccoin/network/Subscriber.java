package voc.cn.cnvoccoin.network;

public interface Subscriber<T>  {

    void onNext(T t);

    void onError(Throwable t);

    void onComplete();
}
