package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.security.rp.RPSDK;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.entity.Realname;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ResetPwd2;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.list;
import voc.cn.cnvoccoin.util.postId;
import voc.cn.cnvoccoin.util.postPayPwd;
import voc.cn.cnvoccoin.view.LoadingDialog;
import voc.cn.cnvoccoin.view.PasswordInputEdt;

import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_PAY_PWD;
import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_REALNAME;
import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_REALNAME_two;
import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_RESET_PWD;

/**
 * 用户没有设置过密码 ====确认设置密码
 */
public class ReSetPayPwdActivity extends BaseActivity implements View.OnClickListener {

    TextView title_name, tv_pwd;
    Button btn_next;
    PasswordInputEdt edt;
    String pwd;
    ImageView iv_back;
    String pwdIntentFlag;
    private ProgressBar processBasr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        initView();
        title_name = findViewById(R.id.title_name);
        if (!CacheActivity.activityList.contains(ReSetPayPwdActivity.this)) {
            CacheActivity.addActivity(ReSetPayPwdActivity.this);
        }
        Intent intent = getIntent();
        int isTitle = intent.getIntExtra("isTitle", -1);
        String istitle = PreferenceUtil.Companion.getInstance().getString("istitle");
        pwdIntentFlag = PreferenceUtil.Companion.getInstance().getString("pwdFlag");

        if (istitle.equals("1")) {
            title_name.setText("确认密码");
        } else {
            title_name.setText("确认重置密码");
        }
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        tv_pwd = findViewById(R.id.tv_pwd);
        tv_pwd.setText("请确认支付密码");
        btn_next.setText("确定");
        edt = (PasswordInputEdt) findViewById(R.id.edt);
        Openkeybord();
        edt.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                pwd = text;
            }
        });
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt.isEnabled()) {
                    tv_pwd.setText("请确认支付密码");
                    tv_pwd.setTextColor(getResources().getColor(R.color.black));
                    edt.setRectNormalColor(getResources().getColor(R.color.color_gray));
                    edt.setRectChooseColor(getResources().getColor(R.color.pwd));
//                    Openkeybord();
                }
            }
        });
        /**
         * 监听EditText删除
         */
        edt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    pwd = "";
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (TextUtils.isEmpty(pwd) || pwd == null) {
                    pwd = "";
                }
                if (pwd.length() == 6) {
                    VocApplication.Companion.getInstance().setPwd2(pwd);
                    if (VocApplication.Companion.getInstance().getPwd1().equals(VocApplication.Companion.getInstance().getPwd2())) {
                        if (VocApplication.Companion.getInstance().isResetPwd() == true) {
                            //请求重置密码接口
                            postResetPwd();
                        } else {
                            postPayPwd();
                        }

                    } else {
                        tv_pwd.setText("支付密码不一致");
                        tv_pwd.setTextColor(getResources().getColor(R.color.qlColorTextRed));
                        edt.setText("");
                        edt.closeText();
                        edt.setRectNormalColor(getResources().getColor(R.color.qlColorTextRed));
                        edt.setRectChooseColor(getResources().getColor(R.color.qlColorTextRed));
                    }
                } else {
//                    Toast.makeText(this, "请输入正确的密码" + pwd, Toast.LENGTH_LONG).show();
                    ToastUtil.showToast("请输入正确的密码");

                }
                break;
        }
    }

    /**
     * 重置密码接口
     */
    public void postResetPwd() {
        processBasr.setVisibility(View.VISIBLE);
        ResetPwd2 request = new ResetPwd2(pwd, "2");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_RESET_PWD, wrapper).subscribe(new Subscriber<String>() {

            @Override
            public void onNext(String s) {
                processBasr.setVisibility(View.GONE);
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if (jsonObject.getString("msg").equals("重置密码成功")) {
                            VocApplication.Companion.getInstance().setResetPwd(false);
                            ToastUtil.showToast("重置成功");
                            CacheActivity.finishActivity();
                            VocApplication.Companion.getInstance().setMessage_flag(true);
                            if (pwdIntentFlag.equals("1")) {
                                Intent in = new Intent(ReSetPayPwdActivity.this, MainActivity.class);
                                startActivity(in);
                            }
                            CacheActivity.finishActivity();


                        } else {
                            ToastUtil.showToast(jsonObject.getString("msg"));
//                            ToastUtil.showToast("新密码不能与近期用过密码相同");
                            edt.setText("");
                            edt.closeText();
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable t) {
                processBasr.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBasr.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 设置支付密码
     */
    public void postPayPwd() {
        processBasr.setVisibility(View.VISIBLE);
        postPayPwd request = new postPayPwd(pwd);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_PAY_PWD, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                processBasr.setVisibility(View.GONE);
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if (jsonObject.getString("msg").equals("设置成功")) {
                            //设置完密码判断是否实名认证
                            postrealnamtwo();
//                            Intent in = new Intent(ReSetPayPwdActivity.this, ForwardActivity.class);
//                            startActivity(in);
//                            CacheActivity.finishActivity();
                        } else {
                            ToastUtil.showToast(jsonObject.getString("msg"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable t) {
                processBasr.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBasr.setVisibility(View.GONE);
            }
        });
    }

    public void Openkeybord() {
        edt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 50);
    }

    private void initView() {
        processBasr = (ProgressBar) findViewById(R.id.processBasr);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        edt.closeKeybord();
//    }

    /**
     * 是否实名认证
     *
     * @return
     */
    private void postrealnamtwo() {
        processBasr.setVisibility(View.VISIBLE);
        postId list = new postId("1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(list);
        HttpManager.post(UrlConstantsKt.POST_REALNAME_two, wrapper).subscribe(new Subscriber<String>() {
            public void onNext(String t) {
                processBasr.setVisibility(View.GONE);
                if (t == null || t.isEmpty()) return;
                try {
                    JSONObject jsonObject = null;

                    jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");

                    if (code == 1) {
                        if("未认证".equals(msg)){
                            //获取实名认证token
                            postrealnam();
                        }else{
                            Intent intents = new Intent(ReSetPayPwdActivity.this, ForwardActivity.class);
                            startActivity(intents);
                            CacheActivity.finishActivity();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                processBasr.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBasr.setVisibility(View.GONE);
            }
        });

    }

    //获取实名认证Token
    public void postrealnam() {
        processBasr.setVisibility(View.VISIBLE);
        postId list = new postId("1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(list);
        HttpManager.post(UrlConstantsKt.POST_REALNAME, wrapper).subscribe(new Subscriber<String>() {
            public void onNext(String t) {
                processBasr.setVisibility(View.GONE);
                Log.e("Tag", "onNext----------------->" + t.toString());
                Realname realname = new Gson().fromJson(t, Realname.class);
                CacheActivity.finishActivity();
                String token = realname.getData().getToken();
                //实人认证代码
                RPSDK.start(token, ReSetPayPwdActivity.this,
                        new RPSDK.RPCompletedListener() {
                            @Override
                            public void onAuditResult(RPSDK.AUDIT audit) {
                                if (audit == RPSDK.AUDIT.AUDIT_PASS) {
                                    //认证通过
                                    //跳到用户信息绑定接口

                                    postrealname();
                                } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) {
                                    //认证不通过
                                    CacheActivity.finishActivity();
                                } else if (audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) {
                                    //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。
                                } else if (audit == RPSDK.AUDIT.AUDIT_NOT) {
                                    //未认证，用户取消
                                    CacheActivity.finishActivity();
                                } else if (audit == RPSDK.AUDIT.AUDIT_EXCEPTION) {
                                    //系统异常
                                    CacheActivity.finishActivity();
                                }
                            }
                        });
            }

            @Override
            public void onError(Throwable t) {
                processBasr.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBasr.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 与数据库进行绑定用户信息
     */
    public void postrealname() {
        processBasr.setVisibility(View.VISIBLE);
        postId list = new postId("1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(list);
        HttpManager.post(UrlConstantsKt.POST_REALNAME_one, wrapper).subscribe(new Subscriber<String>() {
            public void onNext(String t) {
                processBasr.setVisibility(View.GONE);
                if (t == null || t.isEmpty()) return;

                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 1) {
                        Toast.makeText(ReSetPayPwdActivity.this, "实名认证任务已完成，获得287voc", Toast.LENGTH_SHORT).show();
                        Intent intents = new Intent(ReSetPayPwdActivity.this, ForwardActivity.class);
                        startActivity(intents);

                    } else {
                        Toast.makeText(ReSetPayPwdActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        postrealnam();
                    }
                    CacheActivity.finishActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                processBasr.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                processBasr.setVisibility(View.GONE);
            }


        });

    }
}
