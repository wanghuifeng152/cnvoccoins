package voc.cn.cnvoccoin

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

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
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"")
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        //todo
//        MobclickAgent.setSecret(this, "s10bacedtyz")
    }

}