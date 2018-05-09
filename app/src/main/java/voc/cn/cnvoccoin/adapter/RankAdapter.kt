package voc.cn.cnvoccoin.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.entity.RankBean


/**
 * Created by shy on 2018/3/25.
 */
class RankAdapter(var mContext: Context, var data: List<RankBean.DataBean.ListBean>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

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
            holder?.mTvName?.text = dataBean.userAccount
            holder?.mTvVocToday?.text = dataBean.coinSum.toString()
            if (position in 0..2) {
                setRankImage(position,holder)
            }else{
                holder?.mTvRank?.text = (position + 1).toString()
            }
        }
    }

    private fun setRankImage(position: Int, holder: RankViewHolder?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when(position){
                0-> holder?.mTvRank?.background = mContext.getDrawable(R.mipmap.icon_rank_one)
                1-> holder?.mTvRank?.background = mContext.getDrawable(R.mipmap.icon_rank_two)
                2-> holder?.mTvRank?.background = mContext.getDrawable(R.mipmap.icon_rank_three)

            }
        }
    }

    class RankViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var mTvName = view?.findViewById<TextView>(R.id.tv_name)
        var mTvVocToday = view?.findViewById<TextView>(R.id.tv_today_voc)
        var mTvRank = view?.findViewById<TextView>(R.id.tv_rank)

    }

}




