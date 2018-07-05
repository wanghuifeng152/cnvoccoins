package voc.cn.cnvoccoin.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.entity.WalletClass;
import voc.cn.cnvoccoin.util.isNumber;
import voc.cn.cnvoccoin.view.PasswordInputEdt;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.ChargeBean;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.Forwardid;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ResetPwd3;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.isHaveAddress;
import voc.cn.cnvoccoin.view.LoadingDialog;

import static voc.cn.cnvoccoin.util.UrlConstantsKt.SECCE_SS;

public class ForwardActivity extends BaseActivity {


    private String qbUrl = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{42}$";
    @BindView(R.id.address_back)
    ImageView addressBack;
    @BindView(R.id.address_remarks)
    EditText addressRemarks;
    @BindView(R.id.ic_choice)
    ImageView icChoice;
    @BindView(R.id.address_snmd)
    EditText addressSnmd;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.address_confirm)
    Button addressConfirm;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_txian)
    TextView tvTxian;
    @BindView(R.id.tv_qub)
    TextView tvQub;
    private double use;
    private boolean isEdit = true;
    private int position = 0;
    private int outNum = 0;
    private List<InputFilter> inputFilters;
    private double moeny;
    private Dialog dialog;
    private double use1;
    private int startTwo;
    private String mwallet;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_forward);
        ButterKnife.bind(this);
        addressSnmd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        addressSnmd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    addressSnmd.getText().toString().trim();
                    if (addressSnmd.getText().toString().trim().startsWith("0") || addressSnmd.getText().toString().trim().startsWith(".")){
                        addressSnmd.setText("");
                    }
                }
                return false;
            }
        });
        inputFilters = new ArrayList<>();
        Intent intent = getIntent();
        moeny = intent.getDoubleExtra("moeny", 0.0);
        initData();

        //获取备注，钱包地址
//        initHuoQu();
    }



    private void initQingqiu() {

        String trim = addressSnmd.getText().toString().trim();
        Forwardid request = new Forwardid(trim);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.POST_PASSWORD, wrapper).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String str) {
                ChargeBean chargeBean = new Gson().fromJson(str, ChargeBean.class);
                ChargeBean.MsgBean msgBean = chargeBean.getMsg();
                int charge = msgBean.getCharge();
                double actual = msgBean.getActual();
                use = msgBean.getUse();
                tvTxian.setText(use + "");
                tvCharge.setText(charge + "");
                tvAssets.setText(actual + "");

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {


            }
        });

    }

    private String mWallet;

    private void initData() {

        //返回监听
        addressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //输入框后的尖括号
        icChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postResetPwd();
//                finish();
            }
        });


        //创建地址连接
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForwardActivity.this, WalletTwoActivity.class));
            }
        });

        tvQub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DecimalFormat df=new DecimalFormat(".########");
//                Double aDouble = Double.valueOf(df.format(use1));
                int numberDecimalDigits = getNumberDecimalDigits(use1);
                if (numberDecimalDigits > 8){
                    String substring = (use1 + "").substring(0, (use1 + "").indexOf(".") + 9);
                    addressSnmd.setText(substring+"");
                    return;
                }
                addressSnmd.setText(use1+"");
            }

        });



        //确定按钮
        addressConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //提币输入数量
                mwallet = addressSnmd.getText().toString().trim();
                double wallet = 0;
                //判断它不为空
                if (!TextUtils.isEmpty(mwallet) && !mwallet.equals("")) {
                    wallet = Double.parseDouble(mwallet);
                    showPayDialog(mwallet);
                    initQingqiu() ;
                }else{
                    ToastUtil.showToast("提现金额不能为空");
                }

//                //判断不能小于10000
//                if (wallet < 3500) {
////                    Toast.makeText(ForwardActivity.this, "最少3500voc" , Toast.LENGTH_SHORT).show();
//                    ToastUtil.showToast("最少3500voc");
//
//                } else {
//                    if(Double.parseDouble(addressSnmd.getText().toString().trim()) <= use1) {
//                        showPayDialog(mwallet);
//                    }else {
//                        initQingqiu() ;
//                    }
//                }
            }
        });
//提币数量输入框
        addressSnmd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (Double.parseDouble(addressSnmd.getText().toString().trim()) > use1){
//                    addressSnmd.setText("");
//                }

                String moneyNum = addressSnmd.getText().toString().trim();
                if (addressSnmd.getText().toString().trim().matches("^0")) {//判断当前的输入第一个数是不是为0
                    addressSnmd.setText("");
                }
                if (!"".equals(addressSnmd.getText().toString())) {
                    if (moneyNum.length() >= 1) {
                        double v = Double.parseDouble(moneyNum);
                    }
                    if (moneyNum.length() < 1) {
                        moneyNum = "0";
                    }
//                    if ("0".equals(addressSnmd.getText().toString().trim().substring(0,1)))
//                    {
//                        addressSnmd.setText("");
//                    }
                    tvAssets.setText(moneyNum + "");
                    if (addressSnmd.getText().toString().trim().matches("^0")) {//判断当前的输入第一个数是不是为0
                        addressSnmd.setText("");
                        return;
                    }
                    if (Double.parseDouble(addressSnmd.getText().toString().trim()) > use1) {
//                        addressSnmd.setHint("超出可提现金额");
//                        addressSnmd.setHintTextColor(Color.parseColor("#B2FF0000"));
                        addressSnmd.setText("");
                        tvAssets.setText("0");
                    }
                }else {
                    tvAssets.setText("0");
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //地址输入框判断
        addressRemarks.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String remarks = s.toString();
                if (remarks.length() == 42) {
                    if (remarks.startsWith("0x")) {
                        if (Pattern.matches(qbUrl, remarks)) {
                            addressConfirm.setEnabled(true);
                        }
                    } else {
//                        Toast.makeText(ForwardActivity.this, "地址错误", Toast.LENGTH_SHORT).show();
                        ToastUtil.showToast("地址错误");

                    }
                } else {
                    addressConfirm.setEnabled(false);
                }


            }
        });

    }
    public void setMaxmumFilter(double maxmum, int numOfDecimal) {
        inputFilters.add(new MaxmumFilter(0, maxmum, numOfDecimal));
        addressSnmd.setFilters(inputFilters.toArray(new InputFilter[inputFilters.size()]));
    }
    private boolean isDialog = true;
//支付密码页面
    private void showPayDialog(String wallet) {
        dialog = new Dialog(ForwardActivity.this);
        //支付密码页面
        dialog.setContentView(R.layout.activity_payment);
        Window dialogWindow = dialog.getWindow();
        TextView tv_sum = dialogWindow.findViewById(R.id.tv_sum);
        tv_sum.setText(wallet);

        ImageView img_delete = dialogWindow.findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        p.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        startPayListen(dialogWindow);
        getResources();
    }

    //判断支付密码页面
    private void startPayListen(final Window dialogWindow) {
        final PasswordInputEdt edt = dialogWindow.findViewById(R.id.edt);
        Openkeybord(edt);
        edt.setOnInputOverListener(new PasswordInputEdt.onInputOverListenerss() {
            @Override
            public void onInputOver(String text) {
                if (!isEdit){
                    if ("".equals(text)) {
                        isEdit = !isEdit;
                        return;
                    }
                }
                if (text.length() == 6 && isDialog) {
                    isDialog = false;
                    Log.e("TAG", "onInputOver:------------- " + text + isEdit);
                    edt.setEnabled(false);
                    String sunm = addressSnmd.getText().toString();//voc
                    String remarks = addressRemarks.getText().toString();
                    ResetPwd3 pwd3 = new ResetPwd3(remarks, sunm, text);
                    RequestBodyWrapper wrapper = new RequestBodyWrapper(pwd3);
                    HttpManager.post(UrlConstantsKt.POST_RESET_THREE, wrapper).subscribe(new Subscriber<String>() {
                        @Override
                        public void onNext(String s) {
                            if (s == null || s.isEmpty()) return;
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("交易成功")) {
                                        Intent in = new Intent(ForwardActivity.this, ForwardTwoActivity.class);
                                        startActivity(in);
                                    } else if (msg.equals("密码不正确")) {
                                        //支付密码错误页面
                                        isDialog = false;
                                        final Dialog dialogr = new Dialog(ForwardActivity.this);
                                        dialogr.setContentView(R.layout.activity_payment_two);
                                        Window dialogWindow = dialogr.getWindow();
                                        TextView tv_retry = dialogWindow.findViewById(R.id.tv_retry);
                                        final TextView tv_forget = dialogWindow.findViewById(R.id.tv_forget);
                                        tv_forget.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VocApplication.Companion.getInstance().setResetPwd(true);
                                                Intent in = new Intent(ForwardActivity.this, GetMessageCodeActivity.class);
                                                PreferenceUtil.Companion.getInstance().set("istitle", "2");
                                                PreferenceUtil.Companion.getInstance().set("pwdFlag", "2");//忘记密码跳转
                                                dialog.dismiss();
                                                dialogr.dismiss();
                                                startActivity(in);
                                            }
                                        });
                                        tv_retry.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                isEdit = !isEdit;
                                                isDialog = true;
                                                dialogr.dismiss();
                                                edt.setText("");
                                                edt.closeText();
                                                edt.setEnabled(true);
                                            }
                                        });
                                        WindowManager m = getWindowManager();
                                        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                                        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                                        dialogWindow.setGravity(Gravity.CENTER);
                                        p.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.4
                                        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
                                        dialogWindow.setAttributes(p);
                                        getResources();
                                        dialogr.show();
                                        dialogr.setCanceledOnTouchOutside(false);
                                        dialogr.setCancelable(false);

                                    } else if (msg.equals("提现数量低于下限")){
                                        isDialog = true;
                                        dialog.dismiss();
                                        Toast.makeText(ForwardActivity.this, "提现数量低于最低下限", Toast.LENGTH_SHORT).show();
//                                        showPayDialog(mwallet);

                                    }else  if (msg.equals("同一个地址十天之内只能提现一次")){
                                        isDialog = true;
                                        dialog.dismiss();
                                        dialog = null;
                                        Toast.makeText(ForwardActivity.this, "同一个地址十天之内只能提现一次", Toast.LENGTH_SHORT).show();
//                                        showPayDialog(mwallet);

                                    }else {
                                        isDialog = true;
                                        dialog.dismiss();
                                        dialog = null;
                                        ToastUtil.showToast(msg);

                                    }
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
                }
            }
        });


//        final String string = "123456";
//        edt.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
//            @Override
//            public void onInputOver(String s) {
//
//                if (s.matches(string)) {
//                    //支付密码成功页面
//                    Toast.makeText(ForwardActivity.this, "伟大大发发", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    //支付密码错误页面
//                    final Dialog dialog = new Dialog(ForwardActivity.this);
//                    dialog.setContentView(R.layout.activity_payment_two);
//                    Window dialogWindow = dialog.getWindow();
//                    TextView tv_retry = dialogWindow.findViewById(R.id.tv_retry);
//                    final TextView tv_forget = dialogWindow.findViewById(R.id.tv_forget);
//                    tv_forget.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(ForwardActivity.this, "忘记密码", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    tv_retry.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    WindowManager m = getWindowManager();
//                    Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                    dialogWindow.setGravity(Gravity.CENTER);
//                    p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.4
//                    p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
//                    dialogWindow.setAttributes(p);
//                    dialog.show();
//                }
//            }
//        });
    }

    @NotNull
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.fontScale = 1.0f;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
    /**
     * 判断是否有地址
     */
    public void postResetPwd(){
        final LoadingDialog loadingDialog = new LoadingDialog(this, null);
        loadingDialog.show();
        String token = PreferenceUtil.Companion.getInstance().getString("TOKEN");
        isHaveAddress request = new isHaveAddress(token);
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(SECCE_SS,wrapper).subscribe(new Subscriber<String>(){
            @Override
            public void onNext(String s) {
                loadingDialog.dismiss();
                if (s == null || s.isEmpty()) return;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        if("用户还没有钱包地址".equals(jsonObject.getString("msg"))){
                            startActivity(new Intent(ForwardActivity.this, AddresActivity.class));
                        }else{
                            Intent intent = new Intent(ForwardActivity.this, AddresActivity2.class);
//                            setResult(200,intent);
                            intent.putExtra("position",position);
                            startActivityForResult(intent,200);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable t) {
                loadingDialog.dismiss();
            }
            @Override
            public void onComplete() {

                loadingDialog.dismiss();
            }
        });
    }
    public void Openkeybord (final EditText edt){
        edt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 50);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200){
            String addres = data.getStringExtra("addres");
            position = data.getIntExtra("position",0);
            if (addres != null)
                addressRemarks.setText(addres);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMoeny();
    }
    /**
     * 限制输入最大值
     */
    private static final class MaxmumFilter implements InputFilter {

        private final Pattern pattern;
        private final double maxNum;

        MaxmumFilter(int min, double maxNum, int numOfDecimals) {
            pattern = Pattern.compile("^" + (min < 0 ? "-?" : "")
                    + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
            this.maxNum = maxNum;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(".")) {
                if (dstart == 0 || !(dest.charAt(dstart - 1) >= '0' && dest.charAt(dstart - 1) <= '9') || dest.charAt(0) == '0') {
                    return "";
                }
            }
            if (source.equals("0") && (dest.toString()).contains(".") && dstart == 0) {
                return "";
            }

            StringBuilder builder = new StringBuilder(dest);
            builder.delete(dstart, dend);
            builder.insert(dstart, source);
            if (!pattern.matcher(builder.toString()).matches()) {
                return "";
            }

            if (!TextUtils.isEmpty(builder)) {
                double num = Double.parseDouble(builder.toString());
                if (num > maxNum) {
                    return "";
                }
            }
            return source;
        }
    }
//    直接对double进行处理，进行计算通过计算后的结果进行取模操作获取小数位数
    public int getNumberDecimalDigits(double number) {
        if (number == (long)number) {
            return 0;
        }
        int i = 0;
        while (true){
            i++;
            if (number * Math.pow(10, i) % 1 == 0) {
                return i;
            }
        }
    }


    private void getMoeny(){
        RequestBodyWrapper requestBodyWrapper = new RequestBodyWrapper(null);
        HttpManager.post(UrlConstantsKt.ZI_CHAN,requestBodyWrapper)
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onNext(String s) {
                        if (s == null)
                            return;
                        Gson gson = new Gson();
                        WalletClass walletClass = gson.fromJson(s, WalletClass.class);
                        if (walletClass.getCode() == 1){
                            WalletClass.MsgBean msg = walletClass.getMsg();
                            use1 = msg.getUse();
                            setMaxmumFilter(use1,8);
                            tvTxian.setText(use1+"");
                        }
//                        val wallet = gson.fromJson(t, WalletClass::class.java) ?: return
//                        if (wallet.code == 1) {
//                            val msg = wallet.msg
//                            val locking = msg.locking
//                            use = msg.use
//                            voc_coin = msg.getvoc_coin()
//                            wallet_tixian!!.text = use.toString()
//                            wallet_suoding!!.text = locking.toString()
//                            tv_coin!!.text = voc_coin.toString()
//                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
