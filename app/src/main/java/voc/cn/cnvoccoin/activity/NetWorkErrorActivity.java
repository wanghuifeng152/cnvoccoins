package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import voc.cn.cnvoccoin.R;

public class NetWorkErrorActivity extends BaseActivity {

    TextView title_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
        title_name = findViewById(R.id.title_name);
        title_name.setText("网络问题");

    }


}
