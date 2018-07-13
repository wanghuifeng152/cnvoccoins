package voc.cn.cnvoccoin.network;

import android.support.annotation.Keep;


@Keep
public class ResBaseModel<T> {

    public int code;
    public String msg;
//    public long now;
    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
