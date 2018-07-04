package voc.cn.cnvoccoin.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_voc_official.*
import voc.cn.cnvoccoin.R

class VocOfficialActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voc_official)
        setWebView()
        hideStatusBar()
        voc_webview.webViewClient = WebViewClient()
        voc_webview.loadUrl("http://www.vocalchain.io/")
    }
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var window = getWindow()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
    private fun setWebView() {
        val settings = voc_webview.settings
        settings.javaScriptEnabled = true

        //设置屏幕自适应
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        //缩放设置
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        settings.defaultTextEncodingName = "utf-8"
        settings.loadsImagesAutomatically = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭缓存
    }
}
