package voc.cn.cnvoccoin.dialog

import android.Manifest
import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_login.*
import kotlinx.android.synthetic.main.dialog_login.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.activity.*
import voc.cn.cnvoccoin.util.*


/**
 * Created by shy on 2018/3/27.
 */
const val REGISTER: Int = 0
const val LOGIN: Int = 1

class LoginDialog : Dialog {
    val mContext: Context
    var flag: Int
    var mTvMobile: TextView? = null
    var mTvEmail: TextView? = null

    constructor(mContext: Context, flag: Int) : super(mContext, R.style.TradeDialog) {
        this.mContext = mContext
        this.flag = flag
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }

    private fun initView() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null)
        setContentView(view)
//        SystemUIUtils.setStickFullScreen(window.decorView)

        view.tv_close.setOnClickListener { dismiss() }
        mTvMobile = view.tv_mobile_dialog as TextView
        mTvEmail = view.tv_email_dialog as TextView

        when (flag) {
            REGISTER -> {
                setCanceledOnTouchOutside(false)
                setCancelable(false)
                view.tv_seven.text = mContext.resources.getString(R.string.seven_register)
                view.tv_copy.visibility = View.GONE
                view.tv_invite.visibility = View.GONE
                view.tv_userid.visibility = View.GONE
            }
            LOGIN -> {
                setCanceledOnTouchOutside(true)
                setCancelable(true)
                view.tv_mobile_dialog.visibility = View.GONE
                view.tv_email_dialog.visibility = View.GONE
                view.tv_userid.text = String.format(mContext.resources.getString(R.string.login_id), PreferenceUtil.instance?.getString(USER_NAME))
                view.tv_userid.visibility = View.VISIBLE
                tv_seven.visibility = View.GONE
                view.tv_invite.visibility = View.VISIBLE
                view.tv_copy.visibility = View.VISIBLE
                view.iv_group.visibility = View.VISIBLE
                view.tv_copy_into.visibility = View.VISIBLE
                view.tv_common.visibility = View.VISIBLE
                val id = PreferenceUtil.instance?.getInt(USER_ID)
                if (id != null) {
                    view.tv_id.visibility = View.VISIBLE
                    view.tv_id.text = "ID:$id"
                    view.tv_invite.text = String.format(mContext.resources.getString(R.string.invite_code, id))
                }

            }
        }

        mTvMobile?.setOnClickListener { gotoBind(MOBILE_BIND) }
        mTvEmail?.setOnClickListener { gotoBind(EMAIL_BIND) }
        view.tv_copy.setOnClickListener { copyId() }
        view.tv_copy_into.setOnClickListener { copyImage() }
    }

    private fun copyImage() {
        if (mContext is MainActivity){
            if(mContext.permissinTag){
                val res = mContext.resources
                val bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_group)
                Utils.saveImageToGallery(mContext, bmp)
            }else{
                ToastUtil.showToast("权限未开启，请开启应用的存储权限")
            }
        }

    }

    private fun copyId() {
        val cm: ClipboardManager = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.text = String.format(mContext.getString(R.string.invite_text),PreferenceUtil.instance?.getInt(USER_ID))
        ToastUtil.showToast("复制成功!")
    }

    private fun gotoBind(flag: Int) {
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra(LOGIN_BIND, flag)
        mContext.startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onNumberEvent(event: LoginEvent) {
//        if (event.number.isNotEmpty()) {
//            mTvMobile?.text = event.number
//            PreferenceUtil.instance?.set(USER_NAME, event.number)
        dismiss()
        /*   if (mTvMobile?.text.toString().isNotEmpty() || mTvEmail?.text.toString().isNotEmpty()) {
               ToastUtil.showToast("绑定成功")

           }*/
//        }
    }

}