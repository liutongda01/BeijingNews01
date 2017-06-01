package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Liutongda on 2017/6/1.
 * 缓存工具类
 */
public class CacheUtils {
    /**
     * 保持参数
     * @param context
     * @param key
     * @param value
     * */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    /**
     * 得到保存的参数
     * @param context
     * @param key
     * */
    public static boolean getBoolean(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
}
