package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.view.PasswordInputEdt;

/**
 * 用户没有设置过密码 ====设置密码
 */
public class SetPayPwdActivity extends BaseActivity implements View.OnClickListener{

    TextView title_name,tv_pwd;
    Button btn_next;
    PasswordInputEdt edt;
    String pwd ;
    ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        if (!CacheActivity.activityList.contains(SetPayPwdActivity.this)) {
            CacheActivity.addActivity(SetPayPwdActivity.this);
        }
        title_name = findViewById(R.id.title_name);
        String istitle = PreferenceUtil.Companion.getInstance().getString("istitle");
        if(istitle.equals("1")){
            title_name.setText("设置密码");
        }else{
            title_name.setText("重置密码");
        }

        iv_back =findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        tv_pwd = findViewById(R.id.tv_pwd);
        tv_pwd.setText("请设置支付密码");
        btn_next.setText("下一步");
        edt = (PasswordInputEdt) findViewById(R.id.edt);
        edt.setOnInputOverListener(
                new PasswordInputEdt.onInputOverListener() {
                    @Override
                    public void onInputOver(String text) {
                        pwd = text;
                    }
                });
//        删除监听
        edt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    pwd = "";
                }
                return false;
            }
        });
        showKeyboard (edt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                if (TextUtils.isEmpty(pwd) || pwd == null){
                    pwd = "";
                }
                if (pwd.length() == 6 ){
                    VocApplication.Companion.getInstance().setPwd1(pwd);
                    Intent in = new Intent(SetPayPwdActivity.this,ReSetPayPwdActivity.class);
//                    in.putExtra("isTitle",0);
                    pwd = "";
                    startActivityForResult(in,1);
                }else{
                    Toast.makeText(this,"请输入正确的密码",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 ){
            VocApplication.Companion.getInstance().setPwd1("");
            edt.setText("");
            edt.closeText();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
