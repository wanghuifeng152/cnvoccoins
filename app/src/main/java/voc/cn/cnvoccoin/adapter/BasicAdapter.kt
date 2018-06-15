package voc.cn.cnvoccoin.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.util.PreferenceUtil
import voc.cn.cnvoccoin.util.TOKEN
import voc.cn.cnvoccoin.util.ToastUtil
import voc.cn.cnvoccoin.util.USER_ID

class BasicAdapter(var mContext: Context, var data: ArrayList<Int>, var tag: Int) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.mImg?.setImageResource(data[position])
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
                            val mClipData = ClipData.newPlainText("Label", "2018最落地，区块链+智能语音大数据场景应用。比特币挖矿模式，拒绝传销，拒绝虚拟矿机，社区成员多劳多得。个人语音就能挖矿，打造去中心全球公有语音大数据库。已和国内知名语音识别公司展开合作，市场千亿市值，6月28日上所。注册即送3000VOC，一枚价值0.1元，每天朗读15分钟，轻松获得2000币，价值200元。立刻下载：http://www.vochain.world/user/index/htmlregist.html?invite_code="+uid)
                            // 将ClipData内容放到系统剪贴板里。
                            cm.primaryClip = mClipData
                            ToastUtil.showToast("邀请链接已复制到剪贴板\n快去分享给你的朋友吧~")
                        }
                        1 -> {
                            mContext.startActivity(Intent(mContext, CommnutityActivity::class.java))
                        }
                        2 -> {
                            mContext.startActivity(Intent(mContext, FocusOfficalActivity::class.java))
                        }
                        3 -> {
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


    class ViewHolder : RecyclerView.ViewHolder {
        var mImg: ImageView?

        constructor(itemView: View?) : super(itemView) {
            mImg = itemView?.findViewById<ImageView>(R.id.iv_img)
        }
    }


}





