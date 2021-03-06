package voc.cn.cnvoccoin.activity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.UploadVoiceBeans;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.AppUtils;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UploadCoinRequestVoc;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.Utils;
import voc.cn.cnvoccoin.view.WaveLineView;

/**
 * describe:录音界面啊啊啊啊所多付过
 * <p>
 * author: Gary
 */
public class VoiceActivityNew extends BaseActivity {
    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    public static int bufferSizeInBytes = 0;
    //上传币;
    private static final String UPLOAD_COIN = "/api/portal/voc/uploadVocCoinV2";

    //miui系统
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

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
    @BindView(R.id.voice_back)
    ImageView voiceBack;
    private File mAudioDir;
    private int voice_id = 0;
    private double voiceCoin = 0.00;
    String StrVersion;

    public long oldTime;
    public long newTime;


    private IAudioRecordListener listener;
    private Boolean isreodering;
    public boolean hasVoice;
    private DecimalFormat decimalFormat;
    private IntentFilter intentFilter;
    private Handler handler = new Handler();
    ImageView iv_back;
    TextView title_name;
    String strMD5;
    String sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
//        iv_back =findViewById(R.id.iv_back);
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        title_name =findViewById(R.id.title_name);
//        title_name.setText("");
        ButterKnife.bind(this);
        StrVersion = AppUtils.getVerName(this);
        initRecord();
        intentFilter = new IntentFilter();
        initListener();

        tvHaveCoin.setText("" + voiceCoin);
        long time = System.currentTimeMillis();
        Log.i("msg", time + "");
        strMD5 = Utils.md5("6f994ec9a2be0d9934b2b2057e4e1a25Android");

        String t = Long.toString(time).substring(0, 10);
        sign = Utils.md5(t) + "#" + t;
        getReadCoin();
        voiceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 判断是是否有录音权限
     */
    public boolean checkAudioPermission(final Context context) {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            return false;
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING
                && audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
            return false;
        }


        audioRecord.stop();
        audioRecord.release();

        return true;
    }
    //处理触摸事件

    private synchronized void initListener() {

        ivVoice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (checkAudioPermission(VoiceActivityNew.this)) {
                    switch (motionEvent.getAction()) {
                        //按下按钮时 开始录音
                        case MotionEvent.ACTION_DOWN:
                            AudioRecordManager.getInstance(VoiceActivityNew.this).startRecord();
                            viewWave.stopAnim();
                            viewWave.startAnim();
                            isreodering = true;
                            hasVoice = false;
                            //开始录制时间
                            oldTime = System.currentTimeMillis();
                            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                            registerReceiver(broadcastReceiver, intentFilter);
                            break;
                        //移动按钮时
                        case MotionEvent.ACTION_MOVE:
                            if (isCancelled(view, motionEvent)) {
                                AudioRecordManager.getInstance(VoiceActivityNew.this).willCancelRecord();
                                viewWave.stopAnim();
                                isreodering = false;
                                hasVoice = false;
                                viewWave.clearDraw();
                                ToastUtil.showToast("已取消~");

                            } else {
                                AudioRecordManager.getInstance(VoiceActivityNew.this).continueRecord();
                            }
                            break;
                        //释放按钮时
                        case MotionEvent.ACTION_UP:
                            //停止录音
                            AudioRecordManager.getInstance(VoiceActivityNew.this).stopRecord();
                            //销毁录音
                            AudioRecordManager.getInstance(VoiceActivityNew.this).destroyRecord();
                            viewWave.stopAnim();
                            viewWave.clearDraw();

                            //录制完毕时间
                            newTime = System.currentTimeMillis();
                            ivVoice.setEnabled(false);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ivVoice.setEnabled(true);
                                }

                            }, 500);
                            if (newTime - oldTime < 1000) {
                                ToastUtil.showToast("录音时间太短了哦");
                                hasVoice = false;

                            } else if (hasVoice == false && isreodering == true) {
                                ToastUtil.showToast("声音再大一些");
                                hasVoice = false;

                            } else if (newTime - oldTime >= 1000 && hasVoice == true) {
                                getReadCoin();
                                hasVoice = false;
                            }
                            break;
                    }
                } else {
//                    Toast.makeText(VoiceActivityNew.this, "没有录音权限", Toast.LENGTH_SHORT).show();
                    ToastUtil.showToast("没有录音权限");
                    String[] perms = {
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(VoiceActivityNew.this, perms, 1);
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

                if (db > 2) {
                    hasVoice = true;
                }
            }

        });


    }

    private void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    //判断是否熄屏
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                //停止录音
                AudioRecordManager.getInstance(VoiceActivityNew.this).stopRecord();
                //销毁录音
                AudioRecordManager.getInstance(VoiceActivityNew.this).destroyRecord();
                //销毁动画
                viewWave.stopAnim();
                viewWave.clearDraw();
            }
        }
    };

    //初始化录音机

    private void initRecord() {
        //设定录音最长时间
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(120);
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

    //网络请求//网络请求
//    UrlConstantsKt.UPLOAD_COIN
    private void getReadCoin() {
/**
 *《---------------------------------------------==- 刷币 -==---------------------------------------------》
 */

//        Map<String, Object> parameter = new HashMap<>();
//        parameter.put("deviceId", SmAntiFraud.getDeviceId());
//        parameter.put("id", String.valueOf(voice_id));
//        parameter.put("platform", "Android");
//        parameter.put("version", StrVersion);
//        RetrofitUtils.getInstance().getService(PageService.class).getPage(parameter)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResBaseModel<UploadVoiceBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ResBaseModel<UploadVoiceBean> uploadVoiceBeanResBaseModel) {
////                        成功
//                        if (uploadVoiceBeanResBaseModel == null || uploadVoiceBeanResBaseModel.data == null) {
//                            return;
//                        }
//                        if (uploadVoiceBeanResBaseModel.code != 1) {
//                            return;
//                        }
//                        tvVoiceText.setText(uploadVoiceBeanResBaseModel.data.getNext().getContent());
//                        if (voice_id != 0) {
//                            BigDecimal b1 = new BigDecimal(voiceCoin);
//                            BigDecimal b2 = new BigDecimal(Double.valueOf(uploadVoiceBeanResBaseModel.data.getNext().getVoc_coin()));
//                            //数据格式化为小数点后两位
//                            //高精度计算
//                            //voiceCoin = b1.add(b2).setScale(2, RoundingMode.DOWN).doubleValue();
//                            voiceCoin = b1.add(b2).doubleValue();
//                        }
//                        voice_id = uploadVoiceBeanResBaseModel.data.getNext().getId();
////                        LovelyToast.makeText(VoiceActivityNew.this,decimalFormat.format(voiceCoin)+"",LovelyToast.LENGTH_SHORT,LovelyToast.SUCCESS);
//                        if (decimalFormat == null)
//
//                        tvHaveCoin.setText(new DecimalFormat("0.00").format(voiceCoin) + "");
//                        sign = uploadVoiceBeanResBaseModel.data.getSign();
//                        hasVoice = false;
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("aaa", e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        UploadCoinRequestVoc android = new UploadCoinRequestVoc(String.valueOf(voice_id), StrVersion, "Android", SmAntiFraud.getDeviceId());
        RequestBodyWrapper wrapper = new RequestBodyWrapper(android);
        HttpManager.post(UrlConstantsKt.UPLOAD_COIN, wrapper)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        UploadVoiceBeans uploadVoiceBeanResBaseModel = new Gson().fromJson(s, UploadVoiceBeans.class);

//                        成功
                        if (uploadVoiceBeanResBaseModel == null || uploadVoiceBeanResBaseModel.getData() == null) {
                            return;
                        }
                        if (uploadVoiceBeanResBaseModel.getCode() != 1) {
                            return;
                        }
                        tvVoiceText.setText(uploadVoiceBeanResBaseModel.getData().getNext().getContent());
                        if (voice_id != 0) {
                            BigDecimal b1 = new BigDecimal(voiceCoin);
                            BigDecimal b2 = new BigDecimal(Double.valueOf(uploadVoiceBeanResBaseModel.getData().getNext().getVoc_coin()));
                            //数据格式化为小数点后两位
                            //高精度计算
                            //voiceCoin = b1.add(b2).setScale(2, RoundingMode.DOWN).doubleValue();
                            voiceCoin = b1.add(b2).doubleValue();
                        }
                        voice_id = uploadVoiceBeanResBaseModel.getData().getNext().getId();
//                        LovelyToast.makeText(VoiceActivityNew.this,decimalFormat.format(voiceCoin)+"",LovelyToast.LENGTH_SHORT,LovelyToast.SUCCESS);
                        if (decimalFormat == null)

                            tvHaveCoin.setText(new DecimalFormat("0.00").format(voiceCoin) + "");
                        sign = uploadVoiceBeanResBaseModel.getData().getSign();
                        hasVoice = false;
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t != null) {
                            Log.e("TAG", "onError:a+++++++++++++++++++++++++++++b " + t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        new Observer<ResBaseModel<UploadVoiceBean>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(ResBaseModel<UploadVoiceBean> uploadVoiceBeanResBaseModel) {
//                //成功
//                if (uploadVoiceBeanResBaseModel == null || uploadVoiceBeanResBaseModel.data == null) {
//                    return;
//                }
//                if (uploadVoiceBeanResBaseModel.code != 1) {
//                    return;
//                }
//                tvVoiceText.setText(uploadVoiceBeanResBaseModel.data.getNext().getContent());
//                if (voice_id != 0) {
//                    BigDecimal b1 = new BigDecimal(voiceCoin);
//                    BigDecimal b2 = new BigDecimal(
//                            Double.valueOf(uploadVoiceBeanResBaseModel.data.getNext().getVoc_coin()));
//                    //数据格式化为小数点后两位
//                    decimalFormat = new DecimalFormat("0.00");
//                    //高精度计算
//                    //voiceCoin = b1.add(b2).setScale(2, RoundingMode.DOWN).doubleValue();
//                    voiceCoin = b1.add(b2).doubleValue();
//                }
//                voice_id = uploadVoiceBeanResBaseModel.data.getNext().getId();
////                        LovelyToast.makeText(VoiceActivityNew.this,decimalFormat.format(voiceCoin)+"",LovelyToast.LENGTH_SHORT,LovelyToast.SUCCESS);
//                tvHaveCoin.setText(decimalFormat.format(voiceCoin) + "");
//                sign = uploadVoiceBeanResBaseModel.data.getSign();
//                hasVoice = false;
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("aaaaa",e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //获取语句
        //       getReadCoin();
    }

    //上传音频
    private void loadFile(final File file) {

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
    }

    @Override
    protected void onPause() {
        viewWave.stopAnim();
        isreodering = false;
        hasVoice = false;
        viewWave.clearDraw();
        super.onPause();
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

}


