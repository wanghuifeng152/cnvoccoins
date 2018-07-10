package voc.cn.cnvoccoin.entity;

public class BaseShuMeiEntity<T> {
    private String accessKey;
    private String appId;
    private String eventId;
    private T data;

    public BaseShuMeiEntity(String accessKey, String appId, String eventId, T data) {
        this.accessKey = accessKey;
        this.appId = appId;
        this.eventId = eventId;
        this.data = data;
    }


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
