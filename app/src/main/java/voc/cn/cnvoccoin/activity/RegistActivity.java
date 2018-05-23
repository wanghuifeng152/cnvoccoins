package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.LoginEvent;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.RegisterRequest;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;

import static voc.cn.cnvoccoin.util.ConstantsKt.PASSWORD;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_NAME;

/**
 * Created by Administrator on 2018/5/3.
 */

public class RegistActivity extends BaseActivity {

    EditText smsCode;
    EditText pwd;
    EditText pwd_again;
    EditText yaoqingma;
    EditText et_phone;
    private boolean isshow = true;
    private boolean isshow2 = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        smsCode = findViewById(R.id.smsCode);
        pwd = findViewById(R.id.pwd);
        pwd_again = findViewById(R.id.pwd_again);
        yaoqingma = findViewById(R.id.yaoqingma);
        et_phone = findViewById(R.id.et_phone);
        final ImageView iv1 = findViewById(R.id.hide);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pwd.getText().toString();
                String password_two = pwd_again.getText().toString();
                if (!password.equals(password_two)) {
                    ToastUtil.showToast("两次输入密码不一致");
                } else {
                    setRegister();

                }
            }
        });
        findViewById(R.id.tv_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow) {
                    isshow = false;
                    iv1.setImageResource(R.mipmap.hide);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isshow = true;
                    iv1.setImageResource(R.mipmap.show);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        final ImageView iv2 = findViewById(R.id.show);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow2) {
                    isshow2 = false;
                    iv2.setImageResource(R.mipmap.hide);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isshow2 = true;
                    iv2.setImageResource(R.mipmap.show);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    private void setRegister() {
        RegisterRequest request = new RegisterRequest(et_phone.getText().toString(), pwd.getText().toString(), yaoqingma.getText().toString());
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.URL_REGISTER, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
//                    ToastUtil.showToast(jsonObject.getString("msg"))
                        ToastUtil.showToast("注册成功");
                        startActivity(new Intent(RegistActivity.this,LoginActivityNew.class));
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
}
