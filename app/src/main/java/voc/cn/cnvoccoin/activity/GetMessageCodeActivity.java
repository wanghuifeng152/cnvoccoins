package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ResetPwd1;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_RESET_PWD;

/**
 * 重置密码获取验证码
 */
public class GetMessageCodeActivity extends BaseActivity implements View.OnClickListener{
    Button btn_ok;
    TextView title_name,tv_message;
    EditText et_phone,et_code;
    LinearLayout ll_send;
    ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        title_name = findViewById(R.id.title_name);

        title_name.setText("重置支付密码");
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_message = findViewById(R.id.tv_message);
        ll_send = findViewById(R.id.ll_send);
        ll_send.setOnClickListener(this);
        et_phone = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);
        et_phone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        et_code.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    public void postMessage(){
        String code = et_code.getText().toString().trim();
        String mobile = et_phone.getText().toString().trim();
        ResetPwd1 request = new ResetPwd1(code,mobile,"1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_RESET_PWD,wrapper).subscribe(new Subscriber<String>(){

            @Override
            public void onNext(String s) {
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if(jsonObject.getString("msg").equals("验证码正确")){
                            VocApplication.Companion.getInstance().setMessage_flag(true);
                            PreferenceUtil.Companion.getInstance().set("istitle","2");
                            Intent in = new Intent(GetMessageCodeActivity.this,SetPayPwdActivity.class);
                            startActivity(in);
                            finish();
                        }else{
                            ToastUtil.showToast(jsonObject.getString("msg"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                if(et_phone.getText().toString().trim().length() == 11 && et_code.getText().toString().length() == 6){
                    postMessage();;
                }else{
//                    Toast.makeText(this,"请输入正确的手机号和验证码",Toast.LENGTH_LONG).show();
                    ToastUtil.showToast("请输入正确的手机号和验证码");

                }

                break;
            case R.id.ll_send:
                if(et_phone.getText().toString().trim().length() == 11 ){
                    String token = PreferenceUtil.Companion.getInstance().getString("USER_MOBILE");
                    if(token.equals(et_phone.getText().toString().trim())){
                        getMessage();
                    }else{
                        ToastUtil.showToast("手机号码错误~");
                    }

                }else{
                    ToastUtil.showToast("请输入正确的手机号");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 请求获取验证码
     */
    public void getMessage() {
        String phone = et_phone.getText().toString().trim();
        GetConfirmCodeRequest request = new GetConfirmCodeRequest(phone);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.GET_MESSAGE_CODE, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s)
            {
                ToastUtil.showToast("验证码发送成功");
                restart(tv_message);
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
            ll_send.setEnabled(false);
            tv_message.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            ll_send.setEnabled(true);
            tv_message.setText("获取验证码");
        }
    };

}
