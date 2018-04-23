package voc.cn.cnvoccoin.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import voc.cn.cnvoccoin.VocApplication;

public class ToastUtil {
    /**
     * 正在显示的提示。
     */
    private static Toast showingToast = null;


    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 null 或者空白则隐藏当前显示。
     * @since 1.0
     */
    public static void showToast(CharSequence text) {
        /*
         * 检查是否需要显示。
         */
        if (TextUtils.isEmpty(text)) {
            if (null != showingToast) {
                showingToast.cancel();
            }
            return;
        }

        /*
         * 根据文本长短决定显示的时间长度。
         */
        final int duration;
        if (text.length() <= 15) {
            duration = Toast.LENGTH_SHORT;
        } else {
            duration = Toast.LENGTH_LONG;
        }

        /*
         * 如果存在实例则更改文本继续显示，否则创建新的对象。
         */
        if (null != showingToast) {
            showingToast.setText(text);
            showingToast.setDuration(duration);
        } else {
            showingToast = Toast.makeText(VocApplication.Companion.getInstance(), text, duration);
        }
        if (showingToast != null) {
            ViewGroup group = (ViewGroup) showingToast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
        }
        showingToast.setGravity(Gravity.CENTER, 0, 0);
        showingToast.show();
    }

    /**
     * 显示提示。
     *
     * @param text 提示的内容。如果为 0 则隐藏当前显示。
     * @since 1.0
     */
    public static void showToast(int text) {
        if (0 == text) {
            showToast(null);
        } else {
            showToast(VocApplication.Companion.getInstance().getText(text));
        }
    }
}
