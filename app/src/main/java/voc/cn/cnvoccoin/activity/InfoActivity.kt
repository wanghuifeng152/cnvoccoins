package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_info.*
import voc.cn.cnvoccoin.R

/**
 * Created by shy on 2018/3/27.
 */
class InfoActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener { finish() }

    }
}