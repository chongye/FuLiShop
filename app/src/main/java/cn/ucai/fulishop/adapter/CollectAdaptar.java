package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CollectBean;
import bean.MessageBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CollectAdaptar extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<CollectBean> mCollectList;
    String footer;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public void initCollects(ArrayList<CollectBean> list) {
        this.mCollectList.clear();
        this.mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    public void addCollects(ArrayList<CollectBean> list) {
        this.mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFooter(String str) {
        this.footer = str;
        notifyDataSetChanged();
    }

    public CollectAdaptar(ArrayList<CollectBean> mCollectList, Context mContext) {
        this.mCollectList = mCollectList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new CollectViewHolder(View.inflate(mContext, R.layout.item_collect, null));
                break;
            case I.TYPE_FOOTER:
                holder = new GoodsAdapter.FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            GoodsAdapter.FooterViewHolder footerViewHolder = (GoodsAdapter.FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(footer);
            return;
        }
        CollectViewHolder collectViewHolder = (CollectViewHolder) holder;
        CollectBean collects = mCollectList.get(position);
        collectViewHolder.tvGoosName.setText(collects.getGoodsName());
        ImageLoader.downloadImg(mContext, collectViewHolder.ivGoodsThumb, collects.getGoodsThumb());
        collectViewHolder.layoutGoods.setTag(collects);
    }

    @Override
    public int getItemCount() {
        return mCollectList == null ? 0 : mCollectList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoosName)
        TextView tvGoosName;
        @BindView(R.id.layout_Goods)
        RelativeLayout layoutGoods;


        @OnClick(R.id.layout_Goods)
        public void goodsDetails() {
            CollectBean collect = (CollectBean)layoutGoods.getTag();
            MFGT.startDetailsActivity(mContext, collect.getGoodsId());
        }
        @OnClick(R.id.delete_collect)
        public void deletCollect() {
            final CollectBean collect = (CollectBean)layoutGoods.getTag();
            NetDao.deletCollects(mContext, collect.getGoodsId(), collect.getUserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        mCollectList.remove(collect);
                        notifyDataSetChanged();
                    }else{
                        CommonUtils.showShortToast("删除失败");
                    }
                }
                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast("删除失败");
                }
            });
        }

        CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
