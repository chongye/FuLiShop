package cn.ucai.hailegou;

import android.app.Application;

import bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/17.
 */
public class HiLeGouApplication extends Application{
    public static HiLeGouApplication applicationContext;
    private static HiLeGouApplication instance;

    // 判断个人中心是否有用户，为空则跳到登录界面
    private static String userName;
    private static UserAvatar user;

    public static UserAvatar getUser() {
        return user;
    }

    public static void setUser(UserAvatar user) {
        HiLeGouApplication.user = user;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        HiLeGouApplication.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
    }
    public static HiLeGouApplication getInstance(){
        if(instance == null){
            instance = new HiLeGouApplication();
        }
        return instance;
    }
}
