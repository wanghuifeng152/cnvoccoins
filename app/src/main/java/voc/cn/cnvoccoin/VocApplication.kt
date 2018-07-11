package voc.cn.cnvoccoin

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import com.lzy.okgo.OkGo
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.addLogAdapter
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import voc.cn.cnvoccoin.activity.TaskActivity
import voc.cn.cnvoccoin.activity.VocOfficialActivity
import voc.cn.cnvoccoin.activity.VoiceActivityNew
import voc.cn.cnvoccoin.activity.WalletActivity
import java.util.ArrayList
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushInterface.*
import com.ishumei.smantifraud.SmAntiFraud
import com.ishumei.smantifraud.SmAntiFraud.option
import voc.cn.cnvoccoin.activity.MainActivity
import cn.jpush.android.api.CustomPushNotificationBuilder
import cn.jpush.android.data.JPushLocalNotification
import org.json.JSONObject


/**
 * Created by shy on 2018/3/24.
 */
class VocApplication : Application {

    private val TAG = this.javaClass.simpleName
    var message_flag = false //false  从userFragment 提现按钮跳转,true 从提现页面 判断是否设置过支付密码跳转
    var pwd1 = ""
    var pwd2 = ""
    var isResetPwd = false
    var istitle = false
    var option : SmAntiFraud.SmOption? = null
    constructor() {
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: VocApplication
        fun getInstance(): VocApplication {
            return sInstance
        }
    }
    /**
     *  取得当前进程名
     *  @param context
     *  @return      */
    fun getCurProcessName(context: Context): String? {
        val pid = android.os.Process.myPid()
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid === pid) {
                return appProcess.processName
            }
        }
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        CrashReport.initCrashReport(applicationContext, "注册时申请的APPID", false)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "")

        /**
        *《---------------------------------------------==- 数美代码 -==---------------------------------------------》
        */
//             如果 AndroidManifest.xml 中没有指定主进程名字，主进程名默认与 packagename 相同
        if (getCurProcessName(this).equals(this.getPackageName())) {
            option = SmAntiFraud.SmOption()
        }
        /**
        *《---------------------------------------------==- 极光推送 -==---------------------------------------------》
        */
        setDebugMode(true)
        init(this)
        var DEBUG_ORG : String= "IpY1WdrvDKXFcTL80wcH"
        // organization 代码 不要传 AccessKey
        option!!.setOrganization(DEBUG_ORG)
        option!!.setChannel("Voc")
        //渠道代码
        SmAntiFraud.create(this, option)
//        val id = SmAntiFraud.getDeviceId()
//        Log.i("log", "$id====================")


        /**
        *《---------------------------------------------==- 7.0 新特性 -==---------------------------------------------》
        */
        if (Build.VERSION.SDK_INT >= 25) {
            val systemService = getSystemService(ShortcutManager::class.java)
            val intent = Intent(this, VoiceActivityNew::class.java)
            intent.action = Intent.ACTION_VIEW
            val intent2 = Intent(this, TaskActivity::class.java)
            intent2.action = Intent.ACTION_VIEW
            val intent3 = Intent(this, WalletActivity::class.java)
            intent3.action = Intent.ACTION_VIEW
            val build = ShortcutInfo.Builder(this, "id" + 1)
                    .setShortLabel("我的资产")
                    .setLongLabel("我的资产")
                    .setIcon(Icon.createWithResource(this, R.mipmap.kjfs_three))
                    .setIntent(intent3)
                    .build()
            val build2 = ShortcutInfo.Builder(this, "id" + 2)
                    .setShortLabel("任务挖矿")
                    .setLongLabel("任务挖矿")
                    .setIcon(Icon.createWithResource(this,  R.mipmap.kjfs_two))
                    .setIntent(intent2)
                    .build()
            val build3 = ShortcutInfo.Builder(this, "id" + 3)
                    .setShortLabel("语音挖币")
                    .setLongLabel("语音挖币")
                    .setIcon(Icon.createWithResource(this, R.mipmap.kjfs_one))
                    .setIntent(intent)
                    .build()
            val mList = ArrayList<ShortcutInfo>()
            mList.add(build)
            mList.add(build2)
            mList.add(build3)
            systemService!!.dynamicShortcuts = mList
        }
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setDebugMode(false)
        Logger.addLogAdapter(AndroidLogAdapter())
        OkGo.getInstance().init(this)
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误

                    //如果使用默认的 60秒,以下三行也不需要传

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

            //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
            //      .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）

            //可以设置https的证书,以下几种方案根据需要自己设置
            //      .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
            //      .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
            //              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            //      .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
            //      .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
            //      .addInterceptor(new Interceptor() {
            //            @Override
            //            public Response intercept(Chain chain) throws IOException {
            //                 return chain.proceed(chain.request());
            //            }
            //       })

            //这两行同上，不需要就不要加入

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}