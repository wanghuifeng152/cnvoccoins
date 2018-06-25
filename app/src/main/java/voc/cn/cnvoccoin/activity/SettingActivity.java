package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.util.Utils;

/**
 * Created by shy on 2018/6/15.
 */

public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvVersion = findViewById(R.id.tv_version);
        //tvVersion.setText(Utils.getVersionName(this));
    }


}
