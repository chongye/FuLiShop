package cn.ucai.hailegou.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CollectBean;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.hailegou.Dao.NetDao;
import cn.ucai.hailegou.HiLeGouApplication;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.adapter.CollectAdaptar;
import cn.ucai.hailegou.utils.ConvertUtils;
import cn.ucai.hailegou.utils.I;
import cn.ucai.hailegou.utils.L;
import cn.ucai.hailegou.utils.OkHttpUtils;
import cn.ucai.hailegou.views.DisplayUtils;
import cn.ucai.hailegou.views.SpaceItemDecoration;

public class CollectActivity extends BaseActivity {
    ArrayList<CollectBean> mCollectList;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.Recycler)
    RecyclerView Recycler;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    Context mContext;
    GridLayoutManager glm;
    CollectAdaptar mAdapter;

    UserAvatar user;

    int mPageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        mContext = this;
        user = HiLeGouApplication.getUser();
        if(user == null){
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBack((Activity) mContext);
        mCollectList = new ArrayList<>();
        mAdapter = new CollectAdaptar(mCollectList,mContext);
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        Recycler.setAdapter(mAdapter);
        Recycler.setLayoutManager(glm);
        Recycler.addItemDecoration(new SpaceItemDecoration(12));

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green)
        );

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });
    }

    @Override
    protected void initData() {
        dowmLoadCollect(I.ACTION_DOWNLOAD,mPageId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dowmLoadCollect(I.ACTION_DOWNLOAD,1);
    }

    private void dowmLoadCollect(final int action, int mPageId) {
        NetDao.downLoadCollects(mContext, user.getMuserName(), mPageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                L.e("result","result1+"+result);
                if(result==null){
                    L.e("result","result3+"+result);
                    return;
                }
                L.e("result","result2+"+result);
                mAdapter.setMore(true);
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setFooter("加载更多数据");
                ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                if(action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN){
                    mAdapter.initCollects(list);
                }else{
                    mAdapter.addCollects(list);
                }if(list.size()<I.PAGE_SIZE_DEFAULT){
                    mAdapter.setMore(false);
                    mAdapter.setFooter("没有更多数据");
                }

            }

            @Override
            public void onError(String error) {
                mAdapter.setMore(true);
                srl.setRefreshing(false);
                mAdapter.setMore(false);
            }
        });
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageId = 1;
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                dowmLoadCollect(I.ACTION_PULL_DOWN,mPageId);
            }
        });
        Recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition;
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = glm.findLastVisibleItemPosition();
                if(lastPosition==mAdapter.getItemCount()-1&&newState == RecyclerView.SCROLL_STATE_IDLE
                        &&mAdapter.isMore()){
                    mPageId ++;
                    dowmLoadCollect(I.ACTION_PULL_UP,mPageId);
                }
                srl.setEnabled(glm.findFirstVisibleItemPosition()==0);
            }
        });

    }
}
