package voc.cn.cnvoccoin.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.adapter.RankAdapter
import voc.cn.cnvoccoin.entity.MyCoinResponse
import voc.cn.cnvoccoin.entity.RankBean
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

/**
 * Created by shy on 2018/3/24.
 */
class HomeFragment : Fragment() {
    var mRvRank: RecyclerView? = null
    var mBtnVoice: Button? = null
    var mBtnTask: Button? = null
    var mMyCoin: TextView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.fragment_home, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        mRvRank = view?.findViewById<RecyclerView>(R.id.rv_rank)
        mBtnVoice = view?.findViewById<Button>(R.id.btn_voice)
        mBtnTask = view?.findViewById<Button>(R.id.btn_task)
        mMyCoin = view?.findViewById<TextView>(R.id.tv_my_coin)
        mBtnVoice?.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                startActivity(Intent(activity, VoiceActivity::class.java))
            }
        }
        mBtnTask?.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        view?.findViewById<TextView>(R.id.tv_info)?.setOnClickListener { startActivity(Intent(activity, InfoActivity::class.java)) }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_news1.setOnClickListener {
            val intent = Intent(activity, NewsActivity::class.java)
            intent.putExtra(TAG, 1)
            startActivity(intent)
        }

        rl_news2.setOnClickListener {
            val intent = Intent(activity, NewsActivity::class.java)
            intent.putExtra(TAG, 2)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getRank()
        getCoin()
    }


    private fun getCoin() {
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token?.isEmpty()) return
        HttpManager.get(MY_RANK_URL).subscribe(object : Subscriber<ResBaseModel<MyCoinResponse>> {
            override fun onNext(model: ResBaseModel<MyCoinResponse>?) {
                if (model?.data == null) return
                if (model.code == 1) {
                    mMyCoin?.text = model.data.voc_coin
                }
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        }, MyCoinResponse::class.java, ResBaseModel::class.java)
    }

    private fun getRank() {
//        val hashMap = hashMapOf<String, String>()
//        hashMap.put("limit",10)
        HttpManager.get(TODAY_RANK).subscribe(object : Subscriber<String> {
            override fun onComplete() {

            }

            override fun onNext(s: String?) {
                YHLog.d("rank_url", s)
                if (s == null) return
                val gson = Gson()
                var model: RankBean = gson.fromJson(s, RankBean::class.java) ?: return
                if (model.code == 1) {
                    if (model.data?.list == null || model.data?.list?.size == 0) return
                    setRankModel(model.data.list)
                }

            }

            override fun onError(t: Throwable?) {
            }
        })
    }

    private fun setRankModel(data: List<RankBean.DataBean.ListBean>) {
        mRvRank?.layoutManager = LinearLayoutManager(activity)
        var adapter = RankAdapter(activity, data)
        mRvRank?.adapter = adapter

    }


}