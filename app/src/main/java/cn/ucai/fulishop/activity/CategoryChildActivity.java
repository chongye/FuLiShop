package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CategoryChildBean;
import bean.NewGoodsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    String mGoodsName;
    ArrayList<CategoryChildBean> mChildList;

    Context mContext;
    ArrayList<NewGoodsBean> mGoodsList;
    GoodsAdapter mAdapter;
    GridLayoutManager mManager;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_price)
    ImageView ivPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_time)
    ImageView ivTime;

    int sort;
    boolean isPriceAsc;
    boolean isAddTimeAsc;
    @BindView(R.id.iv_Child_back)
    ImageView ivChildBack;
    @BindView(R.id.btnCatChildFilter)
    cn.ucai.fulishop.views.CatChildFilterButton btnCatChildFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        goodId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        mGoodsName = getIntent().getStringExtra(I.Goods.KEY_GOODS_NAME);
        mChildList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.Goods.KEY_GOODS);
        /*Log.i("main",mChildList.get(0).getName());*/
        if (goodId == 0) {
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
        btnCatChildFilter.setText(mGoodsName);

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green)
        );

    }


    private void initData() {
        initData(I.ACTION_DOWNLOAD, mPageId);
        btnCatChildFilter.setOnCatFilterClickListener(mGoodsName, mChildList);
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                srl.setRefreshing(true);
                srl.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN, mPageId);
                mAdapter.setSort(sort);
            }
        });
        Recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = mManager.findLastVisibleItemPosition();
                if (lastPosition >= mAdapter.getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.isMore()) {
                    mPageId++;
                    initData(I.ACTION_PULL_UP, mPageId);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                srl.setEnabled(mManager.findFirstVisibleItemPosition() == 0);
            }
        });
    }

    private void initData(final int action, int pageId) {
        NetDao.downLoadCategoryGoods(mContext, goodId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if (result == null) {
                    return;
                }
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                mAdapter.setMore(result != null && result.length > 0);
               /* if(result.length%2==1){
                    list.add(new NewGoodsBean());
                }*/
                if (!mAdapter.isMore()) {
                    if (action == I.ACTION_PULL_UP) {
                        mAdapter.setFooter(getResources().getString(R.string.no_more));
                    }
                    return;
                }
                switch (action) {
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
                mAdapter.setSort(sort);
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
            }
        });
    }

    //  实现排序后，保证刷新排序不变，需要在刷新中和initData中加mAdapter.setSort(sort)方法
    @OnClick({R.id.tv_price, R.id.tv_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_price:
                if (isPriceAsc) {
                    sort = I.SORT_BY_PRICE_ASC;
                    ivPrice.setImageResource(R.drawable.arrow_order_up);
                } else {
                    sort = I.SORT_BY_PRICE_DESC;
                    ivPrice.setImageResource(R.drawable.arrow_order_down);
                }
                isPriceAsc = !isPriceAsc;
                break;
            case R.id.tv_time:
                if (isAddTimeAsc) {
                    sort = I.SORT_BY_ADDTIME_ASC;
                    ivTime.setImageResource(R.drawable.arrow_order_up);
                } else {
                    sort = I.SORT_BY_ADDTIME_DESC;
                    ivTime.setImageResource(R.drawable.arrow_order_down);
                }
                isAddTimeAsc = !isAddTimeAsc;
                break;
        }
        mAdapter.setSort(sort);
    }
}
