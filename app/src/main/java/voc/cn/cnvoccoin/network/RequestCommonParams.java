package voc.cn.cnvoccoin.network;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import voc.cn.cnvoccoin.util.PreferenceUtil;

import static voc.cn.cnvoccoin.util.ConstantsKt.TOKEN;

public class RequestCommonParams {

    public static Map<String, String> getCommonParameters() {
        Map<String, String> map = new HashMap<>();
        String token = PreferenceUtil.Companion.getInstance().getString(TOKEN);
//        String platform = HttpRestService.getPlatform();
        String channel = HttpRestService.getChannelName();
        String versionName = HttpRestService.getVersionName();
        String deviceId = HttpRestService.getDeviceId();
  /*      if (!TextUtils.isEmpty(token)) {
            map.put("XX-Token", token);
        }*/
        if (!TextUtils.isEmpty(channel)) {
            map.put("channel", channel);
        }
        if (!TextUtils.isEmpty(deviceId)) {
            map.put("deviceid", deviceId);
        }

        if (!TextUtils.isEmpty(versionName)) {
            map.put("version", versionName);
        }
//        map.put("XX-Device-Type","mobile");
        map.put("timestamp", String.valueOf(ServerTime.getDefault().getTimeStamp()));

        return map;
    }

//    /**
//     * GET请求
//     *
//     * @param t url拼接参数
//     * @return Map
//     */
//    public static <T> Map<String, Object> getParameter(final T t) {
//        StringBuilder stringBuilder = RequestCommonParams.getParamBySort(getParameterIncludeCommon(t));
//        String sign = stringBuilder.toString();
//        YHLog.i("---->sign-- " + sign);
//        RequestCommonParams.sign = Security.signParams(sign);
//        Map<String, Object> map = new HashMap<>();
//        map.putAll(convertModelToMap(t));
//        return map;
//    }

//    /**
//     * POST请求
//     *
//     * @param t1 url拼接参数
//     * @param t2 请求Body
//     * @return RequestBody
//     */
//    public static <T1, T2> RequestBody getParameter(final T1 t1, final T2 t2) {
//        StringBuilder stringBuilder = RequestCommonParams.getParamBySort(getParameterIncludeCommon(t1));
//        Gson gson = new Gson();
//        String body = gson.toJson(t2);
//        Log.i("retrofit", "---->body-- " + body);
//        String sign = stringBuilder.append(body).toString();
//        Log.i("retrofit", "---->sign-- " + sign);
//        RequestCommonParams.sign = Security.signParams(sign);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
//        return requestBody;
//    }

//    /**
//     * POST请求
//     *
//     * @param map url拼接参数
//     * @param t2  请求Body
//     * @return RequestBody
//     */
//    public static <T2> RequestBody getParameter(final Map<String, Object> map, final T2 t2) {
//        StringBuilder stringBuilder = RequestCommonParams.getParamBySort(getCombine(map));
//        Gson gson = new Gson();
//        String body = gson.toJson(t2);
//        Log.i("retrofit", "---->body-- " + body);
//        String sign = stringBuilder.append(body).toString();
//        Log.i("retrofit", "---->sign-- " + sign);
//        RequestCommonParams.sign = Security.signParams(sign);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
//        return requestBody;
//    }

//    public static <R> Map<String, Object> getParameterIncludeCommon(final R t) {
//        Map<String, Object> map = getCommonParameters();
//        map.putAll(convertModelToMap(t));
//        return map ;
//    }

    public static <R> Map<String, Object> convertModelToMap(final R t) {
        Map<String, Object> map = new HashMap<>() ;
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            try {
                if (field.isSynthetic()) {
                    continue;
                }
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                Class<?> type = field.getType();
                String key = field.getName();
                Object value = field.get(t);
                if (value == null || (type.isAssignableFrom(String.class) && ((String) value).isEmpty())) {
                    continue;
                }
//                String encode = URLEncoder.encode(String.valueOf(value), "UTF-8");
                map.put(key, value);

            } catch (IllegalAccessException e) {
                continue;
            }
        }

        return map;

    }

    public static StringBuilder getParamBySort(Map<String, String> map) {
        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, ?> entry = list.get(i);
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return sb;
    }

//    public static <V> Map<String, Object> getCombine(Map<? extends String, ? extends V> m) {
//        Map<String, Object> map = getCommonParameters();
//        for (Map.Entry<? extends String, ? extends V> e : m.entrySet()) {
//            map.put(e.getKey(), e.getValue());
//        }
//        return map;
//    }

}

