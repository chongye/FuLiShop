package cn.ucai.hailegou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import cn.ucai.hailegou.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setListener();
    }
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();

    public void onBackPressed(){
        MFGT.finish(this);
    }

}
