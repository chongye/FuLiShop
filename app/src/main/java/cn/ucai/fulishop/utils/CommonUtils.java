package cn.ucai.fulishop.utils;

import android.widget.Toast;

import cn.ucai.fulishop.FuLiShopApplication;

public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(FuLiShopApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(FuLiShopApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(FuLiShopApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(FuLiShopApplication.getInstance().getString(rId));
    }
}
