package cn.ucai.fulishop.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.NewGoodsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.GoodsAdapter;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends BaseFragment{
    View view;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.Recycler)
    RecyclerView Recycler;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    int mPageId = 1;

    GoodsAdapter mGoodsAdapter;
    ArrayList<NewGoodsBean> mNewGoods;
    Context mContext;
    GridLayoutManager gridLayoutManager;




    public NewGoodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }
    @Override
    protected void initData() {
        initData(I.ACTION_DOWNLOAD,mPageId);
    }

    @Override
    protected  void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                tvRefresh.setVisibility(View.VISIBLE);
                //  是否可用
                srl.setEnabled(true);
                // 是否刷新
                srl.setRefreshing(true);
                initData(I.ACTION_PULL_DOWN,mPageId);
            }
        });
        Recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 每一屏的最后的下标
                lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                // 如果每一屏的最后一个下标等于，每一屏的Adapter的最后一个下标，就执行
                if(lastPosition>=mGoodsAdapter.getItemCount()-1&&newState == RecyclerView.SCROLL_STATE_IDLE
                        && mGoodsAdapter.isMore()){
                    mPageId++;
                    initData(I.ACTION_PULL_UP,mPageId);
                }
               /* if(newState!=RecyclerView.SCROLL_STATE_DRAGGING){
                    mGoodsAdapter.notifyDataSetChanged();
                }*/
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
                // 当一屏的第一个下标等于0时，srl控件才可以用
                srl.setEnabled(firstPosition==0);
            }
        });

    }

    private void initData(final int action, int pageId) {
        NetDao.downLoadNewGoods(mContext, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if(result==null){
                    return;
                }
                mGoodsAdapter.setMore(result!=null&&result.length>0);
                List<NewGoodsBean> list = Arrays.asList(result);
                ArrayList<NewGoodsBean> newGoodsList = new ArrayList<>(list);
                if(!mGoodsAdapter.isMore()){
                    if(action==I.ACTION_PULL_UP){
                        mGoodsAdapter.setFooter(getResources().getString(R.string.no_more));
                    }
                    return;
                }
                switch (action){
                    case I.ACTION_DOWNLOAD:
                        mGoodsAdapter.initNewGoods(newGoodsList);
                        break;
                    case I.ACTION_PULL_UP:
                        mGoodsAdapter.addNewGoods(newGoodsList);
                        break;
                    case I.ACTION_PULL_DOWN:
                        mGoodsAdapter.initNewGoods(newGoodsList);
                        tvRefresh.setVisibility(View.GONE);
                        srl.setRefreshing(false);
                        break;
                }
                mGoodsAdapter.setFooter(getResources().getString(R.string.load_more));
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                srl.setEnabled(false);
            }
        });
    }

    @Override
    protected  void initView() {
        mContext = getActivity();
        mNewGoods = new ArrayList<>();
        mGoodsAdapter = new GoodsAdapter(mContext,mNewGoods);
        gridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        // ???
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 是否修复大小
        Recycler.setHasFixedSize(true);
        Recycler.setLayoutManager(gridLayoutManager);
        Recycler.setAdapter(mGoodsAdapter);

        // 设置控件间的距离
        Recycler.addItemDecoration(new SpaceItemDecoration(12));

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
    }

}
