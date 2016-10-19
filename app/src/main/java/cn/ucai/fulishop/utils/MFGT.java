package cn.ucai.fulishop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import bean.BoutiqueBean;
import cn.ucai.fulishop.MainActivity;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.BoutiqueCategroyActivity;
import cn.ucai.fulishop.activity.GoodsDetailsActivity;


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
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
