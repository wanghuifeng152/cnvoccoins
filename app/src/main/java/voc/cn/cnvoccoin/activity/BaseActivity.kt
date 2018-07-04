package voc.cn.cnvoccoin.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import voc.cn.cnvoccoin.R

/**
 * Created by shy on 2018/3/28.
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //EventBus.getDefault().register(this)
        hideStatusBar()
    }
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 23) {
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





    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
//     EventBus.getDefault().unregister(this)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f)
        //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }
    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) {//非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults()//设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

//    override fun getResources(): Resources {
//        val res = super.getResources()
//        var config : Configuration ? = null
//        config!!.setToDefaults()
//        res!!.updateConfiguration(config,res!!.displayMetrics)
//        return res
//
//    }


}