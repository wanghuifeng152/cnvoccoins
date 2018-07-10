package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ishumei.smantifraud.SmAntiFraud;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.BaseShuMeiEntity;
import voc.cn.cnvoccoin.entity.DataEntity;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest;
import voc.cn.cnvoccoin.util.OkHttpUtils;
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
    private boolean isshow = false;
    private boolean isshow2 = false;
    int time = 60;
    private ImageView delete;
    private ImageView iv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        pwd = findViewById(R.id.pwd);
    /*    pwd_again = findViewById(R.id.pwd_again);
        yaoqingma = findViewById(R.id.yaoqingma);*/
        et_phone = findViewById(R.id.et_phone);
        mTvConfirm = findViewById(R.id.tv_get_confirm);
        mEtConfirm = findViewById(R.id.et_confirm_code);
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
                    iv1.setImageResource(R.mipmap.show);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
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

    private void initJudge() {
        TextChange textChange=new TextChange();
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
            if(psw .length() > 0 ){
                iv1.setVisibility(View.VISIBLE);
            }else{
                iv1.setVisibility(View.GONE);
            }
            if(phone != null){
                delete.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
            }
           if(phone.length() == 11){
                if(met.length() == 6){
                    if(psw.length() >= 6){
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
                                if(psw.length() >= 6){
                                    setRegister();
                                }else{
                                    ToastUtil.showToast("密码输入错误");
                                }
                            }
                        });
                        findViewById(R.id.btn_commit).setSelected(true);

                    }else{
                        findViewById(R.id.btn_commit).setSelected(false);
                        findViewById(R.id.btn_commit).setOnClickListener(null);
                    }

                }else{
                    findViewById(R.id.btn_commit).setSelected(false);
                    findViewById(R.id.btn_commit).setOnClickListener(null);
                }
           }else{
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
                final LoadingDialog loadingDialog = new LoadingDialog(RegistActivity.this, "");
                loadingDialog.show();
                GetConfirmCodeRequest request = new GetConfirmCodeRequest(mobile);
                RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
                HttpManager.post(UrlConstantsKt.MOBILE_CONFIRM_CODE, wrapper).subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
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

                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
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

    private void setRegister() {
        RegisterRequest request = new RegisterRequest(et_phone.getText().toString(), pwd.getText().toString(), mEtConfirm.getText().toString());
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.URL_REGISTER, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(final String s) {
                if (s == null || s.isEmpty())
                    return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    final int code = jsonObject.getInt("code");
                    if (code == 1) {
                        //处理事件
                        ToastUtil.showToast("注册成功");

                        /**
                         *《---------------------------------------------==- 数美注册 -==---------------------------------------------》
                         */
                        Gson gson = new Gson();
//        注意！！获取 deviceId，这个接口在需要使用 deviceId 时地方调用。
                        String deviceId = SmAntiFraud.getDeviceId();
                        BaseShuMeiEntity baseShuMeiEntity = new BaseShuMeiEntity("JAfaz1iOMMcUCAmZedUi", "voc.cn.cnvoccoin", "register",
                                new DataEntity(et_phone.getText().toString().trim(), deviceId, OkHttpUtils.getInstens().getIP(), OkHttpUtils.getInstens().getTimer(), "phone", et_phone.getText().toString().trim()));
                        String data = gson.toJson(baseShuMeiEntity);
                        OkHttpUtils.getInstens().postHttp("http://api.fengkongcloud.com/v2/event",data).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("registerError",e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("registerSuccess",response.body().string().toString());
                            }
                        });


                        startActivity(new Intent(RegistActivity.this, LoginActivityNew.class));
//                    ToastUtil.showToast(jsonObject.getString("msg"))
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

