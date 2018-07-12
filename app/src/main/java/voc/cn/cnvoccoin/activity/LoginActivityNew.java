package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishumei.smantifraud.SmAntiFraud;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.ResBaseModel;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest;
import voc.cn.cnvoccoin.util.LoginRequest;
import voc.cn.cnvoccoin.util.LoginResponse;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.SMSLogin;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UploadCoinRequest3;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.view.AsteriskPasswordTransformationMethod;
import voc.cn.cnvoccoin.view.LoadingDialog;

import static voc.cn.cnvoccoin.util.ConstantsKt.PASSWORD;
import static voc.cn.cnvoccoin.util.ConstantsKt.TOKEN;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_ID;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_NAME;

/**
 * Created by Administrator on 2018/5/2.
 */

public class LoginActivityNew extends BaseActivity {

    @BindView(R.id.verification_code_login)
    TextView verificationCodeLogin;
    @BindView(R.id.code_time)
    TextView codeTime;
    private TextView mTvRegist;
    private Button mBtnLogin;
    private EditText mEtPhone;
    private EditText mEtPwd;
    @BindView(R.id.login_delete_name)
    ImageView loginDeleteName;
    @BindView(R.id.login_psw_hide)
    ImageView loginPswHide;
    private boolean isVisible = true;
    private boolean isLoginType = true;
    @BindView(R.id.processBar)
    ProgressBar mProcessBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mEtPhone = findViewById(R.id.et_login_phone);
        mEtPwd = findViewById(R.id.et_login_pwd);
        mEtPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mEtPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        mBtnLogin = findViewById(R.id.btn_commit);
        //左上角返回监听
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //注册监听
        findViewById(R.id.lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistActivity.class);
                startActivity(intent);
            }
        });

        //密码圆点变为*
        mEtPwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        //EditView 监听
        TextChange textChange = new TextChange();
        mEtPhone.addTextChangedListener(textChange);
        mEtPwd.addTextChangedListener(textChange);

    }

    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String phone = mEtPhone.getText().toString();
            String pwd = mEtPwd.getText().toString();
            if (phone.length() == 11) {
                if (pwd.length() >= 6) {
                    //登录监听
                    mBtnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                判断当前登陆类型
                            if (isLoginType)

                            /**
                             * 密码登陆
                             */ {
//                    验证手机号码是否正确
                                if (isMobileNO(mEtPhone.getText().toString().trim())) {
//                        是否输入验证码
                                    if ("".equals(mEtPwd.getText().toString().trim())) {
                                        ToastUtil.showToast("请输入密码");
                                    } else {
                                        getLogin();
                                    }
                                } else {
                                    ToastUtil.showToast("请输入正确手机号码");
                                }
                            } else

                            /**
                             * 验证码登陆
                             */ {
                                //  验证手机号码是否正确
                                if (isMobileNO(mEtPhone.getText().toString().trim())) {
//                        是否输入密码
                                    if ("".equals(mEtPwd.getText().toString().trim())) {
                                        ToastUtil.showToast("请输入验证码");
                                    } else {
                                        SMSLogin();
                                    }
                                } else {
                                    ToastUtil.showToast("请输入正确手机号码");
                                }
                            }
//                getLogin();
                        }
                    });
                    mEtPwd.setOnEditorActionListener(listenerr);
                    mBtnLogin.setSelected(true);
                } else {
                    mBtnLogin.setSelected(false);
                    mBtnLogin.setOnClickListener(null);
                }
            } else {
                mBtnLogin.setSelected(false);
                mBtnLogin.setOnClickListener(null);
            }
        }
    }

    private TextView.OnEditorActionListener listenerr = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //当actionId == XX_SEND 或者 XX_DONE时都触发
            //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
            //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                //处理事件
                getLogin();
            }
            return false;
        }
    };

    /**
     * 《---------------------------------------------==- 验证码登陆 -==---------------------------------------------》
     */
    private void SMSLogin() {
        final String username = mEtPhone.getText().toString();
        final String SMScode = mEtPwd.getText().toString();
        if (username.isEmpty()) return;
        // final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        //loadingDialog.show();
        mProcessBar.setVisibility(View.VISIBLE);

        SMSLogin request = new SMSLogin(username, SMScode, "android");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.SMS_URL_LOGIN, wrapper).subscribe(new Subscriber<ResBaseModel<LoginResponse>>() {

            @Override
            public void onNext(ResBaseModel<LoginResponse> model) {
                mProcessBar.setVisibility(View.GONE);
                if (model == null || model.data == null) return;
                if (model.code != 1) return;
                PreferenceUtil.Companion.getInstance().set(TOKEN, model.data.getToken());
                PreferenceUtil.Companion.getInstance().set(USER_NAME, username);
                PreferenceUtil.Companion.getInstance().set(PASSWORD, SMScode);
                if (model.data.getUser() != null) {
                    PreferenceUtil.Companion.getInstance().set(USER_ID, model.data.getUser().getId());
                }
                ToastUtil.showToast("登录成功");
                //登录成功后跳转到MainActivity页面
                startActivity(new Intent(LoginActivityNew.this, MainActivity.class));
            }

            @Override
            public void onError(Throwable t) {
                ToastUtil.showToast("验证码有误");
                mProcessBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {

            }
        }, LoginResponse.class, ResBaseModel.class);
    }

    private void getLogin() {
        Log.i("SmAntiFraud2", SmAntiFraud.getDeviceId() + "----------------------------");

        final String username = mEtPhone.getText().toString();
        final String password = mEtPwd.getText().toString();
        String deviceId = SmAntiFraud.getDeviceId();
        Log.e("aaaaaaa", deviceId);
        if (username.isEmpty()) return;
        // final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        //loadingDialog.show();


        LoginRequest request = new LoginRequest(username, password, "android");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.URL_LOGIN, wrapper).subscribe(new Subscriber<ResBaseModel<LoginResponse>>() {

            @Override
            public void onNext(ResBaseModel<LoginResponse> model) {
                mProcessBar.setVisibility(View.GONE);
                Log.i("SmAntiFraud", SmAntiFraud.getDeviceId() + "----------------------------");

                if (model == null || model.data == null) return;
                if (model.code != 1) return;
                PreferenceUtil.Companion.getInstance().set(TOKEN, model.data.getToken());
                PreferenceUtil.Companion.getInstance().set(USER_NAME, username);
                PreferenceUtil.Companion.getInstance().set(PASSWORD, password);
                if (model.data.getUser() != null) {
                    PreferenceUtil.Companion.getInstance().set(USER_ID, model.data.getUser().getId());
                }
                ToastUtil.showToast("登录成功");
                //登录成功后跳转到MainActivity页面
                startActivity(new Intent(LoginActivityNew.this, MainActivity.class));
            }

            @Override
            public void onError(Throwable t) {
                mProcessBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {

            }
        }, LoginResponse.class, ResBaseModel.class);
    }

    @OnClick({R.id.login_delete_name, R.id.et_login_pwd, R.id.login_psw_hide, R.id.verification_code_login, R.id.code_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            账号删除按钮
            case R.id.login_delete_name:
                mEtPhone.setText("");
                break;
//                密码是否可见按钮
            case R.id.login_psw_hide:
                if (isVisible) {
                    loginPswHide.setBackgroundResource(R.mipmap.show);
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    if (!"".equals(mEtPwd.getText().toString().trim())) {
                        mEtPwd.setSelection(mEtPwd.getText().length());
                    }
                    isVisible = !isVisible;
                } else {
                    loginPswHide.setBackgroundResource(R.mipmap.visible_true);
                    mEtPwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    if (!"".equals(mEtPwd.getText().toString().trim())) {
                        mEtPwd.setSelection(mEtPwd.getText().length());
                    }
                    isVisible = !isVisible;
                }
                break;
//                切换登陆方式按钮
            case R.id.verification_code_login:
                if (isLoginType) {
                    //验证码登陆
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtPwd.setText("");
                    mEtPwd.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    mEtPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                    loginPswHide.setVisibility(View.GONE);
                    codeTime.setVisibility(View.VISIBLE);
                    codeTime.setText(R.string.get_cofirm_code);
                    verificationCodeLogin.setText(R.string.login_psw);
                    mEtPwd.setHint(R.string.login_psw_hint_code);
                    isLoginType = !isLoginType;
                } else {
//                  密码登陆
                    mEtPwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    mEtPwd.setText("");
                    mEtPwd.setInputType(EditorInfo.IME_ACTION_NONE);
                    mEtPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                    loginPswHide.setVisibility(View.VISIBLE);
                    codeTime.setVisibility(View.GONE);
                    codeTime.setText("");
                    verificationCodeLogin.setText(R.string.login_code);
                    mEtPwd.setHint(R.string.login_psw_hint);
                    isLoginType = !isLoginType;
                }
                break;
//                获取验证码
            case R.id.code_time:
                if (isMobileNO(mEtPhone.getText().toString().trim())) {

                    //获取验证码
                    getMessage();

                } else {
                    ToastUtil.showToast("请输入正确手机号码");
                }
                break;
        }
    }

    /**
     * 取消倒计时
     *
     * @param v
     */
    public void oncancel(View v) {
        timer.cancel();
    }

    /**
     * 开始倒计时
     *
     * @param v
     */
    public void restart(View v) {
        timer.start();
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            codeTime.setEnabled(false);
            codeTime.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            codeTime.setEnabled(true);
            codeTime.setText("获取验证码");
        }
    };

    /**
     * 获取验证码
     */
    public void getMessage() {
        final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        loadingDialog.show();
        GetConfirmCodeRequest request = new GetConfirmCodeRequest(mEtPhone.getText().toString().trim());
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.GET_MESSAGE_CODE, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                loadingDialog.dismiss();
                restart(codeTime);
                ToastUtil.showToast("验证码发送成功");
            }

            @Override
            public void onError(Throwable t) {
                loadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 发送验证码验证
     * //     * @param code
     */
//    public void postMessage(String code){
//        UploadCoinRequest3 request = new UploadCoinRequest3(mEtPhone.getText().toString().trim(),code);
//        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
//        HttpManager.post(UrlConstantsKt.POST_MESSAGE_CODE, wrapper).subscribe(new Subscriber<String>() {
//            @Override
//            public void onNext(String s) {
//                ToastUtil.showToast(s);
//            }
//
//            @Override
//            public void onError(Throwable t) {
////                tv_input.setText("验证码错误,请重试");
////                tv_input.setTextColor(getResources().getColor(R.color.qlColorHqZFUpBg));
//                ToastUtil.showToast("验证码错误，请充实");
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }
    public boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}
