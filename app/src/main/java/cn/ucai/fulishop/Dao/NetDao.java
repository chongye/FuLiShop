package cn.ucai.fulishop.Dao;

import android.content.Context;

import bean.NewGoodsBean;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {

    // 现在新品首页数据
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
}
