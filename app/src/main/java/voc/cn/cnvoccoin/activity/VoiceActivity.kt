package voc.cn.cnvoccoin.activity

import android.media.MediaRecorder
import android.net.LocalServerSocket
import android.net.LocalSocket
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
import voc.cn.cnvoccoin.util.ToastUtil
import voc.cn.cnvoccoin.util.UPLOAD_COIN
import voc.cn.cnvoccoin.util.UploadCoinRequest
import java.io.*
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

    private var mediarecorder: MediaRecorder? = null
    private val sender: LocalSocket? = null
    private val received: LocalSocket? = null
    private val lss: LocalServerSocket? = null
    private val BUFFER_SIZE = 5000
    private val inputStream: InputStream? = null
    private var recoder: Recoder? = null
    private var db: Double = 0.0  // 分贝


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_voice)
        initView()
        getReadCoin()
        val mRecorders = MediaRecorder()
        mRecorders.setAudioSource(MediaRecorder.AudioSource.MIC)



    }

    private fun initView() {


        iv_voice?.setOnTouchListener { v, event ->
            var startY:Float = 0f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                    oldTime = System.currentTimeMillis()
                    view_wave.startAnim()

                    // 开始录音
                    recoder = Recoder()
                    recoder!!.start()

                    true
                }
                MotionEvent.ACTION_UP -> {

                    //这是检测分贝的，不用就可以删除
                    mediarecorder = MediaRecorder()
                    val ratio = mediarecorder!!. getMaxAmplitude () / BASE;
                    db = 0.0;// 分贝
                    if (db < 1)
                        db = 20 * Math.log10(ratio.toDouble());

                    // 结束录音
                    if (mediarecorder != null) {

                        //停止录音的
                        mediarecorder.run {
                            this!!.stop()
                            release()
                            mediarecorder = null
                        }
                    }

                    val endY = event.y
                    view_wave.stopAnim()
                    newTime = System.currentTimeMillis()

                    if(endY -startY < -1000){
                        ToastUtil.showToast("已取消")
                    }else if (newTime - oldTime < 1000) {
                        //这里可以做人声判断
                        ToastUtil.showToast("录音时间过短")
                    }
                    else if(db<-20){ // 检测音量
                        ToastUtil.showToast("再大声一些哦~"+db)
                    }
                    else{
                        getReadCoin()
                    }
                    true
//                    else {
//                    }

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
            }

        }, UploadVoiceBean::class.java, ResBaseModel::class.java)
    }

    //文件转流
    @Throws(Exception::class)
    fun readStream(): ByteArray {
        val fs = FileInputStream("/sdcard/today.aac")
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = fs.read(buffer)

        while (-1 !=len) {
            outStream.write(buffer, 0, len);
        }

        outStream.close()
        fs.close()

        return outStream.toByteArray()

    }

    //调用录音机，存储录音文件
    internal inner class Recoder : Thread() {
        override fun run() {
            // TODO Auto-generated method stub
            super.run()
            val file = File("/sdcard/today.aac")
            mediarecorder = MediaRecorder()
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            if (mediarecorder == null) {
                mediarecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediarecorder!!.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                mediarecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mediarecorder!!.setOutputFile(file.absolutePath)
            }
            try {
                mediarecorder!!.prepare()
            } catch (e: IllegalStateException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            mediarecorder!!.start()
        }
    }

}