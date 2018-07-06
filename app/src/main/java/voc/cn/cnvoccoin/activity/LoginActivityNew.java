package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.ResBaseModel;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.LoginRequest;
import voc.cn.cnvoccoin.util.LoginResponse;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.view.LoadingDialog;

import static voc.cn.cnvoccoin.util.ConstantsKt.PASSWORD;
import static voc.cn.cnvoccoin.util.ConstantsKt.TOKEN;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_ID;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_NAME;

/**
 * Created by Administrator on 2018/5/2.
 */

public class  LoginActivityNew extends BaseActivity {

    TextView mTvRegist;
    Button mBtnLogin;
    EditText mEtPhone;
    EditText mEtPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        initView();

    }

    private void initView() {
        mEtPhone = findViewById(R.id.et_login_phone);
        mEtPwd = findViewById(R.id.et_login_pwd);

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


        //登录监听
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();
            }
        });
        mEtPwd.setOnEditorActionListener(listenerr);
    }
    private TextView.OnEditorActionListener listenerr = new TextView.OnEditorActionListener()
    {
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

    private void getLogin() {
        final String username = mEtPhone.getText().toString();
        final String password = mEtPwd.getText().toString();
        if (username.isEmpty()) return;
        final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        loadingDialog.show();
        LoginRequest request = new LoginRequest(username, password, "android");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.URL_LOGIN, wrapper).subscribe(new Subscriber<ResBaseModel<LoginResponse>>() {

            @Override
            public void onNext(ResBaseModel<LoginResponse> model) {
                loadingDialog.dismiss();
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
                loadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        }, LoginResponse.class, ResBaseModel.class);
    }

}
