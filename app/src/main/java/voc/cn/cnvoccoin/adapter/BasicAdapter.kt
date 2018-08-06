package voc.cn.cnvoccoin.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.entity.TaskEntity
import voc.cn.cnvoccoin.network.HttpManager
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
                            //实名认证
                            ToastUtil.showToast("跳转去认证~")
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
}





