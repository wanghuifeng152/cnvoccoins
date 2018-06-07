package voc.cn.cnvoccoin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import voc.cn.cnvoccoin.R;

/**
 * Created by shy on 2018/6/5.
 */

public class RecordingService  {
    public String mFileName;
    public String mFilePath;
    MediaRecorder mediaRecorder;
    long mStartingTimeMillis;
    long mElapsedMillis;
    File f;
    //通过binder实现了 调用者（client）与 service之间的通信
//    private MyBinder binder = new MyBinder();

/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }*/

    private void startRecording() {
        setFileNameAndPath();
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//mp4
            mediaRecorder.setOutputFile(mFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioChannels(1);
            // 设置录音文件的清晰度
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setAudioEncodingBitRate(192000);
        }

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
        }catch (IllegalStateException e){
        }

    }

    public MediaRecorder getMideaRecoder(){
        return mediaRecorder;
    }

    // 停止录音
    public void stopRecording() {
        mediaRecorder.stop();
        mElapsedMillis = System.currentTimeMillis() - mStartingTimeMillis;
        mediaRecorder.release();
      /*  getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                .edit()
                .putString("audio_path", mFilePath)
                .putLong("elpased", mElapsedMillis)
                .apply();*/
    }

    // 设置录音文件的名字和保存路径
    public void setFileNameAndPath() {
        mFileName = /*getString(R.string.default_file_name)
                + */"_" + ".mp4";
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/SoundRecorder/" + mFileName;
        f = new File(mFilePath);
        if (f.exists()) {
            f.delete();
        }
    }

/*    @Override
    public void onDestroy() {
        if (mediaRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }*/

    public  void onRecord(Context context, boolean start) {
        Intent intent = new Intent(context, RecordingService.class);
        if (start) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                folder.mkdir();
            }
       /*     context.startService(intent);
            context.bindService(intent, connection, BIND_AUTO_CREATE);*/
            startRecording();

        } else {
       /*     context.stopService(intent);
            context.unbindService(connection);*/
       stopRecording();
        }
    }


/*    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }*/

/*    public class MyBinder extends Binder{
        public RecordingService getService(){
            return RecordingService.this;
        }
    }*/
}



