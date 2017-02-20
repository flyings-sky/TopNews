package andfans.com.andfans_csdn.Utils;

import android.content.Context;

/**
 * Created by 兆鹏 on 2017/2/14.
 */
public class Util {
    /**
     *
     * @param context 上下文
     * @param dpValue dp数值
     * @return dp to  px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }
}
