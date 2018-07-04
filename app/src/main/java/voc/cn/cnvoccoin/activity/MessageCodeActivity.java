package voc.cn.cnvoccoin.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import lsp.com.lib.PasswordInputEdt;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UploadCoinRequest3;
import voc.cn.cnvoccoin.util.UrlConstantsKt;


/**
 * 添加地址验证码
 */
public class MessageCodeActivity extends BaseActivity implements View.OnClickListener{
    PasswordInputEdt edt;
    TextView te_number,tv_input,tv_username;
    ImageView iv_back;
    String phone;

    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_code);
        if (!CacheActivity.activityList.contains(MessageCodeActivity.this)) {
            CacheActivity.addActivity(MessageCodeActivity.this);
        }
        intView();
        getMessage();

    }

    public void intView(){
        SharedPreferences sp = getSharedPreferences("voccoin", Activity.MODE_PRIVATE);
        phone = sp.getString("USER_MOBILE","");
        tv_username =findViewById(R.id.tv_username);
        tv_username.setText(phone);
        te_number = findViewById(R.id.te_number);
        te_number.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        restart(te_number);
        iv_back.setOnClickListener(this);
        tv_input = findViewById(R.id.tv_input);
        edt = (PasswordInputEdt) findViewById(R.id.edt);
        edt.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                postMessage(text);
            }
        });
        showKeyboard (edt);
    }
    /**
     * 发送验证码验证
     * @param code
     */
    public void postMessage(String code){
        UploadCoinRequest3 request = new UploadCoinRequest3(phone,code);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.POST_MESSAGE_CODE, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                //false 从首页提现跳转    true 从提现页面判断是否有支付密码跳转
                if (VocApplication.Companion.getInstance().getMessage_flag() == false){
                    Intent in = new Intent(MessageCodeActivity.this,AddresActivity.class);
                    startActivity(in);
                }else{
                    PreferenceUtil.Companion.getInstance().set("istitle","1");
                    Intent in = new Intent(MessageCodeActivity.this,SetPayPwdActivity.class);
                    startActivity(in);
                }
                finish();
            }

            @Override
            public void onError(Throwable t) {
                tv_input.setText("验证码错误,请重试");
                tv_input.setTextColor(getResources().getColor(R.color.qlColorHqZFUpBg));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 请求获取验证码
     */
        public void getMessage() {
            GetConfirmCodeRequest request = new GetConfirmCodeRequest(phone);
            RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
            HttpManager.post(UrlConstantsKt.GET_MESSAGE_CODE, wrapper).subscribe(new Subscriber<String>() {
                @Override
                public void onNext(String s) {
                    ToastUtil.showToast("验证码发送成功");
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
            });
        }

    /**
     * 取消倒计时
     * @param v
     */
    public void oncancel(View v) {
        timer.cancel();
    }

    /**
     * 开始倒计时
     * @param v
     */
    public void restart(View v) {
        timer.start();
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            te_number.setEnabled(false);
            te_number.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            te_number.setEnabled(true);
            te_number.setText("获取验证码");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.te_number:
                getMessage();
                restart(te_number);
                break;
        }
    }
    public void showKeyboard(final EditText et) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) et.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et, 0);
            }
        }, 300);
    }

}
