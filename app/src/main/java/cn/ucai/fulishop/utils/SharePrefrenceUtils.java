package cn.ucai.fulishop.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */

public class SharePrefrenceUtils {
    private static final String SHARE_NAME = "saveUserInfo";
    private static final String SHARE_KEY = "shareKey";
    private static SharePrefrenceUtils instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    // 创建对象时将它实例化
    public SharePrefrenceUtils(Context context){
        mSharedPreferences = context.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharePrefrenceUtils getInstance(Context context){
        if(instance == null){
            instance = new SharePrefrenceUtils(context);
        }
        return instance;
    }

    public void saveUser(String userName){
        mEditor.putString(SHARE_KEY,userName);
        mEditor.commit();
    }

    public String getUser(){
        return mSharedPreferences.getString(SHARE_KEY,null);
    }
    public void removeUser(){
        mEditor.remove(SHARE_KEY);
        mEditor.commit();
    }
}
