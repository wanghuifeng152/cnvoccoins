package voc.cn.cnvoccoin.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import voc.cn.cnvoccoin.R
import java.util.*
import kotlin.concurrent.timerTask
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import voc.cn.cnvoccoin.util.AppUtils
import java.time.temporal.ValueRange


/**
 * Created by shy on 2018/5/15.
 */
class StartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        var task = timerTask {
            val utils = AppUtils(this@StartActivity);
            //获取每个手机的唯一设备号
            val str = utils.getUniqueID();
            Log.e("TAG",str+"+===1111111111111====");
            runOnUiThread {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                finish();
            }
        }
        val timer = Timer()
        timer.schedule(task, 2000)
    }


}