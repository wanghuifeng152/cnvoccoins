package voc.cn.cnvoccoin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;

public class WalletTwoActivity extends Activity {

    @BindView(R.id.voc_webview)
    WebView vocWebview;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_two);
        ButterKnife.bind(this);
        final String url = "http://www.vochain.world./user/index/createmoney";
        vocWebview.loadUrl(url);
        WebSettings settings = vocWebview.getSettings();

        settings.setJavaScriptEnabled(true);

        //屏幕适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        //缩放设置
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
