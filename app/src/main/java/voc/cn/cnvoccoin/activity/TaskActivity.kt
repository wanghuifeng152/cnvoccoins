package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_task.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.adapter.BasicAdapter
import voc.cn.cnvoccoin.entity.TaskEntity
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.GET_TASK


/**
 * Created by shy on 2018/4/28.
 */
const val BASIC_TASK = 0
const val SUPER_TASK = 1
class TaskActivity:BaseActivity() {
    var basicImagmgList = arrayListOf<Int>(R.mipmap.task_uninvite1,R.mipmap.task_unjoin1,R.mipmap.task_unfocus1,R.mipmap.task_login1)
   var superImagmgList = arrayListOf<Int>(R.mipmap.task_unrank1)
    var basicAdapter : BasicAdapter? =null
    var jqString : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        initView()
    }

    private fun initView() {
//        val wrapper = RequestBodyWrapper(null)
        iv_back.setOnClickListener { finish() }
        rv_basic_task.layoutManager = GridLayoutManager(this,3)
        rv_super_task.layoutManager = GridLayoutManager(this,3)
        if (jqString != null && !TextUtils.isEmpty(jqString)){
            basicAdapter = BasicAdapter(this,basicImagmgList, BASIC_TASK)
        }
        basicAdapter = BasicAdapter(this,basicImagmgList, BASIC_TASK)
        val superAdapter = BasicAdapter(this, superImagmgList, SUPER_TASK)
        rv_basic_task.adapter = basicAdapter
        rv_super_task.adapter = superAdapter

    }

    override fun onResume() {
        super.onResume()
        getStatus()
    }

    fun getStatus(){
        processBasr.setVisibility(View.VISIBLE)
        //        更新任务完成度
        HttpManager.get(GET_TASK).subscribe(object : Subscriber<String>{
            override fun onNext(t: String?) {
                val gson : TaskEntity? = Gson().fromJson(t,TaskEntity::class.java)
                if (gson!!.code == 1){
                    processBasr.setVisibility(View.GONE)
                    val data = gson.data
//                    接口回调获取每个ImageView设置是否点击
                    basicAdapter!!.setOnClick(object : BasicAdapter.OnClicks{
                        override fun OnClickItem(v: View, position: Int) {
                            for (datum in data) {
                                if (datum.task == "加入群组") {
                                    if (datum.taskStatus == 1) {
                                        basicImagmgList.set(1, R.mipmap.task_unjoin1_true)
//                                    设置索引为1的ImageViw不可点击
                                        if (position == 1) {
                                            val img: ImageView = v as ImageView
                                            img.isEnabled = false
                                        }
                                    } else {
                                        if (position == 1) {
                                            val img: ImageView = v as ImageView
                                            img.isEnabled = true
                                        }
                                        basicImagmgList.set(1, R.mipmap.task_unjoin1)
                                    }
                                    jqString = datum.string
                                }
                                if (datum.task == "关注公众号") {
                                    if (datum.taskStatus == 1) {
//                                    设置索引为2的ImageView不可点击
                                        if (position == 2) {
                                            val img: ImageView = v as ImageView
                                            img.isEnabled = false
                                        }
                                        basicImagmgList.set(2, R.mipmap.task_unfocus1_true)
                                    } else {
                                        if (position == 2) {
                                            val img: ImageView = v as ImageView
                                            img.isEnabled = true
                                        }
                                        basicImagmgList.set(2, R.mipmap.task_unfocus1)
                                    }
                                    jqString = datum.string
                                }
                            }
                        }
                    })
                    basicAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onError(t: Throwable?) {
                processBasr.setVisibility(View.GONE)
            }

            override fun onComplete() {
            }

        })
    }
}