package cn.ucai.fulishop.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CartBean;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.FuLiShopApplication;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.CartAdapter;
import cn.ucai.fulishop.utils.ConvertUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartsFragment extends BaseFragment {

    @BindView(R.id.Recycler)
    RecyclerView Recycler;
    @BindView(R.id.bt_buy)
    Button btBuy;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.dec_price)
    TextView decPrice;

    Context mContext;
    ArrayList<CartBean> mList;
    LinearLayoutManager manager;
    CartAdapter mAdapter;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;
    @BindView(R.id.layout_buy)
    RelativeLayout layoutBuy;

    UpdateReceiver mReceiver;

    public CartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carts, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mList, mContext);
        manager = new LinearLayoutManager(mContext);
        Recycler.setAdapter(mAdapter);
        Recycler.setLayoutManager(manager);

        Recycler.addItemDecoration(new SpaceItemDecoration(10));
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green)
        );
        setCartStatus(false);
        /*calculatePrice();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        downLoadCart();
    }

    @Override
    protected void initData() {
        downLoadCart();
    }

    public void setCartStatus(boolean hasCart) {
        tvNothing.setVisibility(hasCart?View.GONE:View.VISIBLE);
        layoutBuy.setVisibility(hasCart?View.VISIBLE:View.GONE);
        // 如果购物车为空，这隐藏
        Recycler.setVisibility(hasCart?View.VISIBLE:View.GONE);
        calculatePrice();
    }

    private void downLoadCart() {
        UserAvatar user = FuLiShopApplication.getUser();
        if (user != null) {
            NetDao.downLoadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mAdapter.initCartList(list);
                        setCartStatus(true);
                    } else {
                        setCartStatus(false);
                    }
                }
                @Override
                public void onError(String error) {
                    setCartStatus(false);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downLoadCart();
            }
        });
        mReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter(I.SEND_BROADCAST_UPDATE_CART);
        mContext.registerReceiver(mReceiver,filter);
    }
    public void calculatePrice(){
        int sumPrice = 0;
        int rankPrice = 0;
        if(mList != null){
            for(CartBean cart : mList){
                if(cart.isChecked()){
                    sumPrice += getPrice(cart.getGoods().getCurrencyPrice())*cart.getCount();
                    rankPrice += getPrice(cart.getGoods().getRankPrice())*cart.getCount();
                }
            }
            totalPrice.setText("总价: ￥"+sumPrice);
            decPrice.setText("节省: ￥"+(sumPrice-rankPrice));
        }else{
            setCartStatus(false);
            totalPrice.setText("总价: ￥0");
            decPrice.setText("节省: ￥0");
        }
    }
    private int getPrice(String price){
        return Integer.parseInt(price.substring(price.indexOf("￥")+1));
    }
    class UpdateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            calculatePrice();
            setCartStatus(mList!=null&&mList.size()>0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            mContext.unregisterReceiver(mReceiver);
        }
    }
}
