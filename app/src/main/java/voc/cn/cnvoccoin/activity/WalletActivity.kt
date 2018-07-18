package voc.cn.cnvoccoin.activity

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.pop_filter.view.*
import org.json.JSONException
import org.json.JSONObject
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.VocApplication
import voc.cn.cnvoccoin.entity.PopuWindowClass
import voc.cn.cnvoccoin.entity.WalletClass
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

/**
 * 我的资产
 */
class WalletActivity : Activity() {
    private var voc_coin: Double? = null
    private var use: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
//        val qianbaoflag = PreferenceUtil!!.instance?.getString("money")
//        tv_coin?.text = qianbaoflag
        hideStatusBar()
        iv_back.setOnClickListener { finish() }
        mingxi!!.setOnClickListener({
            startActivity(Intent(this@WalletActivity, DetailedActivity::class.java))
        })
        //提现规则
        tixian_guize!!.setOnClickListener({
            startActivity(Intent(this@WalletActivity, Put_forwardActivity::class.java))
        })
        //锁定弹出PopupWindow
        suo_ding!!.setOnClickListener({
            val popupView = LayoutInflater.from(this).inflate(R.layout.pop_filter, null)
            val popup = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT)
            popup.setBackgroundDrawable(ColorDrawable(0xfffffff))
            popup!!.isFocusable = true
            popup!!.isOutsideTouchable = true   //点击外部popueWindow消失
            popup!!.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            backgroundAlpha(0.3f)
            popupView.guanbi!!.setOnClickListener({
                backgroundAlpha(1.0f)
                popup!!.dismiss()
            })

            //锁定的PopupWindow
            var requestR: RequestBodyWrapper? = RequestBodyWrapper(null)
            HttpManager.post(SUO_DING, requestR).subscribe(object : Subscriber<String> {
                override fun onComplete() {
                }

                override fun onNext(t: String?) {
                    Log.e("锁定的PopupWindow+++", t)
                    if (t == null) return
                    val gson = Gson()
                    val wallet = gson.fromJson(t, PopuWindowClass::class.java) ?: return

                    if (wallet.code == 1) {
                        val msg = wallet.msg
                        val code = msg.code
                        val codes = msg.codes
                        popupView.wallet_content!!.text = code.toString()
                        popupView.wallet2_content!!.text = codes.toString()

                    }
                }

                override fun onError(t: Throwable?) {
                }
            })
        })

        tv_forward!!.setOnClickListener {
            //            tv_forward.isClickable = false
            postIsHavePwd()
        }
    }

    /**
     * popupWindow设置半透明背景
     * @param bgAlpha 透明值 0.0 - 1.0
     */
    fun backgroundAlpha(bgAlpha: Float) {
        val lp = this.getWindow().getAttributes()
        lp.alpha = bgAlpha
        if (bgAlpha == 1f) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        this.getWindow().setAttributes(lp)
    }
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 24) {
            var window = getWindow()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onResume() {
        getData()
        super.onResume()
    }

    private fun postIsHavePwd() {
        //  val loadingDialog = LoadingDialog(this, null)
        processBasr.setVisibility(View.VISIBLE)
        val request = postId("11")
        val wrapper = RequestBodyWrapper(request)
        HttpManager.post(POST_IS_HAVE_PWD, wrapper).subscribe(object : Subscriber<String> {

            override fun onNext(s: String) {
                processBasr.setVisibility(View.GONE);
                if (s == null || s.isEmpty()) return
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(s)
                    val code = jsonObject.getInt("code")
                    if (code == 1) {
                        if (jsonObject.getString("msg").equals("还没有支付密码")) {
                            VocApplication.getInstance().message_flag = true;
                            startActivity(Intent(this@WalletActivity, MessageCodeActivity::class.java))
                        } else {
//                            跳转到提现
                            var intents = Intent(this@WalletActivity, ForwardActivity::class.java)
                            intents.putExtra("moeny", use);
                            startActivity(intents)
//                            tv_forward.isClickable = true
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onError(t: Throwable) {
                processBasr.setVisibility(View.GONE);
            }

            override fun onComplete() {
                processBasr.setVisibility(View.GONE);
            }
        })
    }

    private fun getData(){

        //可提现，锁定，数据
        /*val request = list("1")
        val wrapper = RequestBodyWrapper(request)*/

        var requestR: RequestBodyWrapper? = RequestBodyWrapper(null)
        HttpManager.post(ZI_CHAN, requestR).subscribe(object : Subscriber<String> {
            override fun onComplete() {
            }

            override fun onNext(t: String?) {
                Log.e("可提现，锁定，数据", t)
                if (t == null) return
                val gson = Gson()
                val wallet = gson.fromJson(t, WalletClass::class.java) ?: return

                if (wallet.code == 1) {
                    val msg = wallet.msg
                    val locking = msg.locking
                    use = msg.use
                    voc_coin = msg.getvoc_coin()
                    wallet_tixian!!.text = use.toString()
                    wallet_suoding!!.text = locking.toString()
                    tv_coin!!.text = voc_coin.toString()
                }
            }

            override fun onError(t: Throwable?) {
            }
        })
    }
}