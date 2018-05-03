package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.RegisterRequest;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstants;

/**
 * Created by Administrator on 2018/5/3.
 */

public class RegistActivity extends BaseActivity {
    EditText pwd;
    EditText pwd_again;
    EditText yaoqingma;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        pwd = findViewById(R.id.pwd);
        pwd_again = findViewById(R.id.pwd_again);
        yaoqingma = findViewById(R.id.yaoqingma);
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
    }

    private void setRegister() {
        RegisterRequest request = new RegisterRequest(pwd.getText().toString(), pwd_again.getText().toString(), yaoqingma.getText().toString());
        RequestBodyWrapper wrapper  = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstants.INSTANCE.getREGISTER(), wrapper).subscribe(new Subscriber() {
            @Override
            public void onNext(Object o) {

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
