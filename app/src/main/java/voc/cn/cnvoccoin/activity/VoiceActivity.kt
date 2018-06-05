package voc.cn.cnvoccoin.activity

import android.content.ServiceConnection
import android.media.MediaRecorder
import android.os.Build.VERSION_CODES.BASE
import android.os.Bundle
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_voice.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.entity.UploadVoiceBean
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.service.RecordingService.onRecord
import voc.cn.cnvoccoin.util.ToastUtil
import voc.cn.cnvoccoin.util.UPLOAD_COIN
import voc.cn.cnvoccoin.util.UploadCoinRequest
import java.math.BigDecimal
import java.text.DecimalFormat
import android.os.IBinder
import android.content.ComponentName
import android.media.midi.MidiReceiver
import voc.cn.cnvoccoin.service.RecordingService


/**
 * Created by shy on 2018/4/28.
 */
class VoiceActivity : BaseActivity() {
    var oldTime: Long = 0
    var newTime: Long = 0
    var voice_id: Int = 0
    var voice_coin: String = "0.00"

    private var mediarecorder: MediaRecorder? = null
    private var db: Double = 0.0  // 分贝
    lateinit var connection:ServiceConnection
    lateinit var recordingService:RecordingService
    private var mideaRecoder:MediaRecorder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        initView()
        getReadCoin()

        connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {}

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                recordingService = service as RecordingService
                mideaRecoder = recordingService.mideaRecoder

            }
        }
    }

    private fun initView() {

        iv_voice?.setOnTouchListener { v, event ->
            var startY: Float = 0f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                    oldTime = System.currentTimeMillis()
                    view_wave.startAnim()
                    onRecord(this, true,connection)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val ratio = mediarecorder!!.getMaxAmplitude() / BASE;
                    db = 0.0;// 分贝
                    if (db < 1)
                        db = 20 * Math.log10(ratio.toDouble());

                    val endY = event.y
                    view_wave.stopAnim()
                    newTime = System.currentTimeMillis()
                    if (endY - startY < -1000) {
                        ToastUtil.showToast("已取消")
                    } else if (newTime - oldTime < 1000) {
                        //这里可以做人声判断
                        ToastUtil.showToast("录音时间过短")
                    }
                    /*    else if(db<-20){ // 检测音量
                            ToastUtil.showToast("再大声一些哦~"+db)
                        }*/
                    else {
                        onRecord(this, false,connection)
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


}