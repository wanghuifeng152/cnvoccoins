package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.jetbrains.annotations.Nullable;

import voc.cn.cnvoccoin.R;

/**
 * Created by shy on 2018/6/15.
 */

public class WalletActivity extends BaseActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        setWebView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView = findViewById(R.id.wv);
       final ProgressBar pb =  findViewById(R.id.pb);
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100){
                    pb.setVisibility(View.GONE);
                }else{
                    pb.setProgress(newProgress);
                }
            }
        });
    }

    private void setWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        //设置屏幕自适应
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        //缩放设置
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//关闭缓存

        mWebView.loadUrl("http://www.vochain.world/user/index/withdrawmoney");

    }
}
