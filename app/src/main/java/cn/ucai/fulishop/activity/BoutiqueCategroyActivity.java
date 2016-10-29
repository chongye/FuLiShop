package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import bean.BoutiqueBean;
import bean.NewGoodsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.GoodsAdapter;
import cn.ucai.fulishop.utils.ConvertUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.views.DisplayUtils;
import cn.ucai.fulishop.views.SpaceItemDecoration;

public class BoutiqueCategroyActivity extends BaseActivity {

    @BindView(R.id.tv_boutique_Categroy_name)
    TextView tvBoutiqueCategroyName;
    @BindView(R.id.tv_boutique_Refresh)
    TextView tvBoutiqueRefresh;
    @BindView(R.id.recycler_boutique_categroy)
    RecyclerView recyclerBoutiqueCategroy;
    @BindView(R.id.srl_boutique_categroy)
    SwipeRefreshLayout srlBoutiqueCategroy;
    int goodId;
    String goodName;
    int mPageId = 1;

    Context mContext;
    ArrayList<NewGoodsBean> mGoodsList;
    GoodsAdapter mAdapter;
    GridLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_boutique_categroy);
        mContext = this;
        ButterKnife.bind(this);
        Serializable extra = getIntent().getSerializableExtra(I.Boutique.KEY_GOODS);
        BoutiqueBean goods = (BoutiqueBean) extra;
        goodName = goods.getName();
        goodId = goods.getId();
        if(goodName ==null&&goodId==0){
            finish();
        }
        tvBoutiqueCategroyName.setText(goodName);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initData() {
        initData(I.ACTION_DOWNLOAD,mPageId);
        DisplayUtils.initBack((Activity) mContext);
    }
    @Override
    protected void setListener() {
        srlBoutiqueCategroy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                srlBoutiqueCategroy.setRefreshing(true);
                srlBoutiqueCategroy.setEnabled(true);
                tvBoutiqueRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN,mPageId);
            }
        });
        recyclerBoutiqueCategroy.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = mManager.findLastVisibleItemPosition();
                if(lastPosition >= mAdapter.getItemCount()-1&&newState == RecyclerView.SCROLL_STATE_IDLE
                        &&mAdapter.isMore()){
                    mPageId++;
                    initData(I.ACTION_PULL_UP,mPageId);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                srlBoutiqueCategroy.setEnabled(mManager.findFirstVisibleItemPosition()==0);
            }
        });
    }

    private void initData(final int action,int pageId) {
        NetDao.downLoadBoutiqueCategroy(mContext, goodId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if(result==null){
                    return;
                }
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                mAdapter.setMore(result!=null&&result.length>0);
                if(!mAdapter.isMore()){
                    if(action == I.ACTION_PULL_UP){
                        mAdapter.setFooter(getResources().getString(R.string.no_more));
                    }
                    return;
                }
                switch (action){
                    case I.ACTION_DOWNLOAD:
                        mAdapter.initNewGoods(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        mAdapter.initNewGoods(list);
                        srlBoutiqueCategroy.setRefreshing(false);
                        tvBoutiqueRefresh.setVisibility(View.GONE);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addNewGoods(list);
                        break;
                }
                mAdapter.setFooter(getResources().getString(R.string.load_more));
            }

            @Override
            public void onError(String error) {
                srlBoutiqueCategroy.setRefreshing(false);
                tvBoutiqueRefresh.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initView() {
        mGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext,mGoodsList);
        mManager = new GridLayoutManager(this,I.COLUM_NUM);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerBoutiqueCategroy.setHasFixedSize(true);
        /*mAdapter.setHasStableIds(true);加上有bug*/

        recyclerBoutiqueCategroy.setAdapter(mAdapter);
        recyclerBoutiqueCategroy.setLayoutManager(mManager);
        recyclerBoutiqueCategroy.addItemDecoration(new SpaceItemDecoration(10));

        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });


        srlBoutiqueCategroy.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green)
        );
    }
    public void onBackPressed(){
        finish();
    }
}
