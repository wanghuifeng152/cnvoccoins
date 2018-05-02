package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_voice.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.util.ToastUtil

/**
 * Created by shy on 2018/4/28.
 */
class VoiceActivity : BaseActivity() {
    var oldTime:Long = 0
    var newTime:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        initView()
    }

    private fun initView() {
        iv_voice?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldTime = System.currentTimeMillis()
                    view_wave.startAnim()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view_wave.stopAnim()
                    newTime = System.currentTimeMillis()
                    if (newTime - oldTime > 1000) {
//                        setCoin(tv_have_coin.text.length)
                    } else {
                        ToastUtil.showToast("录音时间过短")
                    }

                    true
                }
            }
            true

        }
    }
}