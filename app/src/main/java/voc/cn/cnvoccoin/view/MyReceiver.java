package voc.cn.cnvoccoin.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.activity.CommnutityActivity;
import voc.cn.cnvoccoin.activity.LoginActivity;
import voc.cn.cnvoccoin.activity.LoginActivityNew;
import voc.cn.cnvoccoin.activity.MainActivity;
import voc.cn.cnvoccoin.util.NotifyUtil;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
     
    private NotificationManager nm;
     
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
         
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Logger.d(TAG, "JPush用户注册成功");
             
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG, "接受到推送下来的自定义消息");
                     
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG, "接受到推送下来的通知");
     
            receivingNotification(context,bundle);
 
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.d(TAG, "用户点击打开了通知");
        
           openNotification(context,bundle);
 
        } else {
            Logger.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
 
   private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Logger.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Logger.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.d(TAG, "extras : " + extras);
    }
 
   private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("type");
        } catch (Exception e) {
            Logger.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        if ("1".equals(myValue)) {
            Intent mIntent = new Intent(context, CommnutityActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }

}