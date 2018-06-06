package voc.cn.cnvoccoin

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.addLogAdapter


/**
 * Created by shy on 2018/3/24.
 */
class VocApplication : Application {
    private val TAG = this.javaClass.simpleName


    constructor() {
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: VocApplication
        fun getInstance(): VocApplication {
            return sInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
//        CrashReport.initCrashReport(applicationContext, "注册时申请的APPID", false)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "")
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setDebugMode(true)
        Logger.addLogAdapter(AndroidLogAdapter());
    }

}