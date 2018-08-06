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
import com.google.gson.Gson
import com.orhanobut.logger.Logger
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


/**
 * Created by shy on 2018/3/24.
 */
class UserFragment : Fragment() {
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
        //点击重置密码
        rl_reset_pwd.setOnClickListener({
            rl_reset_pwd.isEnabled = false
            postIsHavePwd()

        })

        //点击实名认证
        rl_real_name.setOnClickListener({
            //startActivity(Intent(activity, IdentityActivity::class.java))
            ToastUtil.showToast("正在开发中......")
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
        //点击关注公众号
        btn_real.setOnClickListener {
            val token = PreferenceUtil.instance?.getString(TOKEN)
            if (token == null || token.isEmpty()) {
                activity.startActivity(Intent(activity, LoginActivityNew::class.java))
            }else{
                ToastUtil.showToast("跳转去认证~")
//                startActivity(Intent(activity, FocusOfficalActivity::class.java))
            }
        }

//        //点击关注公众号
//        btn_focus.setOnClickListener {
//            val token = PreferenceUtil.instance?.getString(TOKEN)
//            if (token == null || token.isEmpty()) {
//                activity.startActivity(Intent(activity, LoginActivityNew::class.java))
//            }else{
//                startActivity(Intent(activity, FocusOfficalActivity::class.java))
//            }
//        }
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
                                btn_real.setImageResource(R.mipmap.task_authentication_ok)
                                btn_real.isEnabled = false
                            }else{
                                btn_real.setImageResource(R.mipmap.task_authentication)
                                btn_real.isEnabled = true
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


    private fun postIsHavePwd(){

        processBasr.setVisibility(View.VISIBLE)
        val request = postId("11")
        val wrapper = RequestBodyWrapper(request)
        HttpManager.post(POST_IS_HAVE_PWD, wrapper).subscribe(object : Subscriber<String> {

            override fun onNext(s: String) {

                processBasr.setVisibility(View.GONE)
                if (s == null || s.isEmpty()) return
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(s)
                    val code = jsonObject.getInt("code")
                    if (code == 1) {
                        //没有支付密码跳转到设置密码
                        if (jsonObject.getString("msg").equals("还没有支付密码")){
                            ToastUtil.showToast("您还没有设置支付密码, 请先设置支付密码")
                            VocApplication.getInstance().message_flag = true;
                            startActivity(Intent(activity, MessageCodeActivity::class.java))
                            rl_reset_pwd.isEnabled = true
                        }else{
                            //有设置密码去重置
                            VocApplication.getInstance().isResetPwd = true
                            PreferenceUtil.instance?.set("pwdFlag", "1");//首页重置密码跳转
                            val intent = Intent(activity, GetMessageCodeActivity::class.java)
                            intent.putExtra("isTitle",0);
                            startActivity(intent)
                            rl_reset_pwd.isEnabled = true
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onError(t: Throwable) {

                processBasr.setVisibility(View.GONE)
            }

            override fun onComplete() {

                processBasr.setVisibility(View.GONE)
            }
        })
    }
}