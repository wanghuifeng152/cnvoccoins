package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.AppUtils;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.postId;

import static voc.cn.cnvoccoin.util.UrlConstantsKt.POST_IS_HAVE_PWD;

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
    @BindView(R.id.rl_reset_pwd)
    RelativeLayout rl_reset_pwd;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.processBasr)
    ProgressBar processBasr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        // 版本号改为动态获取,不需要手动改
        TextView version = findViewById(R.id.tv_version);
        initData();
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
            json.put("accesskey", "key");
            json.put("id", "id");
            json2.put("name", "zhangsan");
            json2.put("add", "beijing");
            json.put("data", json2.toString());
            Log.i("log", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rl_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postIsHavePwd();
            }
        });
    }

    private void initData() {


    }


    private void postIsHavePwd() {
        processBasr.setVisibility(View.VISIBLE);
        postId request = new postId("11");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(POST_IS_HAVE_PWD, wrapper).subscribe(new Subscriber<String>() {

            @Override
            public void onNext(String s) {
                processBasr.setVisibility(View.GONE);
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if (jsonObject.getString("msg").equals("还没有支付密码")) {
                            ToastUtil.showToast("您还没有设置支付密码, 请先设置支付密码");
                            VocApplication.Companion.getInstance().setMessage_flag(true);
                            PreferenceUtil.Companion.getInstance().set("istitle", "1");
                            Intent intent = new Intent(SettingActivity.this, SetPayPwdActivity.class);
                            startActivity(intent);
                        } else {
                            //有设置密码去重置
                            VocApplication.Companion.getInstance().setResetPwd(true);
                            PreferenceUtil.Companion.getInstance().set("pwdFlag", "1");//首页重置密码跳转
                            Intent intent = new Intent(SettingActivity.this, GetMessageCodeActivity.class);
                            intent.putExtra("isTitle", 0);
                            startActivity(intent);
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
}
