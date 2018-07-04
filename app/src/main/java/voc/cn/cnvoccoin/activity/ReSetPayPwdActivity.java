package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Timer;
import java.util.TimerTask;

import retrofit2.http.HEAD;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ResetPwd2;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.postPayPwd;
import voc.cn.cnvoccoin.view.LoadingDialog;
import voc.cn.cnvoccoin.view.PasswordInputEdt;

import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_PAY_PWD;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        title_name = findViewById(R.id.title_name);
        if (!CacheActivity.activityList.contains(ReSetPayPwdActivity.this)) {
            CacheActivity.addActivity(ReSetPayPwdActivity.this);
        }
        Intent intent = getIntent();
        int isTitle = intent.getIntExtra("isTitle", -1);
        String istitle = PreferenceUtil.Companion.getInstance().getString("istitle");

        if (isTitle != 0) {
            title_name.setText("设置密码");
        } else if (isTitle == 1) {
            title_name.setText("确认密码");
        } else {
            title_name.setText("重置支付密码");
        }

       /*   if(istitle.equals("1")){
            title_name.setText("重置密码");
        }else{
            title_name.setText("确认重置支付密码");
        }*/

//        if(isTitle != 0){
////            title_name.setText("设置密码");
////        }else if (isTitle == 1){
////            title_name.setText("确认密码");
////        }else{
////            title_name.setText("重置支付密码");
////        }
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
                    pwd = "";
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
        final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        loadingDialog.show();
        ResetPwd2 request = new ResetPwd2(pwd, "2");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_RESET_PWD, wrapper).subscribe(new Subscriber<String>() {

            @Override
            public void onNext(String s) {
                loadingDialog.dismiss();
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
                            Intent in = new Intent(ReSetPayPwdActivity.this, WalletActivity.class);
                            startActivity(in);

                        } else {
//                            ToastUtil.showToast(jsonObject.getString("msg"));
                            ToastUtil.showToast("新密码不能与近期用过密码相同");
                            edt.setText("");
                            edt.closeText();
                            finish();
                        }
//                        else if(jsonObject.getString("msg").equals("重置密码失败")){
//                            VocApplication.Companion.getInstance().setPwd2(pwd);
//                            edt.setText("");
//                            edt.closeText();
//                            Intent intent = getIntent();
//                            setResult(1,intent);
//                            finish();
//                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
     * 设置支付密码
     */
    public void postPayPwd() {
        final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        loadingDialog.show();
        postPayPwd request = new postPayPwd(pwd);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_PAY_PWD, wrapper).subscribe(new Subscriber<String>() {


            @Override
            public void onNext(String s) {
                loadingDialog.dismiss();
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if (jsonObject.getString("msg").equals("设置成功")) {
                            Intent in = new Intent(ReSetPayPwdActivity.this, ForwardActivity.class);
                            startActivity(in);
                            CacheActivity.finishActivity();
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
                loadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        edt.closeKeybord();
//    }
}
