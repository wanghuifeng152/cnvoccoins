package voc.cn.cnvoccoin

import android.app.Application
import com.lzy.okgo.OkGo
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.Logger.addLogAdapter
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode


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
        MobclickAgent.setDebugMode(false)
        Logger.addLogAdapter(AndroidLogAdapter());
        OkGo.getInstance().init(this);
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