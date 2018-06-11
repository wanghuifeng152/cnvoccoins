package voc.cn.cnvoccoin.activity;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.UploadVoiceBean;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.ResBaseModel;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UploadCoinRequest;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.view.WaveLineView;

/**
 * describe:录音界面
 * <p>
 * author: Gary
 */
public class VoiceActivityNew extends BaseActivity {

    //上传币;
    private static final String UPLOAD_COIN = "/api/portal/voc/uploadVocCoinV2";
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_have_coin)
    TextView tvHaveCoin;
    @BindView(R.id.ll_voice_top)
    LinearLayout llVoiceTop;
    @BindView(R.id.tv_voice_text)
    TextView tvVoiceText;
    @BindView(R.id.view_wave)
    WaveLineView viewWave;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    private File mAudioDir;
    private int voice_id = 0;
    private double voiceCoin = 0.00;


    public long oldTime;
    public long newTime;


    private IAudioRecordListener listener;
    private Boolean isreodering;
    public boolean hasVoice;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        initRecord();
        initListener();
        tvHaveCoin.setText("" + voiceCoin);


    }

    //处理触摸事件

    private void initListener() {

        ivVoice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    //按下按钮时
                    case MotionEvent.ACTION_DOWN:
                        AudioRecordManager.getInstance(VoiceActivityNew.this).startRecord();
                        viewWave.stopAnim();
                        viewWave.startAnim();
                        isreodering = true;
                        oldTime = java.lang.System.currentTimeMillis();

                        break;
                    //移动按钮时
                    case MotionEvent.ACTION_MOVE:
                        if (isCancelled(view, motionEvent)) {
                            AudioRecordManager.getInstance(VoiceActivityNew.this).willCancelRecord();
                            viewWave.stopAnim();
                            isreodering = false;
                            hasVoice = false;

                        } else {
                            AudioRecordManager.getInstance(VoiceActivityNew.this).continueRecord();
                        }
                        break;
                    //释放按钮时
                    case MotionEvent.ACTION_UP:
                        AudioRecordManager.getInstance(VoiceActivityNew.this).stopRecord();
                        AudioRecordManager.getInstance(VoiceActivityNew.this).destroyRecord();
                        viewWave.stopAnim();
                        viewWave.clearDraw();
                        newTime = java.lang.System.currentTimeMillis();

                        if (newTime - oldTime < 1000) {
                            ToastUtil.showToast("录音时间太短了哦");
                        }
     /*       else if (hasVoice == false) {
              ToastUtil.showToast("声音再大一些");
            }*/
                        else {
                            getReadCoin();
                            hasVoice = false;
                        }
                        break;
                }
                return true;
            }
        });

        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {
            @Override
            public void initTipView() {

            }

            @Override
            public void setTimeoutTipView(int counter) {

            }

            @Override
            public void setRecordingTipView() {

            }

            @Override
            public void setAudioShortTipView() {

            }

            @Override
            public void setCancelTipView() {

            }

            @Override
            public void destroyTipView() {

            }

            @Override
            public void onStartRecord() {

            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                try {
                    if (!TextUtils.isEmpty(audioPath.toString()))

                    {
                        File file = new File(new URI(audioPath.toString()));
                        //loadFile(file);
                    }

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onAudioDBChanged(int db) {

                Logger.t("db").e("db-------->" + db);

                if (db > 4) {
                    hasVoice = true;
                }
            }

        });


    }
    //初始化录音机

    private void initRecord() {
        //设定录音最长时间
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(12);
        //创建本地文件目录
        mAudioDir = new File(Environment.getExternalStorageDirectory(), "voc_record");
        if (!mAudioDir.exists()) {
            mAudioDir.mkdirs();
        }
        AudioRecordManager.getInstance(this).setAudioSavePath(mAudioDir.getAbsolutePath());

    }

    //判断是否取消(移动位置)
    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    //网络请求
    private void getReadCoin() {
        //参数转换
        UploadCoinRequest request = new UploadCoinRequest(String.valueOf(voice_id));
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.UPLOAD_COIN, wrapper)
                .subscribe(new Subscriber<ResBaseModel<UploadVoiceBean>>() {

                    @Override
                    public void onNext(ResBaseModel<UploadVoiceBean> uploadVoiceBeanResBaseModel) {
                        //成功
                        if (uploadVoiceBeanResBaseModel == null || uploadVoiceBeanResBaseModel.data == null) {
                            return;
                        }
                        if (uploadVoiceBeanResBaseModel.code != 1) {
                            return;
                        }
                        tvVoiceText.setText(uploadVoiceBeanResBaseModel.data.getNext().getContent());
                        if (voice_id != 0) {
                            BigDecimal b1 = new BigDecimal(voiceCoin);
                            BigDecimal b2 = new BigDecimal(
                                    Double.valueOf(uploadVoiceBeanResBaseModel.data.getNext().getVoc_coin()));
                            //数据格式化为小数点后两位
                            decimalFormat = new DecimalFormat("#.00");
                            //高精度计算
                            //voiceCoin = b1.add(b2).setScale(2, RoundingMode.DOWN).doubleValue();

                            voiceCoin = b1.add(b2).doubleValue();

                        }
                        voice_id = uploadVoiceBeanResBaseModel.data.getNext().getId();

                        tvHaveCoin.setText(decimalFormat.format(voiceCoin) + "");
                    }

                    @Override
                    public void onError(Throwable t) {
                        //失败
                    }

                    @Override
                    public void onComplete() {
                    }
                }, UploadVoiceBean.class, ResBaseModel.class);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //获取语句
        getReadCoin();
    }

    //上传音频
/*    private void loadFile(final File file) {

        OkGo.<String>post("http://www.vochain.world/api/user/public/saveVoice")//
                .tag(this)//

                .params(null, file)//

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        Logger.t("file").e(file.getAbsolutePath());

                        Logger.t("su").e(response.body().toString());

                    }
                });
    }*/
}


