package voc.cn.cnvoccoin.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        voc_webview.webViewClient = WebViewClient()
        voc_webview.loadUrl("http://www.vocalchain.io/")
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
