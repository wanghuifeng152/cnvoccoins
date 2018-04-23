package voc.cn.cnvoccoin.view;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.util.SystemUIUtils;
import voc.cn.cnvoccoin.util.Utils;


public class YHShopDialog extends AppCompatDialog {

    Context context;
    TextView mTitle;
    TextView mMsg;
    TextView mConfirm;
    TextView mCancel;
    boolean mCanDismissByClick = true;

    public YHShopDialog(Context context) {
        super(context);
        init(context);
        SystemUIUtils.setStickFullScreen(getWindow().getDecorView());
    }

    public YHShopDialog(Context context, int Theme) {
        super(context, Theme);
        init(context);
    }


    protected int getDialogResId() {
        return R.layout.view_layout_materialdialog;
    }


    protected void initSubView() {
    }

    ;

    void init(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(getDialogResId(), null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        mTitle = (TextView) view.findViewById(R.id.md_title);
        mMsg = (TextView) view.findViewById(R.id.md_message);
        mConfirm = (TextView) view.findViewById(R.id.md_confirm);
        mCancel = (TextView) view.findViewById(R.id.md_cancel);
        setOnComfirmClick(null);
        setOnCancelClick(null);
        initSubView();
    }

    public YHShopDialog setMessageColor(String colorStr) {
        if (mMsg != null) mMsg.setTextColor(Color.parseColor(colorStr));
        return this;
    }

    public YHShopDialog setMessage(CharSequence string) {
        if (mMsg != null) mMsg.setText(string);
        return this;
    }

    public YHShopDialog setMessage(@StringRes int resid) {
        if (mMsg != null) mMsg.setText(context.getString(resid));
        return this;
    }

    public YHShopDialog setMessageGravity(int gravity) {
        if (mMsg != null) mMsg.setGravity(gravity);
        return this;
    }

    public YHShopDialog setOnComfirmClick(final View.OnClickListener listener) {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                if (mCanDismissByClick) dismiss();
            }
        });

        return this;
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(mCancel.getText().toString())) {
            mCancel.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(mConfirm.getText().toString())) {
            mConfirm.setVisibility(View.INVISIBLE);
        }
        super.show();
    }


    public YHShopDialog setOnCancelClick(final View.OnClickListener listener) {

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                if (mCanDismissByClick) dismiss();
            }
        });

        return this;
    }

    public YHShopDialog setDialogTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        return this;
    }


    public YHShopDialog setDialogTitle(@StringRes int resid) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(context.getString(resid));
        return this;
    }

    @Deprecated
    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Deprecated
    @Override
    public void setTitle(@Nullable CharSequence title) {
        setTitle(title);
    }

    public YHShopDialog setWidth(float dpValue) {
        Window window = getWindow();
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = (int) Utils.dip2px(context, dpValue);
        window.setAttributes(p);
        return this;
    }

    public YHShopDialog setConfirmColor(String colorStr) {
        mConfirm.setTextColor(Color.parseColor(colorStr));
        return this;
    }

    public YHShopDialog setConfirm(String confirm) {
        mConfirm.setText(confirm);
        return this;
    }

    public YHShopDialog setConfirm(@StringRes int resid) {
        mConfirm.setText(context.getString(resid));
        return this;
    }

    public YHShopDialog setCancel(String cancel) {
        mCancel.setText(cancel);
        return this;
    }

    public YHShopDialog setCancel(@StringRes int resid) {
        mCancel.setText(context.getString(resid));
        return this;
    }

    public YHShopDialog setWarningMode() {
        mCancel.setVisibility(View.GONE);
        setOnComfirmClick(null);
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !mCanDismissByClick) return true;
        else
            return super.onKeyDown(keyCode, event);
    }

    public YHShopDialog dismissByClick(boolean mCanDismiss) {
        this.mCanDismissByClick = mCanDismiss;
        return this;
    }

    public TextView getMsg() {
        return mMsg;
    }


    public static int getConfirmId() {
        return R.id.md_confirm;
    }

    public static int getCancelId() {
        return R.id.md_cancel;
    }
}


