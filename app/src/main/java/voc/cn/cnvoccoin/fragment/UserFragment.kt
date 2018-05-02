package voc.cn.cnvoccoin.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.LoginActivity
import voc.cn.cnvoccoin.activity.TaskActivity

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
        iv_header.setOnClickListener { startActivity(Intent(activity,LoginActivity::class.java)) }
    }
}