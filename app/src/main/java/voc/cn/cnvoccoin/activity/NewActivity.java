package voc.cn.cnvoccoin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import voc.cn.cnvoccoin.R;

public class NewActivity extends AppCompatActivity {
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        mUnBinder = ButterKnife.bind(this);
        loadImg();
        initivBack();
    }

    private void initivBack() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadImg() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int imageWidth = px2dp(screenWidth - 40);
        int imageHeight = px2dp(283);
        img1.setImageBitmap(compressBitmap(R.mipmap.chart, imageWidth, imageHeight));
        img2.setImageBitmap(compressBitmap(R.mipmap.chart2, imageWidth, imageHeight));
    }


    @Override
    protected void onDestroy() {
        if (mUnBinder != null)
            mUnBinder.unbind();
        super.onDestroy();
    }


    private int px2dp(int pxValue) {
        int dpValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, getResources()
                .getDisplayMetrics());
        return dpValue;
    }

    private Bitmap compressBitmap(int bitmapId, int imageWidth, int imageHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId, options);

        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        int sampleSize = 1;
        while (outWidth / sampleSize > imageWidth || outHeight / sampleSize > imageHeight) {
            sampleSize *= 3;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), bitmapId, options);
        return bitmap;
    }

}
