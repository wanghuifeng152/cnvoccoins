package voc.cn.cnvoccoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.fragment.UserFragment;

public class SuccessActivity extends Activity {

    private ImageView address_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        initView();
        initData();
        hideStatusBar();
    }

    private void initData() {

        address_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessActivity.this, AddresActivity2.class));
                finish();
            }
        });

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
    private void initView() {
        address_back = (ImageView) findViewById(R.id.address_back);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(SuccessActivity.this, AddresActivity2.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
