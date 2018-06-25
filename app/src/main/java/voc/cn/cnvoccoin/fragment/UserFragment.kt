package voc.cn.cnvoccoin.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_user.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.entity.MyCoinResponse
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

/**
 * Created by shy on 2018/3/24.
 */
class UserFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_user, null)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_more.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                Logger.t("token").e(token + "")
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        jump_img.setOnClickListener({
            val token = PreferenceUtil!!.instance?.getString(TOKEN)
            if (token == null){
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{
                startActivity(Intent(activity,VocOfficialActivity::class.java))
            }
        })
        tv_se!!.setOnClickListener({
            val token = PreferenceUtil!!.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                //没有登录跳转到登录页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                //已登录跳转到提现页面
                startActivity(Intent(activity, WalletActivity::class.java))
            }
        })

        tv_notlogin.setOnClickListener { startActivity(Intent(activity, LoginActivityNew::class.java)) }
        iv_header.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }
        }
        invitation.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                //获取剪贴板管理器：
                val cm = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                            // 创建普通字符型ClipData
                val uid = PreferenceUtil.instance?.getInt(USER_ID)
////                            val uid = "haha"
                Log.d("9999999999999", "uuuuuu" + uid)
                val mClipData = ClipData.newPlainText("Label", "2018最落地，区块链+智能语音大数据场景应用。比特币挖矿模式，拒绝传销，拒绝虚拟矿机，社区成员多劳多得。个人语音就能挖矿，打造去中心全球公有语音大数据库。已和国内知名语音识别公司展开合作，市场千亿市值，6月28日上所。注册即送3000VOC，一枚价值0.1元，每天朗读15分钟，轻松获得2000币，价值200元。立刻下载：http://www.vochain.world/user/index/htmlregist.html?invite_code=" + uid)
                // 将ClipData内容放到系统剪贴板里。
                cm.primaryClip = mClipData
                ToastUtil.showToast("邀请链接已复制到剪贴板\n快去分享给你的朋友吧~")
//                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        btn_join.setOnClickListener { startActivity(Intent(activity, CommnutityActivity::class.java)) }
        btn_focus.setOnClickListener { startActivity(Intent(activity, FocusOfficalActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        getCoin()
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token != null) {
            tv_notlogin.visibility = View.GONE
        } else {
            tv_notlogin.visibility = View.VISIBLE
        }
    }

    private fun getCoin() {
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token?.isEmpty()) return
        HttpManager.get(MY_RANK_URL).subscribe(object : Subscriber<ResBaseModel<MyCoinResponse>> {
            override fun onNext(model: ResBaseModel<MyCoinResponse>?) {
                if (model?.data == null) return
                if (model.code == 1) {
                    tv_my_coin?.text = model.data.voc_coin
                    tv_see.visibility = View.INVISIBLE
                }
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        }, MyCoinResponse::class.java, ResBaseModel::class.java)
    }


}