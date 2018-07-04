package voc.cn.cnvoccoin.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import voc.cn.cnvoccoin.R
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyCharacterMap.deviceHasKey
import android.support.v4.view.ViewConfigurationCompat.hasPermanentMenuKey
import android.view.*


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
//        getWindow().getDecorView().findViewById<View>(android.R.id.content).setPadding(0, 0, 0, getNavigationBarHeight())
        if (Build.VERSION.SDK_INT >= 24) {
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

    fun getNavigationBarHeight(): Int {

        val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        if (!hasMenuKey && !hasBackKey) {
            val resources = resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            //获取NavigationBar的高度
            val height = resources.getDimensionPixelSize(resourceId)
            return height
        } else {
            return 0
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