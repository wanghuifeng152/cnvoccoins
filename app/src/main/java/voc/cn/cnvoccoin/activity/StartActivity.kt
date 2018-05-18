package voc.cn.cnvoccoin.activity

import android.content.Intent
import android.os.Bundle
import voc.cn.cnvoccoin.R
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by shy on 2018/5/15.
 */
class StartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        var task = timerTask {
            runOnUiThread {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, 2000)
    }
}