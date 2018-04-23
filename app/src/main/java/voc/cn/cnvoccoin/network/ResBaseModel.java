package voc.cn.cnvoccoin.network;

import android.support.annotation.Keep;


@Keep
public class ResBaseModel<T> {

    public int code;
    public String msg;
//    public long now;
    public T data;
}
