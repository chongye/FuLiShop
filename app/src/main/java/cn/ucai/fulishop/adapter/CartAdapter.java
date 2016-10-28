package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CartBean;
import bean.GoodsDetailsBean;
import bean.MessageBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CartBean> mCartList;
    @BindView(R.id.goods_thumb)
    ImageView goodsThumb;

    public CartAdapter(ArrayList<CartBean> mCartList, Context context) {
        this.mCartList = mCartList;
        this.context = context;
    }

    public void initCartList(ArrayList<CartBean> list) {
        this.mCartList.clear();
        this.mCartList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new CartViewHolder(View.inflate(context, R.layout.item_cart, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        final CartBean cartBean = mCartList.get(position);
        GoodsDetailsBean goods = cartBean.getGoods();
        if (goods != null) {
            ImageLoader.downloadImg(context, cartViewHolder.goodsThumb, goods.getGoodsThumb());
            cartViewHolder.cartGoodsName.setText(goods.getGoodsName());
            cartViewHolder.goodsprice.setText(goods.getCurrencyPrice());
            cartViewHolder.checkbox.setChecked(cartBean.isChecked());
        }
        cartViewHolder.addCart.setTag(position);
        cartViewHolder.cartCount.setText("(" + cartBean.getCount() + ")");
        cartViewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartBean.setChecked(isChecked);
                context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                NetDao.updateCart(context, cartBean.getId(), cartBean.getCount(), String.valueOf(cartBean.isChecked()), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartList == null ? 0 : mCartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
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

        @OnClick(R.id.add_cart)
        public void addCart() {
            int position = (int) addCart.getTag();
            final CartBean cartBean = mCartList.get(position);
            NetDao.updateCart(context, cartBean.getId(), cartBean.getCount() + 1, String.valueOf(cartBean.isChecked()), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        cartBean.setCount(cartBean.getCount() + 1);
                        context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                        cartCount.setText("(" + cartBean.getCount() + ")");
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @OnClick(R.id.del_cart)
        public void delCart() {
            final int position = (int) addCart.getTag();
            final CartBean cartBean = mCartList.get(position);
            if (cartBean.getCount() > 1) {
                NetDao.updateCart(context, cartBean.getId(), cartBean.getCount() - 1, String.valueOf(cartBean.isChecked()), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            cartBean.setCount(cartBean.getCount() - 1);
                            context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                            cartCount.setText("(" + cartBean.getCount() + ")");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.deleteCart(context, cartBean.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            mCartList.remove(position);
                            context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
        @OnClick(R.id.goods_thumb)
        public void goodsThumb() {
            int position = (int) addCart.getTag();
            int goodId = mCartList.get(position).getGoods().getGoodsId();
            MFGT.startDetailsActivity(context,goodId);
        }

        /*@OnClick({R.id.add_cart, R.id.del_cart})
        public void onClick(View view) {
            int position = (int) addCart.getTag();
            final CartBean cartBean = mCartList.get(position);
            switch (view.getId()) {
                case R.id.add_cart:
                    NetDao.updateCart(context, cartBean.getId(), cartBean.getCount(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                cartBean.setCount(cartBean.getCount()+1);
                                cartCount.setText("("+cartBean.getCount()+")");
                                context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));

                                checkbox.setChecked(cartBean.isChecked());
                            }
                        }
                        @Override
                        public void onError(String error) {

                        }
                    });
                    break;
                case R.id.del_cart:
                    if(cartBean.getCount()>1){
                    NetDao.updateCart(context, cartBean.getId(), cartBean.getCount(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if(result!=null&&result.isSuccess()){
                                cartBean.setCount(cartBean.getCount()-1);
                                context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                                cartCount.setText("("+cartBean.getCount()+")");

                                checkbox.setChecked(cartBean.isChecked());
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                    }else {
                        NetDao.deleteCart(context, cartBean.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if(result!=null&&result.isSuccess()){
                                    context.sendBroadcast(new Intent(I.SEND_BROADCAST_UPDATE_CART));
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                    }
                    break;
            }
        }*/
    }
}
