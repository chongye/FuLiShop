package cn.ucai.hailegou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bean.BoutiqueBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.utils.I;
import cn.ucai.hailegou.utils.ImageLoader;
import cn.ucai.hailegou.utils.MFGT;

public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<BoutiqueBean> mBoutiqueList;

    String footer;
    boolean isMore;


    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mBoutiqueList) {
        this.mContext = mContext;
        this.mBoutiqueList = mBoutiqueList;
    }

    public void initBoutique(ArrayList<BoutiqueBean> list) {
        this.mBoutiqueList.clear();
        this.mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void addBoutique(ArrayList<BoutiqueBean> list) {
        this.mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_ITEM:
                holder = new BoutiqueViewHolder(View.inflate(mContext, R.layout.item_boutique, null));
                break;
            case I.TYPE_FOOTER:
                holder = new GoodsAdapter.FooterViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_footer, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsAdapter.FooterViewHolder) {
            ((GoodsAdapter.FooterViewHolder) holder).tvFooter.setText(getFooterString());
            return;
        }
        BoutiqueViewHolder bvh = (BoutiqueViewHolder) holder;
        final BoutiqueBean boutiques = mBoutiqueList.get(position);
        bvh.tvBoutiqueTitle.setText(boutiques.getTitle());
        bvh.tvBoutiqueName.setText(boutiques.getName());
        bvh.tvBoutiqueDescription.setText(boutiques.getDescription());
        ImageLoader.downloadImg(mContext, bvh.ivBoutique, boutiques.getImageurl());
        bvh.layoutBoutique.setTag(boutiques);
        /*bvh.layoutBoutique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.startBoutiqueCategroyActivity(mContext,boutiques.getName(),boutiques.getId());
            }
        });*/
    }

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList == null ? 0 : mBoutiqueList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_Boutique)
        ImageView ivBoutique;
        @BindView(R.id.tv_boutique_title)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tv_boutique_name)
        TextView tvBoutiqueName;
        @BindView(R.id.tv_boutique_description)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layout_Boutique)
        RelativeLayout layoutBoutique;
        @OnClick(R.id.layout_Boutique)
                public void onClick(){
            BoutiqueBean goods = (BoutiqueBean) layoutBoutique.getTag();
            MFGT.startBoutiqueCategroyActivity(mContext,goods);
        }

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
