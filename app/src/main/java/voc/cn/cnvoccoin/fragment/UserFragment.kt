package voc.cn.cnvoccoin.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.LoginActivity

/**
 * Created by shy on 2018/3/24.
 */
class UserFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_user, null)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        val ivHeader = view?.findViewById<ImageView>(R.id.iv_header)
        ivHeader?.setOnClickListener { startActivity(Intent(activity,LoginActivityNew::class.java)) }
    }
}