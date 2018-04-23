package voc.cn.cnvoccoin.network;

public interface Publisher {

    <T> Publisher subscribe(Subscriber<T> s);

    void detach();

    Publisher setHttpLoading(HttpLoading loading);
}