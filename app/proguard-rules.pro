# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/freedy/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarning
-renamesourcefileattribute SourceFile

#避免混淆Annotation，内部类、泛型、匿名类
-keepattributes *Annotation*, Exceptions, InnerClasses,Signature,EnclosingMethod

-keepclasseswithmembernames enum * {
    *;
}

-keep class android.os.**{*;} #for sunmi
-keep class android.support.** {*;}

-keep class cn.yonghui.shop.framework.base.KeepAttr
-keepclassmembers class * extends cn.yonghui.shop.framework.base.KeepAttr{*;}
-keepclassmembers interface * extends cn.yonghui.shop.framework.base.KeepAttr{*;}

-keepclasseswithmembers public class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keep class cn.yonghui.shop.shopapplication.BuildConfig{*;}

#kotlin
-dontwarn com.baidu.**
-keep class com.baidu.** {*;}

#kotlin
-dontwarn kotlin.jvm.
-keep class kotlin.jvm.**
#Gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

# OkHttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn retrofit.**
-keep class retrofit.**{*;}
-keepclassmembers class ** {
      @retrofit2.http.GET <methods>;
      @retrofit2.http.POST <methods>;
      @retrofit2.http.Streaming <methods>;
      @retrofit2.http.Url <methods>;
      @retrofit2.http.Multipart <methods>;
      @retrofit2.http.QueryMap <methods>;
      @retrofit2.http.Body <methods>;
      @retrofit2.http.Part <methods>;
  }
# RxJava RxAndroid
-dontwarn io.reactivex.**
-keep class io.reactivex.** {*;}
-dontwarn org.reactivestreams.**
-keep class org.reactivestreams.**{*;}
#fresco
-dontwarn com.facebook.**
-keep class com.facebook.** {*;}

#eventbus
-keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}

-dontwarn org.apache.**
-keep class org.apache.**{*;}
-dontwarn org.xmlpull.**
-keep class org.xmlpull.**{*;}
-dontwarn com.networkbench.**
-keep class com.networkbench.** { *; }


-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# release don't show log
-assumenosideeffects class android.util.Log {
	public static boolean isLoggable(java.lang.String, int);
	public static int v(...);
	public static int i(...);
	public static int w(...);
	public static int d(...);
	public static int e(...);
}

# growingio
-dontwarn com.growingio.android.sdk.**
-keep class com.growingio.android.sdk.** {
 *;
}

-keepclassmembers class * extends android.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
# growingio end

# bugly start
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# bugly end

####################XStream操作xml与Bean之间转换jar（开始）#####################
-dontwarn com.thoughtworks.xstream.**
-keep class com.thoughtworks.xstream.** {*;}
####################XStream操作xml与Bean之间转换jar（结束）#####################

#alipay start
-dontwarn cn.yonghui.paycenter.alipay.model.builder.**
-keep class cn.yonghui.paycenter.alipay.model.builder.** {*;}
#alipay end



# ProGuard configurations for NetworkBench Lens

-keep class com.networkbench.** { *; }

-dontwarn com.networkbench.**

-keepattributes Exceptions, Signature, InnerClasses

-keepattributes SourceFile,LineNumberTable

# End NetworkBench Lens


# mars xlog start
-keep class com.tencent.mars.** {
  public protected private *;
}
# mars xlog end

# ftp start
-dontwarn org.apache.commons.net.**
-keep class org.apache.commons.net.** { *; }
# ftp end

# jpush start
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
# jpush end

-keep class * extends cn.yonghui.shop.framework.bus.IBizBusObject {*;}
-keep class cn.yonghui.shop.framework.util.NetworkWatchUtil{*;}

-keep class com.tencent.ytcommon.**{*;}
-keep class com.tencent.youtufacetrack.**{*;}
-keep class com.serenegiant.**{*;}


