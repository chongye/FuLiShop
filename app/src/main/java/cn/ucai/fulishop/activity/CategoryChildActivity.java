package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

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
import cn.ucai.fulishop.views.SpaceItemDecoration;

public class CategoryChildActivity extends AppCompatActivity {

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.Recycler)
    RecyclerView Recycler;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    int goodId;
    int mPageId = 1;

    Context mContext;
    ArrayList<NewGoodsBean> mGoodsList;
    GoodsAdapter mAdapter;
    GridLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        goodId = getIntent().getIntExtra(I.Goods.KEY_GOODS,0);
        L.i("goodId","id="+goodId);
        if(goodId==0){
            finish();
        }
        initView();
        initData();
        setListener();

    }
    private void initView() {
        mContext = this;
        mGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext, mGoodsList);
        mManager = new GridLayoutManager(this, I.COLUM_NUM);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        Recycler.setHasFixedSize(true);
        /*mAdapter.setHasStableIds(true);加上有bug*/

        Recycler.setAdapter(mAdapter);
        Recycler.setLayoutManager(mManager);
        Recycler.addItemDecoration(new SpaceItemDecoration(12));

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green)
        );

    }


    private void initData() {
        initData(I.ACTION_DOWNLOAD,mPageId);
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                srl.setRefreshing(true);
                srl.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN,mPageId);
            }
        });
        Recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                srl.setEnabled(mManager.findFirstVisibleItemPosition()==0);
            }
        });
    }
    private void initData(final int action,int pageId) {
        NetDao.downLoadCategoryGoods(mContext, goodId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if(result==null){
                    return;
                }
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                mAdapter.setMore(result!=null&&result.length>0);
               /* if(result.length%2==1){
                    list.add(new NewGoodsBean());
                }*/
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
                        srl.setRefreshing(false);
                        tvRefresh.setVisibility(View.GONE);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addNewGoods(list);
                        break;
                }
                mAdapter.setFooter(getResources().getString(R.string.load_more));
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
            }
        });
    }
}