package voc.cn.cnvoccoin.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    private  static OkHttpUtils okHttpUtils;
    private OkHttpClient okHttpClient;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpUtils() {
        okHttpClient =  new OkHttpClient.Builder().build();
    }

    public static OkHttpUtils getInstens(){
        if (okHttpUtils == null){
            synchronized (OkHttpUtils.class){
                if (okHttpUtils == null){
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }
    public Call postHttp(String url,String json){
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).build();
        return okHttpClient.newCall(request);
    }
    /**
    *《---------------------------------------------==- 获取当前IP -==---------------------------------------------》
    */
    public String getIP(){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }
    /**
     *《---------------------------------------------==- 获取当前时间Long -==---------------------------------------------》
     */
    public long getTimer(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = new Date();
        try {
            Date parse = df.parse(df.format(date));
            long time = parse.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
