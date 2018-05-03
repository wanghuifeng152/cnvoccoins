package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.content.pm.FeatureGroupInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import voc.cn.cnvoccoin.R;

/**
 * Created by Administrator on 2018/5/2.
 */

public class LoginActivityNew extends AppCompatActivity{

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_regist)
    TextView mTvRegist;
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
        findViewById(R.id.tv_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistActivity.class);
                startActivity(intent);
            }
        });
    }
    @OnClick({R.id.iv_back,R.id.tv_regist})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_regist:
                finish();
                break;
        }
    }

}
