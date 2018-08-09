package voc.cn.cnvoccoin.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.alibaba.security.rp.RPSDK
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.R.id.*
import voc.cn.cnvoccoin.VocApplication
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.entity.MyCoinResponse
import voc.cn.cnvoccoin.entity.TaskEntity
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

import kotlinx.android.synthetic.main.fragment_user.*
import voc.cn.cnvoccoin.entity.Realname
import voc.cn.cnvoccoin.entity.Realname_one
import java.net.URLConnection


/**
 * Created by shy on 2018/3/24.
 */
class UserFragment : Fragment() {
//    internal var isTask = false //false为 实名认证，  true为任务里面的实名认证
    private var jqString : String? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_user, null)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //点击更多按钮跳转到更多页面
        tv_more.setOnClickListener {
            processBasr.setVisibility(View.VISIBLE)
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                Logger.t("token").e(token + "")
                processBasr.setVisibility(View.GONE)
                //如果没有登录跳转到登录页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {
                processBasr.setVisibility(View.GONE)
                //已经登录跳转到更多页面
                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        //点击底部图片跳转到官网
        jump_img.setOnClickListener({

            processBasr.setVisibility(View.VISIBLE)
            val token = PreferenceUtil!!.instance?.getString(TOKEN)
            if (token == null){

                processBasr.setVisibility(View.GONE)
                //没有登录将跳转到登录页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{

                processBasr.setVisibility(View.GONE)
                //已经登录跳转到官网
                startActivity(Intent(activity,VocOfficialActivity::class.java))
            }
        })
        //点击体现按钮
        tv_se!!.setOnClickListener({

            processBasr.setVisibility(View.VISIBLE)
            val token = PreferenceUtil!!.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {

                processBasr.setVisibility(View.GONE)
                //没有登录跳转到登录页面
                startActivity(Intent(activity, LoginActivityNew::class.java))
            } else {

                //已登录跳转到提现页面
//                val qianbaoflag = PreferenceUtil!!.instance?.getString("flag")
//                if(qianbaoflag ==null){
//                    VocApplication.getInstance().message_flag = false;
//                    startActivity(Intent(activity, MessageCodeActivity::class.java))
//                }else{
                    startActivity(Intent(activity, WalletActivity::class.java))
                processBasr.setVisibility(View.GONE)
//                }
            }


            }

        )
            //点击登录按钮
        tv_notlogin.setOnClickListener { startActivity(Intent(activity, LoginActivityNew::class.java)) }
        //未登陆，点击登陆后可见，跳转登陆
        tv_see.setOnClickListener {
            startActivity(Intent(activity, LoginActivityNew::class.java))
        }
        //点击头像
        iv_header.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }
        }
        //点击设置
      tv_setting.setOnClickListener({
      startActivity(Intent(activity, SettingActivity::class.java))

      })


        //点击实名认证
        rl_real_name.setOnClickListener({
//            isTask = false
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{
                rl_real_name.setClickable(false)
                postrealnam()
            }


        })

        //点击邀请好友图片
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
                var mClipData : ClipData ? = null
                if (jqString != null && !TextUtils.isEmpty(jqString)) {
                    mClipData = ClipData.newPlainText("Label", jqString)
                }else{
                    mClipData = ClipData.newPlainText("Label", activity.resources.getString(R.string.str_yaoqing)+uid)
                }
                // 将ClipData内容放到系统剪贴板里。
                cm.primaryClip = mClipData
                ToastUtil.showToast("邀请链接已复制到剪贴板\n快去分享给你的朋友吧~")
//                startActivity(Intent(activity, TaskActivity::class.java))
            }
        }
        //点击加入社区
        btn_join.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                activity.startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{
                startActivity(Intent(activity, CommnutityActivity::class.java))
            }
        }
        //点击任务实名认证
        btn_focus.setOnClickListener {
//            isTask  = true
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                activity.startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{
                postrealnam()
//                startActivity(Intent(activity, FocusOfficalActivity::class.java))
            }
        }
/**
*《---------------------------------------------==- 基础任务 -==---------------------------------------------》
*/

    }
    private fun getTask(){
        HttpManager.get(GET_TASK).subscribe(object : Subscriber<String>{
            override fun onError(t: Throwable?) {
                Log.e("ssss",t!!.message)
            }
            override fun onComplete() {
            }
            override fun onNext(t: String?) {
                PreferenceUtil.instance?.set("strTask", t)
                val gson : TaskEntity? = Gson().fromJson(t, TaskEntity::class.java)
                if (gson!!.code == 1) {
                    val data = gson.data
                    for (datum in data) {
                        if (datum.task == "加入群组"){
                            if (datum.taskStatus == 1) {
                                btn_join.setImageResource(R.mipmap.task_unjoin2_true)
                                btn_join.isEnabled = false
                            }else{
                                btn_join.setImageResource(R.mipmap.task_unjoin2)
                                btn_join.isEnabled = true
                            }
                            jqString = datum.string
                        }
                        if (datum.task == "实名认证"){
                            if (datum.taskStatus == 1) {
                                btn_focus.setImageResource(R.mipmap.task_authentication_ok)
                                btn_focus.isEnabled = false
                            }else{
                                btn_focus.setImageResource(R.mipmap.task_authentication)
                                btn_focus.isEnabled = true
                            }
                            jqString = datum.string
                        }
                    }
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        Log.e("aa","onResume")
        isLogin()
        getTask()
        getCoin()
    }

    /**
     * 是否登录
     */
    private fun isLogin(){
        val token = PreferenceUtil.instance?.getString(TOKEN)
        if (token == null || token.isEmpty()) {
            tv_notlogin.visibility = View.VISIBLE
            tv_se.visibility = View.GONE
            tv_see.visibility = View.VISIBLE
            tv_my_coin?.text = "*******"
        }else{
            if (tv_notlogin == null)
            {
            }
            else
            {
                tv_notlogin.visibility = View.GONE
            }

            tv_se.visibility = View.VISIBLE
            tv_see.visibility = View.GONE
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
                isLogin()


            }

        }, MyCoinResponse::class.java, ResBaseModel::class.java)
    }


//和账户进行身份证绑定
    private fun postrealname(){
    processBasr.setVisibility(View.VISIBLE)
        val list = list("1")
        val wrapper = RequestBodyWrapper(list)
        HttpManager.post(POST_REALNAME_one,wrapper).subscribe(object :Subscriber<String>{
            override fun onNext(t: String?) {
                processBasr.setVisibility(View.GONE)
                if (t == null || t.isEmpty()) return
                var jsonObject: JSONObject? = null

                jsonObject = JSONObject(t)
                val code = jsonObject!!.getInt("code")
                val msg = jsonObject.getString("msg")
                Toast.makeText(activity, "实名认证任务已完成，获得287voc", Toast.LENGTH_SHORT).show()

                getTask()
//                if (code == 1) {
//                    var intents = Intent(activity, ForwardActivity::class.java)
//                    startActivity(intents)
//                }else{
                    Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()
//                }

            }

            override fun onError(t: Throwable?) {
                processBasr.setVisibility(View.GONE)
            }

            override fun onComplete() {
                processBasr.setVisibility(View.GONE)
            }
        })

    }

//获取实名认证Token
    private fun postrealnam(){
    processBasr.setVisibility(View.VISIBLE)
    btn_focus.setClickable(false)
        val list = list("1")
        val wrapper = RequestBodyWrapper(list)
        HttpManager.post(POST_REALNAME, wrapper).subscribe(object : Subscriber<String> {
            override fun onNext(t: String?) {
                processBasr.setVisibility(View.GONE)
                btn_focus.setClickable(true)
                rl_real_name.setClickable(true)
                Log.e("Tag","onNext----------------->"+t.toString())
//                Toast.makeText(activity, "成功", Toast.LENGTH_SHORT).show()
                val realname = Gson().fromJson<Realname>(t, Realname::class.java!!)
                val token = realname.getData().getToken()
                //实人认证代码
                RPSDK.start(token, activity,
                        object : RPSDK.RPCompletedListener {
                            override fun onAuditResult(audit: RPSDK.AUDIT) {

                                if (audit == RPSDK.AUDIT.AUDIT_PASS) {
                                    //认证通过
                                    //进行绑定账号和身份接口
                                    postrealname()
                                }
                                else if (audit == RPSDK.AUDIT.AUDIT_FAIL) {

                                    //认证不通过
                                    CacheActivity.finishActivity()

                                } else if (audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。

                                } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                                    CacheActivity.finishActivity()
                                } else if (audit == RPSDK.AUDIT.AUDIT_EXCEPTION) { //系统异常
                                    CacheActivity.finishActivity()
                                }
                            }
                        })
            }

            override fun onError(t: Throwable?) {
                processBasr.setVisibility(View.GONE)
            }

            override fun onComplete() {
                processBasr.setVisibility(View.GONE)
            }

        })
    }

//    /**
//     * 是否设置过密码
//     */
//    private fun postIsHavePwd() {
//        val request = postId("11")
//        val wrapper = RequestBodyWrapper(request)
//        HttpManager.post(POST_IS_HAVE_PWD, wrapper).subscribe(object : Subscriber<String> {
//
//
//            override fun onNext(s: String?) {
//                if (s == null || s.isEmpty()) return
//                var jsonObject: JSONObject? = null
//                try {
//                    jsonObject = JSONObject(s)
//                    val code = jsonObject.getInt("code")
//                    if (code == 1) {
//                        if (jsonObject.getString("msg") == "还没有支付密码") {
//                            ToastUtil.showToast("您还没有设置支付密码, 请先设置支付密码")
//                            VocApplication.getInstance().message_flag = true
//                            PreferenceUtil.instance!!.set("istitle", "1")
//                            val intent = Intent(activity, MessageCodeActivity::class.java)
//                            startActivity(intent)
//                        } else {
//                            var intents = Intent(activity, ForwardActivity::class.java)
//                            startActivity(intents)
//                        }
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//            }
//
//            override fun onError(t: Throwable) {
//
//            }
//
//            override fun onComplete() {
//
//            }
//        })
//    }
}