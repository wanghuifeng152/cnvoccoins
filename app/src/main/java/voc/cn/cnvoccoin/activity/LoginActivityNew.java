package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import voc.cn.cnvoccoin.R;

/**
 * Created by Administrator on 2018/5/2.
 */

public class LoginActivityNew extends AppCompatActivity{

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
