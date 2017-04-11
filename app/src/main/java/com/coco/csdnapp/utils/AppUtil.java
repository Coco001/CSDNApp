package com.coco.csdnapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.text.TextUtils;


public class AppUtil {

    public static String getRefreashTime(Context context, int newType) {
        String timeStr = PreferenceUtil.readString(context, "NEWS_" + newType);
        if (TextUtils.isEmpty(timeStr)) {
            return "我好笨，忘记了...";
        }
        return timeStr;
    }

    public static void setRefreashTime(Context context, int newType) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PreferenceUtil.write(context, "NEWS_" + newType, df.format(new Date()));
    }
}
