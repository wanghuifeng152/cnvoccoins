package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_news.*
import voc.cn.cnvoccoin.R

/**
 * Created by shy on 2018/5/14.
 */
const val TAG = "TAG"
class NewsActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setWebView()
        getparams()
    }

    private fun getparams() {
        if(intent == null)return
        val tag = intent.getIntExtra(TAG, 1)
        when(tag){
            1 ->{ wv.loadUrl("http://www.vochain.world/portal/article/index/id/2.html") }
            2 ->{ wv.loadUrl("http://www.vochain.world/portal/article/index/id/3.html") }
        }

        wv.webChromeClient = object :WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress == 100){
                    pb.visibility = View.GONE
                }else{
                    pb.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }
        iv_back.setOnClickListener { finish()}
    }

    private fun setWebView() {
        val settings = wv.settings
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