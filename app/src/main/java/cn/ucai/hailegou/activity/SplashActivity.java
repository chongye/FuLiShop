package cn.ucai.hailegou.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bean.UserAvatar;
import cn.ucai.hailegou.Dao.UserAvatarDao;
import cn.ucai.hailegou.HiLeGouApplication;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.utils.L;
import cn.ucai.hailegou.utils.MFGT;
import cn.ucai.hailegou.utils.SharePrefrenceUtils;

public class SplashActivity extends AppCompatActivity {
    static final long sleepTime = 2000;
    SplashActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                L.e("SplashActivity","SplashActivity......");
                UserAvatar user = HiLeGouApplication.getUser();
                String userName = SharePrefrenceUtils.getInstance(mContext).getUser();
                if(user==null&&userName!=null){
                    UserAvatarDao userAvatar = new UserAvatarDao(mContext);
                    user = userAvatar.getUserAvatar(userName);
                    if(user!=null){
                        HiLeGouApplication.setUser(user);
                    }
                }
                MFGT.gotoMainActivity(SplashActivity.this);
                finish();
            }
        },sleepTime);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
              *//*  // 开始时间
                long start = System.currentTimeMillis();
                // create 设置联网操作 后续进行
                long costTime = System.currentTimeMillis()-start;// 从启动到联网的操作耗时
                if(sleepTime-costTime>0){ // 如果不到两秒，这继续休眠
                    try {
                        Thread.sleep(sleepTime-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                *//**//*startActivity(new Intent(SplashActivity.this, MainActivity.class));
                *//**//**//**//*overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_in_from_right);*//**//**//**//*
                finish(); //  防止退出时重新到splash界面*//**//*
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        }).start();*/
    }
}
