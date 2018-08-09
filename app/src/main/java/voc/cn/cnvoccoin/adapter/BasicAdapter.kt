package voc.cn.cnvoccoin.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.alibaba.security.rp.RPSDK
import com.google.gson.Gson
import org.json.JSONObject
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.R.id.btn_focus
import voc.cn.cnvoccoin.R.id.iv_img
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.entity.Realname
import voc.cn.cnvoccoin.entity.TaskEntity
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

class BasicAdapter(var mContext: Context, var data: ArrayList<Int>, var tag: Int) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    var jqString : String ?= null

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.mImg?.setImageResource(data[position])
        if (onClick != null)
            onClick!!.OnClickItem(holder?.mImg!!,position)
        HttpManager.get(GET_TASK).subscribe(object : Subscriber<String> {
            override fun onNext(t: String?) {
                val gson : TaskEntity? = Gson().fromJson(t, TaskEntity::class.java)
                if (gson!!.code == 1){
                    jqString = gson.data.get(0).string
                }
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        })
        holder?.mImg?.setOnClickListener {
            when (tag) {
                BASIC_TASK -> {
                    when (position) {
                        0 -> {
//                            mContext.startActivity(Intent(mContext, InvitationActivity::class.java))

//                            //获取剪贴板管理器：
                            val cm = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                            // 创建普通字符型ClipData
                            val uid = PreferenceUtil.instance?.getInt(USER_ID)
////                            val uid = "haha"
                            Log.d("9999999999999","uuuuuu"+uid)
                            var mClipData : ClipData ? = null
                            if (jqString != null && TextUtils.isEmpty(jqString)) {
                                mClipData = ClipData.newPlainText("Label", jqString)
                            }else{
                                mClipData = ClipData.newPlainText("Label", mContext.resources.getString(R.string.str_yaoqing) + uid)
                            }
                            // 将ClipData内容放到系统剪贴板里。
                            cm.primaryClip = mClipData
                            ToastUtil.showToast("邀请链接已复制到剪贴板\n快去分享给你的朋友吧~")
                        }
                        1 -> {

                            postrealnam(mContext);
                        }
                        2 -> {
                            mContext.startActivity(Intent(mContext, CommnutityActivity::class.java))
                        }
                        3 -> {
                            mContext.startActivity(Intent(mContext, FocusOfficalActivity::class.java))
                        }
                        4 -> {
                            val token = PreferenceUtil.instance?.getString(TOKEN)
                            if (token == null || token.isEmpty()) {
                                mContext.startActivity(Intent(mContext, LoginActivityNew::class.java))
                            } else {
                                ToastUtil.showToast("您已登录~")
                            }
                        }
                    }
                }
                SUPER_TASK -> {
                    when (position) {
                        0 -> {
                            val token = PreferenceUtil.instance?.getString(TOKEN)
                            if (token == null || token.isEmpty()) {
                                mContext.startActivity(Intent(mContext, LoginActivityNew::class.java))
                            } else {
                                mContext.startActivity(Intent(mContext, VoiceActivityNew::class.java))
                            }
                        }
                        1 -> {
                            val token = PreferenceUtil.instance?.getString(TOKEN)
                            if (token == null || token.isEmpty()) {
                                mContext.startActivity(Intent(mContext, LoginActivityNew::class.java))
                            } else {
                                mContext.startActivity(Intent(mContext, VoiceActivityNew::class.java))
                            }

                        }
                    }

                }
            }
        }
    }

    interface OnClicks {
        fun OnClickItem(v: View, position: Int)
    }

    private var onClick: OnClicks? = null

    fun setOnClick(onClick: OnClicks) {
        this.onClick = onClick
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var mImg: ImageView?

        constructor(itemView: View?) : super(itemView) {
            mImg = itemView?.findViewById<ImageView>(R.id.iv_img)
        }
    }

    //获取实名认证Token
    private fun postrealnam(mContext: Context){
        val list = list("1")
        val wrapper = RequestBodyWrapper(list)
        HttpManager.post(POST_REALNAME, wrapper).subscribe(object : Subscriber<String> {
            override fun onNext(t: String?) {
                Log.e("Tag","onNext----------------->"+t.toString())

                val realname = Gson().fromJson<Realname>(t, Realname::class.java!!)
                val token = realname.getData().getToken()
                //实人认证代码
                RPSDK.start(token, mContext,
                        object : RPSDK.RPCompletedListener {
                            override fun onAuditResult(audit: RPSDK.AUDIT) {

                                if (audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过

                                    //进行绑定账号和身份接口
                                    postrealname(mContext);
                                }
                                else if (audit == RPSDK.AUDIT.AUDIT_FAIL) {
                                    //认证不通过
                                    CacheActivity.finishActivity()

                                } else if (audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。

                                } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                                    CacheActivity.finishActivity()
                                } else if (audit == RPSDK.AUDIT.AUDIT_EXCEPTION) { //系统异常
                                    CacheActivity.finishActivity()
                                }
                            }
                        })
            }

            override fun onError(t: Throwable?) {

            }

            override fun onComplete() {

            }

        })
    }

    //和账户进行身份证绑定
    private fun postrealname(mContext: Context){
        val list = list("1")
        val wrapper = RequestBodyWrapper(list)
        HttpManager.post(POST_REALNAME_one,wrapper).subscribe(object :Subscriber<String>{
            override fun onNext(t: String?) {
                if (t == null || t.isEmpty()) return
                var jsonObject: JSONObject? = null

                jsonObject = JSONObject(t)
                val code = jsonObject!!.getInt("code")
                val msg = jsonObject.getString("msg")
                if (code == 1) {
                    Toast.makeText(mContext, "实名认证任务已完成，获得287voc", Toast.LENGTH_SHORT).show()
                    var intents = Intent(mContext, ForwardActivity::class.java)
                    mContext.startActivity(intents)
                }else{
                    Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onError(t: Throwable?) {

            }

            override fun onComplete() {

            }
        })

    }
}





