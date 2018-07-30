package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.util.AppUtils;

/**
 *
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_title)
    RelativeLayout llTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        // 版本号改为动态获取,不需要手动改
        TextView version = findViewById(R.id.tv_version);
        version.setText(AppUtils.getVerName(SettingActivity.this));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            json.put("accesskey","key");
            json.put("id","id");
            json2.put("name","zhangsan");
            json2.put("add","beijing");
            json.put("data",json2.toString());
            Log.i("log",json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
