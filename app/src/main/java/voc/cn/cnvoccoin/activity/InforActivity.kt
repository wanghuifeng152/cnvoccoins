package voc.cn.cnvoccoin.activity

import android.os.Bundle
import voc.cn.cnvoccoin.R
import kotlinx.android.synthetic.main.activity_infor.*
class InforActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infor)
//        iv_back.setOnClickListener(){
//            finish()
//        }
        iv_back.setOnClickListener {
            finish()
        }
    }
}
