package voc.cn.cnvoccoin.activity

import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_join_community.*
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.Constant.IS_GRANTED_PERMISSION
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*
import android.R.array
import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import voc.cn.cnvoccoin.entity.CommunityShequModel
import voc.cn.cnvoccoin.entity.communityModel


/**
 * Created by shy on 2018/5/8.
 */
open class CommnutityActivity:BaseActivity() {
    var tag:String = ""
    var imgId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        setParams()
        initView()

        if (tag == JOIN_COMMUNTITY){
            getPicUrls()
        }
    }

    open fun setParams() {
        tag = JOIN_COMMUNTITY
        imgId = R.mipmap.ic_group
    }


    open fun getLayoutResId():Int{
        return R.layout.activity_join_community
    }

    private fun initView() {
        tv_copy_into.setOnClickListener { copyImage() }
        tv_confirm.setOnClickListener { confirmTask() }
        iv_back.setOnClickListener { finish() }
    }

    private fun confirmTask() {
        val yanzhengma = et_yanzhengma.text.toString()
        if(yanzhengma.isEmpty())return
        val request = TaskRequest(yanzhengma, tag)
        val wrapper = RequestBodyWrapper(request)
                        HttpManager.post(MAKE_TASK,wrapper).subscribe(object :Subscriber<String>{
                    override fun onNext(t: String?) {
                        if(t == null || t.isEmpty())return
                        val gson = Gson()
                        val model = gson.fromJson(t, ResBaseModel::class.java) ?: return
                        if(model.code == 1){
                    ToastUtil.showToast(model.msg)
                }

            }

            override fun onError(t: Throwable?) {

            }

            override fun onComplete() {
            }

        })
    }

    private fun copyImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PreferenceUtil.instance?.getBoolean(IS_GRANTED_PERMISSION, false) == true) {
                val res = resources
                val bmp = BitmapFactory.decodeResource(res, imgId)
                Utils.saveImageToGallery(this, bmp)
            } else {
                ToastUtil.showToast("权限未开启，请开启应用的存储权限")
            }
        }else{
            val res = resources
            val bmp = BitmapFactory.decodeResource(res, imgId)
            Utils.saveImageToGallery(this, bmp)
        }
    }
    private fun getPicUrls() {
        // 请求公众号图片地址
        // SHEQU_PICS  /api/user/public/getCodeArr
        HttpManager.get(SHEQU_PICS).subscribe(object :Subscriber<String>{
            override fun onComplete() {

            }
            override fun onError(t: Throwable?) {
//                ToastUtil.showToast("")

            }

            override fun onNext(t: String?) {
                if(t == null || t.isEmpty())return
                val gson = Gson()
                val model = gson.fromJson(t, CommunityShequModel::class.java) ?: return
                if(model.data == null || model.data.isEmpty())return
                if(model.code == 1){
                    val picUrl = model.data[0]

                    Glide.with(Activity())
                            .load(picUrl)
                            .into(findViewById(R.id.iv_img))
                }
            }


        })
    }
}