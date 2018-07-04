package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;

public class IdentityActivity extends AppCompatActivity {

    @BindView(R.id.identity_back)
    ImageView identityBack;
    @BindView(R.id.identity_name)
    EditText identityName;
    @BindView(R.id.identity_number)
    EditText identityNumber;
    @BindView(R.id.identity_confirm)
    Button identityConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        identityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
