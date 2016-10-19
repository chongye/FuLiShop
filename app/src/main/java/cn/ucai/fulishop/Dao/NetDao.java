package cn.ucai.fulishop.Dao;

import android.content.Context;

import bean.BoutiqueBean;
import bean.GoodsDetailsBean;
import bean.NewGoodsBean;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {

    // 下载新品首页数据
    // http://101.251.196.90:8000/FuLiCenterServerV2.0/findNewAndBoutiqueGoods?cat_id=0&page_id=1&page_size=4
    public static void downLoadNewGoods(Context context, int pageId,OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,I.CAT_ID+"")
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    // 下载新品详情数据
    // http://101.251.196.90:8000/FuLiCenterServerV2.0/findGoodDetails?goods_id=7672
    /*@param goods_id*/
    public static void downLoadGoodsDetails(Context context, int goodId, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener){
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID,goodId+"")
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    /*下载精品首页
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findBoutiques*/
    public static void downLoadBoutiques(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener){
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }
    /*下载精品分类界面
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findNewAndBoutiqueGoods?cat_id=262&page_id=1&page_size=10*/
    public static void downLoadBoutiqueCategroy(Context context, int catId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
}
