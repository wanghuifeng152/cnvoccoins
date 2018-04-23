package voc.cn.cnvoccoin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.entity.DataBean


/**
 * Created by shy on 2018/3/25.
 */
class RankAdapter(var mContext: Context, var data: List<DataBean>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.rank_item, parent, false)
        return RankViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RankViewHolder?, position: Int) {
        data?.let {
            var dataBean = data[position]
            holder?.mTvName?.text = (position + 1).toString()+"."+dataBean.user_nickname
            holder?.mTvVoc?.text = dataBean.voc_coin.toString()
            holder?.mTvVocToday?.text = dataBean.voc_coin.toString()
        }
    }

    class RankViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var mTvName = view?.findViewById<TextView>(R.id.tv_name)
        var mTvVocToday = view?.findViewById<TextView>(R.id.tv_today_voc)
        var mTvVoc = view?.findViewById<TextView>(R.id.tv_voc)

    }

}




