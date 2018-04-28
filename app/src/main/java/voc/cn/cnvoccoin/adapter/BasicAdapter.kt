package voc.cn.cnvoccoin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import voc.cn.cnvoccoin.R

class BasicAdapter(var mContext: Context, var data: ArrayList<Int>) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.mImg?.setImageResource(data[position])
    }


    class ViewHolder :RecyclerView.ViewHolder{
        var mImg:ImageView?
        constructor(itemView: View?) : super(itemView) {
             mImg = itemView?.findViewById<ImageView>(R.id.iv_img)
        }
    }


}
