package cn.ucai.hailegou.utils;

import android.widget.Toast;

import cn.ucai.hailegou.HiLeGouApplication;

public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(HiLeGouApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(HiLeGouApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(HiLeGouApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(HiLeGouApplication.getInstance().getString(rId));
    }
}
