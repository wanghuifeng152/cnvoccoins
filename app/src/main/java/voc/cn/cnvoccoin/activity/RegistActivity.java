package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishumei.smantifraud.SmAntiFraud;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest_sm;
import voc.cn.cnvoccoin.util.RegisterRequest;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.view.LoadingDialog;

/**
 * Created by Administrator on 2018/5/3.
 */

public class RegistActivity extends BaseActivity {
    EditText pwd;
    /*   EditText pwd_again;
       EditText yaoqingma;*/
    EditText et_phone;
    TextView mTvConfirm;
    EditText mEtConfirm;
    Timer timer;
    @BindView(R.id.processBar)
    ProgressBar processBar;
    private boolean isshow = false;
    private boolean isshow2 = false;
    int time = 60;
    private ImageView delete;
    private ImageView iv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        initView();
        pwd = findViewById(R.id.pwd);
        et_phone = findViewById(R.id.et_phone);
        et_phone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mTvConfirm = findViewById(R.id.tv_get_confirm);
        mEtConfirm = findViewById(R.id.et_confirm_code);
        mEtConfirm.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        iv1 = findViewById(R.id.hide);

        initJudge();
        /*yaoqingma.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    setRegister();
                }
                return false;
            }
        });*/
        getConfirmCode();
        //返回监听
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phone.setText("");
                delete.setVisibility(View.GONE);
            }
        });
        //注册监听 根据两次输入密码是否一样进行判断注册
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String password = pwd.getText().toString();
//                String password_two = pwd_again.getText().toString();
//                if (!password.equals(password_two)) {
//                    ToastUtil.showToast("两次输入密码不一致");
//                } else {
                String psw = pwd.getText().toString();
                if (psw.length() >= 6) {
                    setRegister();
                } else {
                    ToastUtil.showToast("密码输入错误");
                }


                //final ImageView iv2 = findViewById(R.id.show);
                //点击确认密码后面图标是否隐藏
    /*    iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow2) {
                    //密码隐藏
                    isshow2 = false;
                    iv2.setImageResource(R.mipmap.hide);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    //密码显示
                    isshow2 = true;
                    iv2.setImageResource(R.mipmap.show);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });*/
            }
        });
        //点击返回上一个页面（登录页面）
        findViewById(R.id.tv_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //点击输入密码后面图标显示密码是否隐藏
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow) {
                    //密码隐藏
                    isshow = false;
                    iv1.setImageResource(R.mipmap.xianshi);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    //密码显示
                    isshow = true;
                    iv1.setImageResource(R.mipmap.eye);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    private void initJudge() {
        TextChange textChange = new TextChange();
        mEtConfirm.addTextChangedListener(textChange);
        et_phone.addTextChangedListener(textChange);
        pwd.addTextChangedListener(textChange);


    }

    //设置edittext的输入监听
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String psw = pwd.getText().toString();
            String phone = et_phone.getText().toString();
            String met = mEtConfirm.getText().toString();
            if (psw.length() > 0) {
                iv1.setVisibility(View.VISIBLE);
            } else {
                iv1.setVisibility(View.GONE);
            }
            if (phone.length() > 0) {
                delete.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }
            if (phone.length() == 11) {
                if (met.length() == 6) {
                    if (psw.length() >= 6) {
                        //注册监听 根据两次输入密码是否一样进行判断注册
                        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                String password = pwd.getText().toString();
//                String password_two = pwd_again.getText().toString();
//                if (!password.equals(password_two)) {
//                    ToastUtil.showToast("两次输入密码不一致");
//                } else {
                                String psw = pwd.getText().toString();
                                if (psw.length() >= 6) {
                                    setRegister();
                                } else {
                                    ToastUtil.showToast("密码输入错误");
                                }
                            }
                        });
                        findViewById(R.id.btn_commit).setSelected(true);

                    } else {
                        findViewById(R.id.btn_commit).setSelected(false);
                        findViewById(R.id.btn_commit).setOnClickListener(null);
                    }

                } else {
                    findViewById(R.id.btn_commit).setSelected(false);
                    findViewById(R.id.btn_commit).setOnClickListener(null);
                }
            } else {
                findViewById(R.id.btn_commit).setSelected(false);
                findViewById(R.id.btn_commit).setOnClickListener(null);
            }
        }
    }

    private void getConfirmCode() {
        //点击获取验证码
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = et_phone.getText().toString();
                if (mobile == null || mobile.isEmpty()) {
                    ToastUtil.showToast("请输入手机号");
                    return;
                }
                processBar.setVisibility(View.VISIBLE);
                String DID = SmAntiFraud.getDeviceId();
                Log.i("log", "~~~~~~~~~~~~`" + DID);
                GetConfirmCodeRequest_sm request = new GetConfirmCodeRequest_sm(mobile, DID);
                RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
//                UrlConstantsKt.MOBILE_CONFIRM_CODE
                HttpManager.post(UrlConstantsKt.MOBILE_CONFIRM_CODE, wrapper).subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        processBar.setVisibility(View.GONE);
                        if (s == null || s.isEmpty()) return;
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code == 1) {
                                ToastUtil.showToast(jsonObject.getString("msg"));
                                getConfirmTimer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        processBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        processBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void getConfirmTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time > 0) {
                            time--;
                            mTvConfirm.setText(time + "S");
                            mTvConfirm.setClickable(false);
                        } else {
                            timer.cancel();
                            mTvConfirm.setText("重新获取");
                            mTvConfirm.setClickable(true);

                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * zhuce
     */
    private void setRegister() {
        String deviceId = SmAntiFraud.getDeviceId();
        String edphone = et_phone.getText().toString();
        String pwad = pwd.getText().toString();
        String mEtcon = mEtConfirm.getText().toString();
        processBar.setVisibility(View.VISIBLE);
        RegisterRequest request = new RegisterRequest(edphone, pwad, mEtcon,deviceId);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.URL_REGISTER, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(final String s) {
                processBar.setVisibility(View.GONE);
                if (s == null || s.isEmpty())
                    return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    final int code = jsonObject.getInt("code");
                    if (code == 1) {
                        //处理事件
                        ToastUtil.showToast("注册成功");
                        startActivity(new Intent(RegistActivity.this, LoginActivityNew.class));
//                    ToastUtil.showToast(jsonObject.getString("msg"))
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable t) {
                processBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void initView() {
        delete = (ImageView) findViewById(R.id.delete);
    }
}