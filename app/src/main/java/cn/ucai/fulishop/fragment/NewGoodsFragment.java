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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
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
        initView();
        initData(I.ACTION_DOWNLOAD,mPageId);
        setListener();
        return view;
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                tvRefresh.setVisibility(View.VISIBLE);
                srl.setEnabled(true);
                srl.setRefreshing(true);
                initData(I.ACTION_PULL_DOWN,mPageId);
            }
        });
        Recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                if(lastPosition>=mGoodsAdapter.getItemCount()-1&&newState == RecyclerView.SCROLL_STATE_DRAGGING
                        && mGoodsAdapter.isMore()){
                    mPageId++;
                    initData(I.ACTION_PULL_UP,mPageId);
                }
                if(newState!=RecyclerView.SCROLL_STATE_DRAGGING){
                    mGoodsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData(final int action, int mPageId) {
        NetDao.downLoadNewGoods(mContext, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
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
                        mGoodsAdapter.setFooter(I.NewGoods.HINT_DOWNLOAD_FAILURE);
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
                        /*srl.setEnabled(false);*/
                        srl.setRefreshing(false);
                        /*ImageLoader.release();*/
                        break;
                }
                mGoodsAdapter.setFooter(I.NewGoods.HINT_DOWNLOADING);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
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

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
    }

}
