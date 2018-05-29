package voc.cn.cnvoccoin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.ClipboardManager;// lina

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;

import static voc.cn.cnvoccoin.activity.Constant.IS_GRANTED_PERMISSION;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_ID;


/**
 * Created by Administrator on 2018/5/3.
 */

public class InvitationActivity extends AppCompatActivity {
    private RelativeLayout rl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
//        final TextView tvCode=findViewById(R.id.tv_code);
//        if(PreferenceUtil.Companion.getInstance().getInt(USER_ID,0) > 0){
//            tvCode.setText(PreferenceUtil.Companion.getInstance().getInt(USER_ID,0)+"");
//        }
//        rl=findViewById(R.id.rl);
//        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        findViewById(R.id.copyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //截屏并保存到相册
                pastInvteLink();
            }
        });
    }

    private void pastInvteLink(){

//        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//
//        cm.setText("hahahhahahah要复制的内容");
//        ToastUtil.showToast("复制成功，去粘贴吧~");

    }

    private Bitmap saveBitmap;
    /**
     * 顶部裁剪坐标
     */
    private int mCutTop;
    /**
     * 左侧裁剪坐标
     */
    private int mCutLeft;

    private void screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null) {
            try {
                //二次截图
                saveBitmap = Bitmap.createBitmap(rl.getWidth(), rl.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(saveBitmap);
                Paint paint = new Paint();

                canvas.drawBitmap(bmp, new Rect(mCutLeft, mCutTop, mCutLeft + rl.getWidth(), mCutTop + rl.getHeight()),
                        new Rect(0, 0, rl.getWidth(), rl.getHeight()), paint);
                File imageDir = new File(Constant.IMAGE_DIR);
                if (!imageDir.exists()) {
                    imageDir.mkdir();
                }
                String imageName = Constant.SCREEN_SHOT;
                File file = new File(imageDir, imageName);
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream os = new FileOutputStream(file);
                saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();

                //将截图保存至相册并广播通知系统刷新
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(PreferenceUtil.Companion.getInstance().getBoolean(IS_GRANTED_PERMISSION,false)){
                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), imageName, null);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
                        sendBroadcast(intent);

                        ToastUtil.showToast("保存成功");
                    }else{
                        ToastUtil.showToast("没有权限，请去设置中开启");
                    }
                }else{
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), imageName, null);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
                    sendBroadcast(intent);
                    ToastUtil.showToast("保存成功");
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast("保存失败");
        }

    }
//    private int[] mSavePositions = new int[2];
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        if (hasFocus) {
//            rl.getLocationOnScreen(mSavePositions);
//            mCutLeft = mSavePositions[0];
//            mCutTop = mSavePositions[1];
//
//        }
//    }

}
