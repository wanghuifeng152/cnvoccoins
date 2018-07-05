package voc.cn.cnvoccoin.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.orhanobut.logger.Logger
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
import java.text.SimpleDateFormat
import java.util.*


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
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token.isEmpty()){

        }else{
            /**
             * 排行榜定时器，30分钟刷新一次
             */
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    val sharedPreferences = activity.getSharedPreferences("timer", 0)
                    val simpleDateFormatss = SimpleDateFormat("yyyyMMddhhmmss")// HH:mm:ss
                    //获取当前时间
                    val datess = Date(System.currentTimeMillis())

                    if (sharedPreferences.getLong("timeOne",0) == 0L){
                        val simpleDateFormat = SimpleDateFormat("yyyyMMddhhmmss")
                        val date = Date(System.currentTimeMillis())
                        val format = simpleDateFormat.format(date)
                        val time = simpleDateFormat.parse(format).time
//            val sharedPreferences = activity.getSharedPreferences("timer", 0)
//            val edit = sharedPreferences.edit()
                        val edit = sharedPreferences.edit()
                        edit.putLong("timeOne",time)
                        edit.apply()
                        getRank()
                        Log.e("sssss",""+time)
                    }else if (simpleDateFormatss.parse(simpleDateFormatss.format(datess)).time >= (sharedPreferences.getLong("timeOne",0)+(30*60*1000))){
                        val edit = sharedPreferences.edit()
                        edit.putLong("timeOne",simpleDateFormatss.parse(simpleDateFormatss.format(datess)).time)
                        edit.apply()
                        Log.e("ffff",""+simpleDateFormatss.parse(simpleDateFormatss.format(datess)).time)
                        getRank()
                    }
                }
            },30*60*1000,30*60*1000)
        }
        return view
    }

    private fun initView(view: View?) {
        mRvRank = view?.findViewById<RecyclerView>(R.id.rv_rank)
        mBtnVoice = view?.findViewById<Button>(R.id.btn_voice)
        mBtnTask = view?.findViewById<Button>(R.id.btn_task)
        mMyCoin = view?.findViewById<TextView>(R.id.tv_my_coin)
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token.isEmpty()) {
            mMyCoin?.text = "登录/注册后可见"
        }

        //语言挖矿按钮点击判断是否已经登录
        mBtnVoice?.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                //没有登录跳转到登录页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                //已经登录跳转到挖矿页面
                startActivity(Intent(activity, VoiceActivityNew::class.java))
            }
        }
        //任务挖矿点击判断是否已经登录
        mBtnTask?.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                //未登录跳转登陆页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                //已经登录跳转任务挖矿
                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        //资产数目 判断是否登录
        mMyCoin?.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }
        }

//点击跳转到挖币说明
        view?.findViewById<TextView>(R.id.tv_info)?.setOnClickListener { startActivity(Intent(activity, InfoActivity::class.java)) }
    }

    //点击条目跳转
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //点击条目跳转到怎样快速挖币
        rl_news1.setOnClickListener {
            val intent = Intent(activity, NewsActivity::class.java)
            intent.putExtra(TAG, 1)
            startActivity(intent)
        }
//点击跳转到voc语音连
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
        //排行榜
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

                // 未登录的话会走这个方法的
                Logger.t("error").e("" + t!!.message.toString())

            }

            override fun onComplete() {
                //  weidenglu
                val token = PreferenceUtil.instance?.getString(TOKEN)
                if (token == null || token.isEmpty()) {
                    mMyCoin?.text = "登录/注册后可见"
                }

            }

        }, MyCoinResponse::class.java, ResBaseModel::class.java)
    }

    private fun getRank() {
//        val hashMap = hashMapOf<String, String>()
//        hashMap.put("limit",10)
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token?.isEmpty()) return
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
                    Log.e("sxzcxzcz",s)
                }
            }

            override fun onError(t: Throwable?) {
            }
        })
    }

    //RecyclerView适配器 排行榜UI视图名单
    private fun setRankModel(data: List<RankBean.DataBean.ListBean>) {
        mRvRank?.layoutManager = LinearLayoutManager(activity)
        var adapter = RankAdapter(activity, data)
        mRvRank?.adapter = adapter


    }
}