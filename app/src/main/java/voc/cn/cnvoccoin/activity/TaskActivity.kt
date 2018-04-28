package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_task.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.adapter.BasicAdapter

/**
 * Created by shy on 2018/4/28.
 */
class TaskActivity:BaseActivity() {
    var basicImagmgList = arrayListOf<Int>(R.mipmap.task_uninvite,R.mipmap.task_unjoin,R.mipmap.task_unfocus,R.mipmap.task_unlogin,R.mipmap.task_unreload)
    var superImagmgList = arrayListOf<Int>(R.mipmap.task_unrank,R.mipmap.task_unvoice)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        initView()
    }

    private fun initView() {
        iv_back.setOnClickListener { finish() }
        rv_basic_task.layoutManager = GridLayoutManager(this,3)
        rv_super_task.layoutManager = GridLayoutManager(this,3)

        val basicAdapter = BasicAdapter(this,basicImagmgList)
        val superAdapter = BasicAdapter(this,superImagmgList)
        rv_basic_task.adapter = basicAdapter
        rv_super_task.adapter = superAdapter
    }
}