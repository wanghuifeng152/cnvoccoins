package voc.cn.cnvoccoin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import org.reactivestreams.Subscription
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.adapter.RankAdapter
import voc.cn.cnvoccoin.entity.DataBean
import voc.cn.cnvoccoin.entity.RankResModel
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.UrlConstants.RANK_URL
import voc.cn.cnvoccoin.util.YHLog

/**
 * Created by shy on 2018/3/24.
 */
class HomeFragment : Fragment() {
    var mRvRank: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        mRvRank = view?.findViewById<RecyclerView>(R.id.rv_rank)
    }

    override fun onResume() {
        super.onResume()
        getRank()
        getMyRank()
    }

    private fun getMyRank() {

    }

    private fun getRank() {
        val hashMap = hashMapOf<String, String>()
        HttpManager.get(RANK_URL,hashMap).subscribe(object : Subscriber<String> {
            override fun onComplete() {

            }

            override fun onNext(s: String?) {
                YHLog.d("rank_url", s)
                if (s == null) return
                val gson = Gson()
                var model: RankResModel = gson.fromJson(s, RankResModel::class.java) ?: return
                if (model.code == 1) {
                    if (model.data.isEmpty()) return
                    setRankModel(model.data)
                }

            }

            override fun onError(t: Throwable?) {
            }
        })
    }

    private fun setRankModel(data: List<DataBean>) {
        mRvRank?.layoutManager = LinearLayoutManager(activity)
        var adapter = RankAdapter(activity, data)
        mRvRank?.adapter = adapter

    }


}