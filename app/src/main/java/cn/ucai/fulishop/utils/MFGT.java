package cn.ucai.fulishop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import bean.BoutiqueBean;
import bean.CategoryChildBean;
import cn.ucai.fulishop.MainActivity;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.BoutiqueCategroyActivity;
import cn.ucai.fulishop.activity.CategoryChildActivity;
import cn.ucai.fulishop.activity.GoodsDetailsActivity;
import cn.ucai.fulishop.activity.LoginActivity;
import cn.ucai.fulishop.activity.RegisterActivity;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Context context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }
    public static void startDetailsActivity(Context context,int goodsId){
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
        /*context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }
    public static void startBoutiqueCategroyActivity(Context context, BoutiqueBean goods){
        Intent intent = new Intent(context, BoutiqueCategroyActivity.class);
        intent.putExtra(I.Boutique.KEY_GOODS,goods);
        startActivity(context,intent);
    }
    public static void startCategoryChildActivity(Context context, int catId, String groupName, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    /*跳转到注册界面*/
    public static void gotoRegisterActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        startActivity(context,intent);
    }
    /*跳转到登录界面*/
    public static void gotoLoginActivity(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REQUEST);
    }
    /*跳转到登录界面*/
    public static void gotoLoginActivity(Context context,String name){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(I.User.USER_NAME,name);
        startActivity(context,intent);
        finish((Activity) context);
    }
    public static void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
