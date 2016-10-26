package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bean.AlbumsBean;
import bean.GoodsDetailsBean;
import bean.MessageBean;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.FuLiShopApplication;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.CollectAdaptar;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.views.FlowIndicator;
import cn.ucai.fulishop.views.SlideAutoLoopView;

public class GoodsDetailsActivity extends BaseActivity {
    int goodId;
    @BindView(R.id.iv_Gd_back)
    ImageView ivGdBack;
    @BindView(R.id.tv_Gb_EnglishName)
    TextView tvGbEnglishName;
    @BindView(R.id.tv_Gb_ChineseName)
    TextView tvGbChineseName;
    @BindView(R.id.tv_Gb_Price)
    TextView tvGbPrice;
    @BindView(R.id.Salv)
    SlideAutoLoopView Salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_Goods)
    WebView wvGoods;
    @BindView(R.id.layout_GoodsMessage)
    RelativeLayout layoutGoodsMessage;
    @BindView(R.id.iv_Gd_collect)
    ImageView ivGdCollect;

    Context mContext;
    UserAvatar user;
    boolean isCollected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        mContext = this;
        goodId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("main", "details:" + goodId);
        if (goodId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        NetDao.downLoadGoodsDetails(this, goodId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result == null) {
                    finish();
                }
                showGoods(result);
            }

            @Override
            public void onError(String error) {
                finish();
                CommonUtils.showShortToast(error);
            }
        });
        // 添加收藏
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    private void showGoods(GoodsDetailsBean goodsDetails) {
        tvGbEnglishName.setText(goodsDetails.getGoodsEnglishName());
        tvGbChineseName.setText(goodsDetails.getGoodsName());
        tvGbPrice.setText(goodsDetails.getCurrencyPrice());
        // 实现轮播
        Salv.startPlayLoop(indicator, getAlbumImgUrl(goodsDetails), getAlbumImgCount(goodsDetails));
        wvGoods.loadDataWithBaseURL(null, goodsDetails.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean goodsDetails) {
        if (goodsDetails.getProperties() != null && goodsDetails.getProperties().length > 0) {
            return goodsDetails.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean goodsDetails) {
        String[] urls = new String[]{};
        if (goodsDetails.getProperties() != null && goodsDetails.getProperties().length > 0) {
            AlbumsBean[] albums = goodsDetails.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }

    @OnClick(R.id.iv_Gd_back)
    public void onBackClick() {
        MFGT.finish(this);
    }

    // 重写系统的back按钮
    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    //  判断是否添加
    private void isCollected(){
        user = FuLiShopApplication.getUser();
        if(user == null){
            isCollected = false;
        }else {
            NetDao.isCollected(mContext, goodId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null && result.isSuccess()){
                        isCollected = true;
                        setGoodsCollectStatu();
                    }
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    setGoodsCollectStatu();
                }
            });
        }
        setGoodsCollectStatu();
    }
    private void setGoodsCollectStatu(){
        if(isCollected){
            ivGdCollect.setImageResource(R.mipmap.bg_collect_out);
        }else {
            ivGdCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    // 商品的收藏，收藏过得为红色，为收藏的为白，为登录也为白色
    @OnClick(R.id.iv_Gd_collect)
    public void CollectGoods() {
        if(user == null){
            MFGT.gotoLoginActivity((Activity) mContext);
        }else{
            if(isCollected){
                NetDao.deletCollects(mContext, goodId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result != null&&result.isSuccess()){
                            isCollected = !isCollected;
                            setGoodsCollectStatu();
                            CommonUtils.showShortToast("取消收藏");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else{
                NetDao.addCollects(mContext, goodId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        isCollected = true;
                        setGoodsCollectStatu();
                        CommonUtils.showShortToast("收藏成功");
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }

    }
}
