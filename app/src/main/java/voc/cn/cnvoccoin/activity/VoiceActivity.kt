package voc.cn.cnvoccoin.activity

import android.content.ComponentName
import android.content.ServiceConnection
import android.media.MediaRecorder
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_voice.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.entity.UploadVoiceBean
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.service.RecordingService
import voc.cn.cnvoccoin.service.RecordingService.onRecord
import voc.cn.cnvoccoin.util.ToastUtil
import voc.cn.cnvoccoin.util.UPLOAD_COIN
import voc.cn.cnvoccoin.util.UploadCoinRequest
import java.math.BigDecimal
import java.text.DecimalFormat


/**
 * Created by shy on 2018/4/28.
 */
class VoiceActivity : BaseActivity() {
    var oldTime: Long = 0
    var newTime: Long = 0
    var voice_id: Int = 0
    var voice_coin: String = "0.00"
    var hasVoice: Boolean = false

    private var mideaRecoder: MediaRecorder? = null
    private var db: Double = 0.0  // 分贝

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        initView()
        getReadCoin()
    }


    private fun initView() {

        iv_voice?.setOnTouchListener { v, event ->
            var startY: Float = 0f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                    oldTime = System.currentTimeMillis()
                    view_wave.stopAnim()
                    view_wave.startAnim()
                    onRecord(this, true, connection)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val endY = event.y
                    view_wave.stopAnim()
                    newTime = System.currentTimeMillis()
                    if (endY - startY < -1000) {
                        ToastUtil.showToast("已取消")
                        onRecord(this, false, connection)

                    } else if (newTime - oldTime < 1000) {
                        //这里可以做人声判断
                        ToastUtil.showToast("录音时间过短")
                        onRecord(this, false, connection)
                    } else {
                        onRecord(this, false, connection)
                        getReadCoin()
                    }
                    true

                }
            }
            true

        }
    }

    private fun getReadCoin() {
        /* val loadingDialog = LoadingDialog(this, "")
         loadingDialog.show()*/
        val request = UploadCoinRequest(voice_id.toString())
        val wrapper = RequestBodyWrapper(request)
        HttpManager.post(UPLOAD_COIN, wrapper).subscribe(object : Subscriber<ResBaseModel<UploadVoiceBean>> {
            override fun onNext(model: ResBaseModel<UploadVoiceBean>?) {
                if (model?.data == null || model?.data.next == null) return
                if (model.code != 1) return
                tv_voice_text.text = model.data.next.content
                if (voice_id != 0) {
                    val bigDecimal = BigDecimal(voice_coin)
                    val plus = BigDecimal(model.data.next.voc_coin).plus(bigDecimal)
                    val format = DecimalFormat("#######.##")
                    voice_coin = format.format(plus)
                }
                voice_id = model.data.next.id
                tv_have_coin.text = voice_coin
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
//                loadingDialog.dismiss()
            }

        }, UploadVoiceBean::class.java, ResBaseModel::class.java)
    }

    private var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            ToastUtil.showToast("fail")
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            ToastUtil.showToast("success")
            val binder = service as RecordingService.MyBinder
            var recordingService = binder.service as RecordingService
            mideaRecoder = recordingService.mideaRecoder
        }
    }

    // 检测音量
    fun onAudioDBChanged(db: Int) {

        if (db / 5 > 1) {
            hasVoice = true;
        }

    }
}