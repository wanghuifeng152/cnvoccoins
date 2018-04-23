package voc.cn.cnvoccoin.util;

import android.os.Build;
import android.view.View;

public class SystemUIUtils {
    public static final int SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED = 0x00004000; //USE FOR SUNMI

    public static void setStickFullScreen(View view) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            view.setSystemUiVisibility(View.GONE);
        } else {
            int systemUiVisibility = view.getSystemUiVisibility();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED;
            systemUiVisibility |= flags;
            view.setSystemUiVisibility(systemUiVisibility);
        }
    }
}