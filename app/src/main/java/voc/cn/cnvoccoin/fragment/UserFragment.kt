package voc.cn.cnvoccoin.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.InvitationActivity
import voc.cn.cnvoccoin.activity.LoginActivityNew
import voc.cn.cnvoccoin.activity.TaskActivity
import voc.cn.cnvoccoin.entity.MyCoinResponse
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.MY_RANK_URL
import voc.cn.cnvoccoin.util.PreferenceUtil
import voc.cn.cnvoccoin.util.TOKEN

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

        tv_more.setOnClickListener { startActivity(Intent(activity,TaskActivity::class.java)) }
        tv_notlogin.setOnClickListener { startActivity(Intent(activity, LoginActivityNew::class.java)) }
        iv_header.setOnClickListener { startActivity(Intent(activity, LoginActivityNew::class.java)) }
        invitation.setOnClickListener { startActivity(Intent(activity, InvitationActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        getCoin()
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if(token != null){
            tv_notlogin.visibility = View.GONE
        }else{
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