package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_rank.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.adapter.RankAdapter
import voc.cn.cnvoccoin.entity.DataBean
import voc.cn.cnvoccoin.entity.RankResModel
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.UrlConstants
import voc.cn.cnvoccoin.util.YHLog
import voc.cn.cnvoccoin.view.LoadingDialog

/**
 * Created by shy on 2018/3/27.
 */
class RankActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_rank)
        initView()
        getRank()
    }

    /**
     *           YHLog.d("rank_url", s)
    if (s == null) return
    val gson = Gson()
    var model: RankResModel = gson.fromJson(s, RankResModel::class.java) ?: return
    if (model.code == 1) {
    if (model.data.isEmpty()) return
    setRankModel(model.data)
    }
     */
    private fun getRank() {
        val loadingDialog = LoadingDialog(this, "")
        loadingDialog.show()
        HttpManager.get(UrlConstants.RANK_URL).subscribe(object : Subscriber<String> {
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

            override fun onComplete() {
                loadingDialog.dismiss()
            }

        })


    }

    private fun setRankModel(data: List<DataBean>) {
        rv_rank?.layoutManager = LinearLayoutManager(this)
        var adapter = RankAdapter(this, data)
        rv_rank?.adapter = adapter

    }

    private fun initView() {
        iv_back.setOnClickListener { finish() }
    }
}