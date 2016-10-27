package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CartBean;
import bean.GoodsDetailsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.ImageLoader;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CartBean> mCartList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new CartViewHolder(View.inflate(context, R.layout.item_cart, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        CartBean cartBean = mCartList.get(position);
        GoodsDetailsBean goods = cartBean.getGoods();
        if(goods!=null){
            ImageLoader.downloadImg(context,cartViewHolder.goodsThumb,goods.getGoodsThumb());
            cartViewHolder.cartGoodsName.setText(goods.getGoodsName());
            cartViewHolder.goodsprice.setText(goods.getCurrencyPrice());
        }
        cartViewHolder.cartCount.setText("("+cartBean.getCount()+")");
    }

    @Override
    public int getItemCount() {
        return mCartList == null ? 0 : mCartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.goods_thumb)
        ImageView goodsThumb;
        @BindView(R.id.cart_goods_name)
        TextView cartGoodsName;
        @BindView(R.id.add_cart)
        ImageView addCart;
        @BindView(R.id.cart_count)
        TextView cartCount;
        @BindView(R.id.del_cart)
        ImageView delCart;
        @BindView(R.id.goods_price)
        TextView goodsprice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
