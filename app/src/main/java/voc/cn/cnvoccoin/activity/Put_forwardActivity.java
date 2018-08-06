package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;

public class Put_forwardActivity extends BaseActivity {

    @BindView(R.id.web_put)
    WebView webPut;
    @BindView(R.id.identity_back)
    ImageView identityBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_forward);
        ButterKnife.bind(this);
        initData();
        identityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
