package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;

public class Put_forwardActivity extends AppCompatActivity {

    @BindView(R.id.web_put)
    WebView webPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        WebSettings settings = webPut.getSettings();
        settings.setJavaScriptEnabled(true);
        webPut.loadUrl("file:///android_asset/web.html");
//        webPut.loadUrl("https://blog.csdn.net/u011150924/article/details/53079916");

//        webPut.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                webPut.loadUrl("file:///android_assets/web.html");
//                return true;
//            }
//        });
    }
}
