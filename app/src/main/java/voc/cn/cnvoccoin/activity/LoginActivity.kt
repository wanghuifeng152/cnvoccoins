package voc.cn.cnvoccoin.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.ishumei.smantifraud.SmAntiFraud
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*

/**
 * Created by shy on 2018/3/28.
 */
const val MOBILE_BIND = 0
const val EMAIL_BIND = 1
const val LOGIN_BIND: String = "LOGIN_BIND"

class LoginActivity : BaseActivity() {
    var flag: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getData()
        initView()

    }

    private fun getData() {
        flag = intent.getIntExtra(LOGIN_BIND, -1)
        when (flag) {
            MOBILE_BIND -> tv_mobile.text = resources.getString(R.string.mobile_input)
            EMAIL_BIND -> tv_mobile.text = resources.getString(R.string.email_input)
        }
    }

    private fun initView() {
        et_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val number = s.toString()
                when (flag) {
                    MOBILE_BIND -> {
                        if (number.length == 11) {
                            btn_ok.isEnabled = Utils.isMobileNum(number)
                        } else {
                            btn_ok.isEnabled = false
                        }
                    }
                    EMAIL_BIND -> {
                        btn_ok.isEnabled = Utils.isEmail(number)
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        btn_ok.setOnClickListener {
            val password = et_password.text.toString()
            val password_two = et_password_two.text.toString()
            if (!password.equals(password_two)) {
                ToastUtil.showToast("两次输入密码不一致")
            } else {
                setRegister()

            }
        }

        ll_title.setOnClickListener { finish() }
    }

    private fun setRegister() {
        val deviceId = SmAntiFraud.getDeviceId()
        val request = RegisterRequest(et_input.text.toString(), et_password.text.toString(), "")
        var wrapper: RequestBodyWrapper = RequestBodyWrapper(request)
        HttpManager.post(URL_REGISTER, wrapper).subscribe(object : Subscriber<String> {
            override fun onNext(s: String?) {
                if (s == null || s.isEmpty()) return
                val jsonObject = JSONObject(s)
                val code = jsonObject.getInt("code")
                if (code == 1) {
//                    ToastUtil.showToast(jsonObject.getString("msg"))
                    PreferenceUtil.instance?.set(USER_NAME, et_input.text.toString())
                    PreferenceUtil.instance?.set(PASSWORD, et_password.text.toString())
                    finish()
                    EventBus.getDefault().post(LoginEvent(flag, et_input.text.toString()))
                }
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }

}