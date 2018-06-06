package voc.cn.cnvoccoin.activity;


import static voc.cn.cnvoccoin.util.ConstantsKt.PASSWORD;
import static voc.cn.cnvoccoin.util.ConstantsKt.TOKEN;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_ID;
import static voc.cn.cnvoccoin.util.ConstantsKt.USER_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.lqr.audio.AudioRecordManager;
import com.orhanobut.logger.Logger;
import java.io.File;
import org.jetbrains.annotations.Nullable;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.UploadVoiceBean;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.ResBaseModel;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.GetConfirmCodeRequest;
import voc.cn.cnvoccoin.util.LoginResponse;
import voc.cn.cnvoccoin.util.PreferenceUtil;
import voc.cn.cnvoccoin.util.ToastUtil;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.view.WaveLineView;

/**
 * describe:录音界面
 *
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

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voice);
    ButterKnife.bind(this);
    initRecord();
    initListener();


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
            break;
          //移动按钮时
          case MotionEvent.ACTION_MOVE:
            if (isCancelled(view, motionEvent)) {
              AudioRecordManager.getInstance(VoiceActivityNew.this).willCancelRecord();
              viewWave.stopAnim();
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
            break;
        }
        return true;
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
    GetConfirmCodeRequest request = new GetConfirmCodeRequest(String.valueOf(voice_id));
    RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
    HttpManager.post(UrlConstantsKt.UPLOAD_COIN, wrapper)
        .subscribe(new Subscriber<ResBaseModel<UploadVoiceBean>>() {


          @Override
          public void onNext(ResBaseModel<UploadVoiceBean> uploadVoiceBeanResBaseModel) {
            //成功
            //Logger.e("hello");

          }

          @Override
          public void onError(Throwable t) {
            //失败
            Logger.t("异常").e(t.getMessage().toString());
          }

          @Override
          public void onComplete() {
            //完成
          }
        }, UploadVoiceBean.class, ResBaseModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();
    //获取语句
    getReadCoin();
  }
}


