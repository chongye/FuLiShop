package cn.ucai.fulishop;

import android.app.Application;
import android.content.Context;

import bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiShopApplication extends Application{
    public static FuLiShopApplication applicationContext;
    private static FuLiShopApplication instance;

    // 判断个人中心是否有用户，为空则跳到登录界面
    private static String userName;
    private static UserAvatar user;

    public static UserAvatar getUser() {
        return user;
    }

    public static void setUser(UserAvatar user) {
        FuLiShopApplication.user = user;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FuLiShopApplication.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
    }
    public static FuLiShopApplication getInstance(){
        if(instance == null){
            instance = new FuLiShopApplication();
        }
        return instance;
    }
}
