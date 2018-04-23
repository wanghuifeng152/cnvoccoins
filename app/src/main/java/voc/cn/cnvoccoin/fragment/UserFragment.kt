package voc.cn.cnvoccoin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import voc.cn.cnvoccoin.R

/**
 * Created by shy on 2018/3/24.
 */
class UserFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_user,null)
        return view    }
}