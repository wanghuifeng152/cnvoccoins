package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import voc.cn.cnvoccoin.R;

/**
 * Created by Administrator on 2018/5/3.
 */

public class RegistActivity extends AppCompatActivity {
    private boolean isshow=true;
    private boolean isshow2=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.tv_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final ImageView iv1=findViewById(R.id.hide);
        final EditText pwd=findViewById(R.id.pwd);
        final EditText pwd_again=findViewById(R.id.pwd_again);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow){
                    isshow=false;
                    iv1.setImageResource(R.mipmap.hide);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    isshow=true;
                    iv1.setImageResource(R.mipmap.show);
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        final ImageView iv2=findViewById(R.id.show);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isshow2){
                    isshow2=false;
                    iv2.setImageResource(R.mipmap.hide);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    isshow2=true;
                    iv2.setImageResource(R.mipmap.show);
                    pwd_again.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }
}
