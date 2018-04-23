package voc.cn.cnvoccoin.network;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by freedy on 5/4/17.
 */

public class RequestBodyWrapper extends RequestBody {

    private String postBody ;

    public <T>RequestBodyWrapper(T model) {
        Gson gson = new Gson();
        postBody = gson.toJson(model);
        check() ;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/json; charset=utf-8");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(postBody.getBytes(), 0, postBody.getBytes().length);
    }

    private void check() {
        if (postBody == null || postBody.getBytes() == null) throw new NullPointerException("content == null");
    }

    public String getBody() {
        return postBody ;
    }
}
