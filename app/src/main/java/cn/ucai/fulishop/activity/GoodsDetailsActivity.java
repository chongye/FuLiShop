package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import bean.AlbumsBean;
import bean.GoodsDetailsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        goodId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodId == 0) {
            finish();
        }
        initView();
        initData();
        setListener();
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
                if(result==null){
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
    }

    private void showGoods(GoodsDetailsBean goodsDetails) {
        tvGbEnglishName.setText(goodsDetails.getGoodsEnglishName());
        tvGbChineseName.setText(goodsDetails.getGoodsName());
        tvGbPrice.setText(goodsDetails.getCurrencyPrice());
        // 实现轮播
        Salv.startPlayLoop(indicator,getAlbumImgUrl(goodsDetails),getAlbumImgCount(goodsDetails));
        wvGoods.loadDataWithBaseURL(null,goodsDetails.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private int getAlbumImgCount(GoodsDetailsBean goodsDetails) {
        if(goodsDetails.getProperties()!=null&&goodsDetails.getProperties().length>0){
            return goodsDetails.getProperties()[0].getAlbums().length;
        }
        return 0;
    }
    private String[] getAlbumImgUrl(GoodsDetailsBean goodsDetails) {
        String[] urls = new String[]{};
        if(goodsDetails.getProperties()!=null&&goodsDetails.getProperties().length>0){
            AlbumsBean[] albums = goodsDetails.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for(int i=0;i<albums.length;i++){
                urls[i] = albums[i].getImgUrl();
            }
        }
        return urls;
    }
    @OnClick(R.id.iv_Gd_back)
    public void onBackClick(){
        MFGT.finish(this);
    }
    // 重写系统的back按钮
    @Override
    public void onBackPressed(){
        MFGT.finish(this);
    }
}
