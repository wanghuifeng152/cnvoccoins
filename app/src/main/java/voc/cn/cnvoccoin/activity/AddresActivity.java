package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.add;

/**
 * 添加地址
 */
public class AddresActivity extends AppCompatActivity {

    @BindView(R.id.address_back)
    ImageView addressBack;
    @BindView(R.id.address_remarks)
    EditText addressRemarks;
    @BindView(R.id.address_wallet)
    EditText addressWallet;
    @BindView(R.id.address_confirm)
    Button addressConfirm;
    @BindView(R.id.address_Web)
    WebView addressWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addres);
        ButterKnife.bind(this);
        //关闭输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideStatusBar();
        initListener();
        initData();
    }
    //只透明状态栏
    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 24) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void initData() {
        addressWeb.loadUrl("http://www.vochain.world./user/index/createmoney");
        WebSettings settings = addressWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        addressWeb.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                addressWeb.loadUrl("http://www.vochain.world./user/index/createmoney");
                if(request.getUrl().toString().equals("https://token.im/")){
                    Uri uri = Uri.parse(request.getUrl().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void initListener() {
        addressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addressWallet.addTextChangedListener(new TextWatcher() {

            private CharSequence tmap;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tmap = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String remarks = addressRemarks.getText().toString().trim();
                final String wallet = addressWallet.getText().toString().trim();
                if(remarks != null && wallet != null){
                    if (tmap.length() == 42) {
                        addressConfirm.setOnClickListener(
                                new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add request = new add(addressRemarks.getText().toString().trim(),addressWallet.getText().toString().trim());
                                RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
                                HttpManager.post(UrlConstantsKt.ADD, wrapper).subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onNext(String s) {
                                        if (s == null || s.isEmpty()) return;
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(s);
                                            int code = jsonObject.getInt("code");
                                            if (code == 1) {
//                                            ToastUtil.showToast(jsonObject.getString("msg"));
//                                            ToastUtil.showToast("添加成功");
                                                SharedPreferences flag = getSharedPreferences("voccoin", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = flag.edit();
                                                editor.putString("flag","1");
                                                editor.commit();

                                                Intent in = new Intent(AddresActivity.this,SuccessActivity.class);
                                                startActivity(in);
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {


                                    }
                                });
                                //finish();
                            }
                        });
                        addressConfirm.setSelected(true);
                    }
                    if (tmap.length() < 42) {
                        addressConfirm.setOnClickListener(null);
                        addressConfirm.setSelected(false);

                    }
                }

            }
        });
    }


}
