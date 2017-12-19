package com.ryo.happymoment.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by Administrator on 2017/12/19.
 */
/**
 * 用来保存用户的相关配置信息
 * */
public class BaseSettingsPreference {
    Context context;
    SharedPreferences preference;

    public BaseSettingsPreference(Context context) {
        this.context = context;
    }

    private void init(Context context) {
        preference = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }
}
