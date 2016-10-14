package cn.ucai.fulishop.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulishop.MainActivity;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.MFGT;

public class SplashActivity extends AppCompatActivity {
    static final long sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始时间
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
                /*startActivity(new Intent(SplashActivity.this, MainActivity.class));
                *//*overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_in_from_right);*//*
                finish(); //  防止退出时重新到splash界面*/
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        }).start();
    }
}
