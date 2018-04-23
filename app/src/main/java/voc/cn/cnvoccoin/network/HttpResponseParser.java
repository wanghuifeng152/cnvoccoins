package voc.cn.cnvoccoin.network;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import voc.cn.cnvoccoin.util.ToastUtil;


public class HttpResponseParser {


    private static final String SOCKET_TIMEOUT_EXCEPTION = "请求超时";
    private static final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";
    private static final String UNKNOWN_HOST_EXCEPTION = "网络异常，请检查您的网络状态";


//    public static void parse(int code,String tip) {
//        switch (code) {
//            case StatusCode.DEVICE_NOT_AVAILABLE_ERROR_CODE:
//                Activity context = AppUtil.getCurrentActivity();
//                if(context != null){
//                    final YHDialog yhDialog = new YHDialog(context);
//                    yhDialog.setWidth(300);
//                    yhDialog.setCancelOnTouchOutside(false);
//                    yhDialog.setMessage(tip);
//                    yhDialog.setOnComfirmClick(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            yhDialog.dismiss();
//                            Intent intent = new Intent();
//                            intent.setAction("yh.action.LoginActivity");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            YHShopApplication.getInstance().startActivity(intent);
//                        }
//                    });
//                    yhDialog.setConfirm("确定");
//                    yhDialog.setCancel(null);
//                    yhDialog.setMessageColor("#000000");
//                    yhDialog.setConfirmColor("#0498F3");
//                    yhDialog.show();
//                }
//                break;
//            case StatusCode.INVALID_ACCESS_TOKEN_CODE:
//                ToastUtil.showToast("登录过期,请重新登录");
//                Intent intent = new Intent();
//                intent.setAction("yh.action.LoginActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                YHShopApplication.getInstance().startActivity(intent);
//                break;
//            case StatusCode.ERRORCODE_TOKEN_EXPIRED:
//                Activity activity = AppUtil.getCurrentActivity();
//                if (activity != null) {
//                    validateTime(activity);
//                }
//                break;
//            default:
//                //do not parse here
//                break;
//        }
//    }

    public static <T extends Throwable> void parse(T t) {
        String error = null;
        if (t instanceof SocketTimeoutException) {
            error = SOCKET_TIMEOUT_EXCEPTION;

        } else if (t instanceof ConnectException) {
            error = CONNECT_EXCEPTION;

        } else if (t instanceof UnknownHostException) {
            error = UNKNOWN_HOST_EXCEPTION;
        }
        if (!TextUtils.isEmpty(error)) {
            // TODO: 17/6/6 show toast 
            ToastUtil.showToast(error);
        }

    }

    /**
     * JSON解析
     *
     * @param str json字符串
     * @return ResBaseModel 返回根model
     */
    public static <T> ResBaseModel<T> toJsonResBaseModel(String str) {
        if (!TextUtils.isEmpty(str)) {
            ResBaseModel<T> model = toJsonParse(str);
            return model;
        }
        return null;
    }

    /**
     * JSON解析
     *
     * @param str json字符串
     * @param cls 返回model的class
     * @return T 返回cls的model
     */
    public static <T> T toJsonDataModel(String str, Class<T> cls) {
        if (!TextUtils.isEmpty(str)) {
            try {
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                if (jsonObject.has("data")) {
                    JsonObject data = jsonObject.getAsJsonObject("data");
                    return gson.fromJson(data, cls);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * JSON解析
     *
     * @param str json字符串
     * @return T 返回cls的model
     */
    public static <T> List<T> toJsonListModel(String str, Class<T> cls) {

        String jsonList = getData(str);
        if (!TextUtils.isEmpty(jsonList)) {
            try {
                ArrayList<T> list = new ArrayList<T>();
                Gson gson = new Gson();
                JsonArray array = new JsonParser().parse(jsonList).getAsJsonArray();
                for (final JsonElement elem : array) {
                    list.add(gson.fromJson(elem, cls));
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getData(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
                if (jsonObject.has("data")) {
                    return jsonObject.get("data").toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static <T> ResBaseModel<T> toJsonParse(String str) {
        ResBaseModel<T> model = new Gson().fromJson(str, new TypeToken<ResBaseModel<T>>() {
        }.getType());
     /*   if (model.now > 0) {
            ServerTime.getDefault().syncTimeStamp(model.now);
        }*/
        return model;
    }

    public static <T> T toJsonResBaseModel(String response, final Type type)

    {
        Gson gson = new Gson();
        Type resultType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return ResBaseModel.class;
            }
        };
        return gson.fromJson(response, resultType);
    }

    /**
     * token过期弹窗
     *
     * @return
     */
//    public static final int REFRESH_TOKEN = 1;
//
//    public static void validateTime(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(
//                ShopPreferenceConstant.PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(ShopPreferenceConstant.BAR_CODE);
//        editor.remove(ShopPreferenceConstant.MEMBER_POSTOKEN);
//        editor.commit();
//        final YHDialog yhDialog = new YHDialog(context);
//        yhDialog.setWidth(600);
//        yhDialog.setCancelOnTouchOutside(false);
//        yhDialog.setMessage(context.getString(R.string.token_past_due));
//        yhDialog.setOnComfirmClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yhDialog.dismiss();
//                Intent intent = new Intent();
//                intent.setAction("yh.action.ScanCardGuideActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("refresh_token", REFRESH_TOKEN);
//                YHShopApplication.getInstance().startActivity(intent);
//            }
//        });
//        yhDialog.setConfirm(context.getString(R.string.close_hint_know));
//        yhDialog.setCancel(null);
//        yhDialog.setMessageColor("#000000");
//        yhDialog.setConfirmColor("#0498F3");
//        yhDialog.show();
//    }
}
