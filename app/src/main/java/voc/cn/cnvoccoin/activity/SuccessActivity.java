package voc.cn.cnvoccoin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
