package voc.cn.cnvoccoin.network;


import com.orhanobut.logger.Logger;
import java.util.Map;

public class HttpManager {

  /**
   * GET请求
   *
   * @param url 请求path,可以是全路径
   * @return HttpCreate 创建一个请求
   */
  public static HttpCreate get(String url) {

    return new HttpGetCreate(url);
  }

  /**
   * GET请求
   *
   * @param url 请求path,可以是全路径
   * @param options 请求url参数
   * @return HttpCreate 创建一个请求
   */
  public static HttpCreate get(String url, Map<String, ?> options) {

    return new HttpGetCreate(url, options);
  }

  /**
   * GET请求
   *
   * @param url 请求path,可以是全路径
   * @param t 请求参数model
   * @return HttpCreate 创建一个请求
   */
  public static <T> HttpCreate get(String url, T t) {

    return new HttpGetCreate(url, t);
  }

  /**
   * POST请求
   *
   * @param url 请求path,可以是全路径
   * @param t 请求model
   * @return HttpCreate 创建一个请求
   */
  public static <T extends RequestBodyWrapper> HttpCreate post(String url, T t) {

    return new HttpPostCreate(url, t);
  }

  /**
   * POST请求
   *
   * @param url 请求path,可以是全路径
   * @param options 请求参数
   * @param t 请求model
   * @return HttpCreate 创建一个请求
   */
  public static <T> HttpCreate post(String url, Map<String, ?> options, T t) {
    return new HttpPostCreate(url, options, t);
  }

  /**
   * 自定义
   *
   * @param c 自定义请求构造
   * @return HttpCreate 创建一个请求
   */
  public static <C extends HttpCreate> C create(C c) {

    return c;
  }

//    /**
//     * 自定义
//     *
//     * @param sub
//     * @return HttpCreate 创建一个请求
//     */
//
//    public static <C> HttpCreate create(HttpOnSubscribe<C> sub) {
//
//        return new HttpGetCreate(sub);
//    }

  public static <T> T checkNotNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    } else {
      return object;
    }
  }

}