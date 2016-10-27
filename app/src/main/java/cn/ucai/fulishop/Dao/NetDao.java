package cn.ucai.fulishop.Dao;

import android.content.Context;
import android.content.DialogInterface;

import java.io.File;

import bean.BoutiqueBean;
import bean.CartBean;
import bean.CategoryChildBean;
import bean.CategoryGroupBean;
import bean.CollectBean;
import bean.GoodsDetailsBean;
import bean.MessageBean;
import bean.NewGoodsBean;
import bean.Result;
import bean.UserAvatar;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.MD5;
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
    /*下载分类首界面
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findCategoryGroup*/
    public static void downLoadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>listener){
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }
    /*下载分类子界面
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findCategoryChildren?parent_id=344*/
    public static void downLoadCategoryChild(Context context, int parentId, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener){
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,parentId+"")
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
    /*下载分类二级界面
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findGoodsDetails?cat_id=345&page_id=1&page_size=10*/
    public static void downLoadCategoryGoods(Context context,int catId,int pageId,OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener){
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    /*注册用户
    http://101.251.196.90:8000/FuLiCenterServerV2.0/register?m_user_name=123&m_user_nick=123&m_user_password=122
    * */
    public static void register(Context context,String username,String nick,String password,OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }
    /*用户登录
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/login?m_user_name=yechong&m_user_password=1994*/
    public static void login(Context context,String username,String password,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.PASSWORD,MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }
    /*http://101.251.196.90:8000/FuLiCenterServerV2.0/updateNick?m_user_name=yechong&m_user_nick=wys*/
    public static void updateNick(Context context,String userName,String nick,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utlis = new OkHttpUtils<>(context);
        utlis.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .targetClass(String.class)
                .execute(listener);
    }
    /*http://101.251.196.90:8000/FuLiCenterServerV2.0/updateAvatar?name_or_hxid=yechong&avatarType=user_avatar*/
    public static void updateAvatar(Context context, UserAvatar user, File file ,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,user.getMuserName())
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void syncUserInfo(Context context,String userName,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utlis = new OkHttpUtils<>(context);
        utlis.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }
    /*查找收藏宝贝的数量
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findCollectCount?userName=yechong*/
    public static void getCollectCount(Context context, String user, OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,user)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    /*下载收藏商品的数据
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findCollects?userName=yulin&page_id=1&page_size=10*/
    public static void downLoadCollects(Context context, String userName, int pageId, OkHttpUtils.OnCompleteListener<CollectBean[]> listener){
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,userName)
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(listener);
    }
    /*删除收藏的商品
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/deleteCollect?goods_id=20&userName=userName*/
    public static void deletCollects(Context context,int goodId,String userName,OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID,String.valueOf(goodId))
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    /*是否添加收藏
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/isCollect?goods_id=7672&userName=yechong*/
    public static void isCollected(Context context,int goodId,String userName,OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID,String.valueOf(goodId))
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /*添加商品收藏
    http://101.251.196.90:8000/FuLiCenterServerV2.0/addCollect?goods_id=7672&userName=yechong*/
    public static void addCollects(Context context,int goodId,String userName,OkHttpUtils.OnCompleteListener<MessageBean>listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Collect.GOODS_ID,String.valueOf(goodId))
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    /*下载购物车商品
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/findCarts?userName=yechong*/
    public static void downLoadCart(Context context, String userName, OkHttpUtils.OnCompleteListener<CartBean[]> listener){
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(listener);
    }
    /*更新购物车
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/updateCart?id=1&count=1&isChecked=1*/
    public static void updateCart(Context context,int id,int count ,String isChecked,OkHttpUtils.OnCompleteListener<MessageBean> listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(id))
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,isChecked)
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    /*删除购物车
    * http://101.251.196.90:8000/FuLiCenterServerV2.0/deleteCart?id=1*/
    public static void deleteCart(Context context,int id, OkHttpUtils.OnCompleteListener<MessageBean>listener){
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,String.valueOf(id))
                .targetClass(MessageBean.class)
                .execute(listener);

    }

}
