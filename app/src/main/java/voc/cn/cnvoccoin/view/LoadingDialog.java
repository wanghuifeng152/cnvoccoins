package voc.cn.cnvoccoin.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.util.SystemUIUtils;


/**
 * Created by kuangxiaomin on 2017/6/12.
 */

public class LoadingDialog extends Dialog {
    private TextView mTvLoaidng;
    private Context mContext;
    private GifView mGif;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.loading_dialog);
        mContext=context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        setContentView(v);
        mGif = (GifView)v.findViewById(R.id.gif);
        mGif.setMovieResource(R.raw.loading);
        mTvLoaidng = (TextView)v.findViewById(R.id.tv_login_text);
        if(TextUtils.isEmpty(msg)){
            mTvLoaidng.setVisibility(View.GONE);
        }else {
            mTvLoaidng.setVisibility(View.VISIBLE);
            mTvLoaidng.setText(msg);
        }

        setCanceledOnTouchOutside(false);
//        SystemUIUtils.setStickFullScreen(getWindow().getDecorView());
    }
}
