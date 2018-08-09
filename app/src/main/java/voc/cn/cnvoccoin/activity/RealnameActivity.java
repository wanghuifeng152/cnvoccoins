package voc.cn.cnvoccoin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.alibaba.security.rp.RPSDK;
import com.google.gson.Gson;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.Realname;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.isName;

public class RealnameActivity extends AppCompatActivity {

    @BindView(R.id.address_back)
    ImageView addressBack;
    @BindView(R.id.address_remarks)
    EditText addressRemarks;
    @BindView(R.id.address_snmd)
    EditText addressSnmd;
    @BindView(R.id.real_butt)
    Button realButt;
    final String pase = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realname);
        ButterKnife.bind(this);
        initData();
    }

    private void initlinrea() {
        String name = addressRemarks.getText().toString();
        String sund = addressSnmd.getText().toString();
        isName isname = new isName(name, sund);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(isname);
        Log.e("TAG", "initlinrea: =-----------2222-->"+wrapper );

        HttpManager.post(UrlConstantsKt.POST_REALNAME,wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String o) {
                    Log.e("TAG", "onNext:---------------> "+o.toString() );
                Realname realname = new Gson().fromJson(o, Realname.class);
                String token = realname.getData().getToken();
                Log.e("TAG", "onNext: --------------11111->"+token );
//                RPSDK.start(token, RealnameActivity.this,
//                        new RPSDK.RPCompletedListener() {
//                            @Override
//                            public void onAuditResult(RPSDK.AUDIT audit) {
//                                Toast.makeText(RealnameActivity.this, audit + "", Toast.LENGTH_SHORT).show();
//                                if(audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
//                                }
//                                else if(audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
//                                }
//                                else if(audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。
//                                }
//                                else if(audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
//                                }
//                                else if(audit == RPSDK.AUDIT.AUDIT_EXCEPTION){ //系统异常
//                                }
//                            }
//                        });
            }

            @Override
            public void onError(Throwable t) {
                Log.e("realname",t.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });


    }

    private void initData() {
        addressSnmd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String testsum = s.toString();
                if (testsum.length() == 18) {
                    if (Pattern.matches(pase, testsum)) {
                        realButt.setEnabled(true);
                    } else {
                        Toast.makeText(RealnameActivity.this, "身份信息输入有误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    realButt.setEnabled(false);
                }
            }
        });
        realButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initlinrea();
            }
        });


    }
}
