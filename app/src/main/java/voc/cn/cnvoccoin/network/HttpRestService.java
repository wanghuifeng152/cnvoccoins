package voc.cn.cnvoccoin.network;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Method;

import voc.cn.cnvoccoin.VocApplication;
import voc.cn.cnvoccoin.util.PackageUtil;
import voc.cn.cnvoccoin.util.PreferenceUtil;


public class HttpRestService {
    private static HttpRestService sInstance = new HttpRestService();

    public static String SHOP_BIZ_PARAMS = "shop_biz_params" ;
    public static boolean isMock = false ;

    public static HttpRestService getInstance() {
        return sInstance;
    }

    private HttpRestService() {
        init(VocApplication.Companion.getInstance());
    }

    public void init(Context context) {
    }


    public static String getPackageName() {
        return PackageUtil.getPackageName();
    }

    public static String getVersionName() {
        return PackageUtil.getVersionName(VocApplication.Companion.getInstance());
    }

    public static int getVersionCode() {
        return PackageUtil.getVersionCode(VocApplication.Companion.getInstance());
    }

    public static String getDeviceId() {
        return getBizParam() != null ? getBizParam().deviceId : null;
    }

    public static String getChannelName() {
        return PackageUtil.getMetaData(VocApplication.Companion.getInstance(), "channel");
    }

    public static String getAccessToken() {
        return getBizParam() != null ? getBizParam().token : null;
    }

    public static long getTimestamp() {
        return ServerTime.getDefault().getTimeStamp();
    }

    public static String getPlatform() {
        return "android";
    }

    public static String getUserId() {
        return getBizParam() != null ? getBizParam().userId : null;
    }

    public static String getSunmiSN() {
        String snCode = null;
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            snCode = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snCode;
    }

    public static boolean intercept(int code, String tip) {
        if (mIntercepter != null) {
            return mIntercepter.intercept(code, tip) ;
        }
        return true ;
    }

    private static CommonIntercepter mIntercepter ;
    public static void setCommonIntercepter(CommonIntercepter commonIntercepter) {
        mIntercepter = commonIntercepter ;
    }

    public interface CommonIntercepter {
        boolean intercept(int code, String tip) ;
    }

    private static BizParam sBizParam ;
    public static void setBizParam (BizParam bizParam) {
        sBizParam = bizParam ;
    }

    private static BizParam getBizParam() {
        if (sBizParam == null) {
            String bizStr = PreferenceUtil.Companion.getInstance().getString(SHOP_BIZ_PARAMS) ;
            if (!TextUtils.isEmpty(bizStr)) {
                sBizParam = new Gson().fromJson(bizStr, BizParam.class) ;
            }
        }
        return sBizParam ;
    }

    public static class BizParam {
        public String deviceId;
        public String token ;
        public String userId ;

        public BizParam(String deviceId, String token, String userId) {
            this.deviceId = deviceId ;
            this.token = token ;
            this.userId = userId ;
        }
    }

}
