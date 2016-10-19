package cn.ucai.fulishop.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bean.BoutiqueBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.BoutiqueAdapter;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.ConvertUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends BaseFragment {
    BoutiqueAdapter boutiqueAdapter;
    ArrayList<BoutiqueBean> mBoutiqueList;
    Context mContext;
    LinearLayoutManager manager;

    @BindView(R.id.tv_Refresh)
    TextView tvRefresh;
    @BindView(R.id.boutique_Recycler)
    RecyclerView boutiqueRecycler;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    int pageId;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }
   @Override
    protected void initData() {
        initData(I.ACTION_DOWNLOAD);
    }
    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 0;
                srl.setRefreshing(true);
                srl.setEnabled(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN);
            }
        });
        boutiqueRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = manager.findLastVisibleItemPosition();
                if(lastPosition==boutiqueAdapter.getItemCount()-1&&newState == RecyclerView.SCROLL_STATE_IDLE
                        &&boutiqueAdapter.isMore()){
                    pageId += I.PAGE_ID_DEFAULT;
                    initData(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                srl.setEnabled(manager.findFirstVisibleItemPosition()==0);
            }
        });
    }

    private void initData(final int action) {
        NetDao.downLoadBoutiques(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if(result==null){
                    return;
                }
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                boutiqueAdapter.setMore(true);
                boutiqueAdapter.setFooter(getResources().getString(R.string.load_more));
                ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
               if(action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN){
                   boutiqueAdapter.initBoutique(list);
               }else{
                   boutiqueAdapter.addBoutique(list);
               }
                if(list.size()<I.PAGE_ID_DEFAULT){
                    boutiqueAdapter.setMore(false);
                    boutiqueAdapter.setFooter(getResources().getString(R.string.no_more));
                }else{
                    boutiqueAdapter.setMore(false);
                    boutiqueAdapter.setFooter(getResources().getString(R.string.load_more));
                }
            }

            @Override
            public void onError(String error) {
                srl.setEnabled(false);
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                CommonUtils.showShortToast(error);
            }
        });

    }

    @Override
    protected void initView() {
        mContext = getActivity();
        mBoutiqueList = new ArrayList<>();
        boutiqueAdapter = new BoutiqueAdapter(mContext,mBoutiqueList);
        manager = new LinearLayoutManager(mContext);

        boutiqueRecycler.setAdapter(boutiqueAdapter);
        boutiqueRecycler.setLayoutManager(manager);

        boutiqueRecycler.setHasFixedSize(true);

        boutiqueRecycler.addItemDecoration(new SpaceItemDecoration(10));

        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red)
        );
    }

}
