package cn.ucai.fulishop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import bean.NewGoodsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.activity.GoodsDetailsActivity;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<NewGoodsBean> goods;
    String footer;

    boolean isMore;

    int sort;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> goods) {
        this.mContext = mContext;
        this.goods = goods;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void initNewGoods(ArrayList<NewGoodsBean> goods) {
        this.goods.clear();
        this.goods.addAll(goods);
        notifyDataSetChanged();
    }

    public void addNewGoods(ArrayList<NewGoodsBean> goods) {
        this.goods.addAll(goods);
        notifyDataSetChanged();
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }
    public void setSort(int sort){
        this.sort = sort;
        sortBy();
        notifyDataSetChanged();
    }

    private void sortBy() {
        Collections.sort(goods, new Comparator<NewGoodsBean>() {
            int result = 0;
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                switch (sort){
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                    // 左边减右边，如果是正数，左边大，交换位置，否者不交换，次时右边大，升序
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    // 右边减左边，正数，右边大，交换位置，此时左边大降序
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                }
                return result;
            }
            private int getPrice(String price){
                return Integer.valueOf(price.substring(price.indexOf("￥")+1));
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
                break;
            case I.TYPE_ITEM:
                holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
        /*if(getItemViewType(I.TYPE_FOOTER)==position){*/
            FooterViewHolder fvh = (FooterViewHolder) holder;
            fvh.tvFooter.setText(footer);
            return;
        }
        GoodsViewHolder gvh = (GoodsViewHolder) holder;
        final NewGoodsBean newGood = goods.get(position);
        gvh.tvGoosName.setText(newGood.getGoodsName());
        gvh.tvGoodsPrice.setText(newGood.getCurrencyPrice());
        ImageLoader.downloadImg(mContext, gvh.ivGoodsThumb, newGood.getGoodsThumb());
        gvh.layoutGoods.setTag(newGood.getGoodsId());
    }

    @Override
    public int getItemCount() {
        return goods == null ? 0 : goods.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoosName)
        TextView tvGoosName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_Goods)
        LinearLayout layoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_Goods)
        public void OnNewGoodsItemClick(){
            int goodId = (int) layoutGoods.getTag();
            MFGT.startDetailsActivity(mContext,goodId);
        }
    }
}
