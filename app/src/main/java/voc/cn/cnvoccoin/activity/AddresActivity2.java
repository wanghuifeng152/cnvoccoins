package voc.cn.cnvoccoin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.adapter.DetailedAdapter;
import voc.cn.cnvoccoin.entity.AddresClass;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.list;

/**
 * 地址列表页面
 */
public class AddresActivity2 extends AppCompatActivity {

    @BindView(R.id.identity_back)
    ImageView identityBack;
    @BindView(R.id.address2_lv)
    ListView address2Lv;
    @BindView(R.id.identity_confirm)
    TextView identityConfirm;

    private DetailedAdapter detailedAdapter;
    private List<AddresClass.MsgBean> addresClassMsg;
    private int i;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addres2);
        ButterKnife.bind(this);
        initData();
        intent = getIntent();
        hideStatusBar();
    }
    //只透明状态栏
    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 23) {
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

        list request = new list("1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.ADDRES, wrapper).subscribe(new Subscriber<String>() {
            private List<AddresClass.MsgBean> addresClassMsg;

            @Override
            public void onNext(String s) {
                Log.d("TAG", "onNext() returned:++++++++++++++++ " + s);
                AddresClass addresClass = new Gson().fromJson(s, AddresClass.class);
                addresClassMsg = addresClass.getMsg();
                if (addresClassMsg.get(0).getAddress().equals("0") && addresClassMsg.get(0).getRemarks().equals("0")){
                    ToastUtil.showToast("您还没有添加地址");
                }else{
                    detailedAdapter = new DetailedAdapter(AddresActivity2.this, addresClassMsg);
                    address2Lv.setAdapter(detailedAdapter);
                    Intent intent = getIntent();
                    int position = intent.getIntExtra("position", 0);
                    detailedAdapter.setChecked(position);
                    detailedAdapter.notifyDataSetChanged();

                }
                initListener(addresClassMsg);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initListener(final List<AddresClass.MsgBean> addresClassMsgList) {
        identityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("addres","");
//                startActivity(intent);
                setResult(200,intent);
                finish();
            }
        });
        address2Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i = position;
                detailedAdapter.setChecked(position);
//                Intent intent = new Intent(AddresActivity2.this,ForwardActivity.class);
                intent.putExtra("addres", addresClassMsgList.get(i).getAddress());
                intent.putExtra("position",position);
//                startActivity(intent);
                setResult(200, intent);
                detailedAdapter.notifyDataSetChanged();
                finish();
            }
        });
        identityConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddresActivity2.this, AddresActivity.class);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
