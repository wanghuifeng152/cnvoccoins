package voc.cn.cnvoccoin.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.util.PreferenceUtil
import voc.cn.cnvoccoin.util.TOKEN
import voc.cn.cnvoccoin.util.ToastUtil

class BasicAdapter(var mContext: Context, var data: ArrayList<Int>, var tag: Int) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.mImg?.setImageResource(data[position])
            holder?.mImg?.setOnClickListener {
                when(tag){
                    BASIC_TASK -> {
                        when(position){
                            0 -> {mContext.startActivity(Intent(mContext, InvitationActivity::class.java))}
                            1 -> { mContext.startActivity(Intent(mContext, CommnutityActivity::class.java)) }
                            2 -> {mContext.startActivity(Intent(mContext, FocusOfficalActivity::class.java))}
                            3 -> {
                                val token = PreferenceUtil.instance?.getString(TOKEN)
                                if(token == null || token.isEmpty()){
                                    mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                                }else{
                                    ToastUtil.showToast("您已登录~")
                                }
                            }
                        }
                    }
                    SUPER_TASK -> {}
                }
            }
        }


    class ViewHolder :RecyclerView.ViewHolder{
        var mImg:ImageView?
        constructor(itemView: View?) : super(itemView) {
             mImg = itemView?.findViewById<ImageView>(R.id.iv_img)
        }
    }


}
