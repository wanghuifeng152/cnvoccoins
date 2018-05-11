package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_voice.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.entity.UploadVoiceBean
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.ToastUtil
import voc.cn.cnvoccoin.util.UPLOAD_COIN
import voc.cn.cnvoccoin.util.UploadCoinRequest

/**
 * Created by shy on 2018/4/28.
 */
class VoiceActivity : BaseActivity() {
    var oldTime: Long = 0
    var newTime: Long = 0
    var voice_id: Int = 0
    var voice_coin: Double = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        initView()
        getReadCoin()
    }

    private fun initView() {
        iv_voice?.setOnTouchListener { v, event ->
            var startY:Float = 0f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                    oldTime = System.currentTimeMillis()
                    view_wave.startAnim()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val endY = event.y
                    view_wave.stopAnim()
                    newTime = System.currentTimeMillis()
                    if(endY -startY < -10){
                        ToastUtil.showToast("已取消")
                    }else if (newTime - oldTime > 1000) {
                        getReadCoin()
                    } else {
                        ToastUtil.showToast("录音时间过短")
                    }

                    true
                }
            }
            true

        }
    }

    private fun getReadCoin() {
        val request = UploadCoinRequest(voice_id.toString())
        val wrapper = RequestBodyWrapper(request)
        HttpManager.post(UPLOAD_COIN, wrapper).subscribe(object : Subscriber<ResBaseModel<UploadVoiceBean>> {
            override fun onNext(model: ResBaseModel<UploadVoiceBean>?) {
                if (model?.data == null || model?.data.next == null) return
                if (model.code != 1) return
                tv_voice_text.text = model.data.next.content
                if (voice_id != 0) {
                    voice_coin += model.data.next.voc_coin.toDouble()
                }
                voice_id = model.data.next.id
                tv_have_coin.text = voice_coin.toString()
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        }, UploadVoiceBean::class.java, ResBaseModel::class.java)
    }
}