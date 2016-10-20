package cn.ucai.fulishop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bean.CategoryChildBean;
import bean.CategoryGroupBean;
import butterknife.BindView;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        /*this.mContext = mContext;
        this.mGroupList = mGroupList;
        this.mChildList = mChildList;*/
        this.mContext = mContext;
        this.mGroupList = new ArrayList<CategoryGroupBean>();
        this.mGroupList.addAll(mGroupList);
        this.mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        this.mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = View.inflate(mContext, R.layout.item_category_group, null);
            holder.mGroupName = (TextView) convertView.findViewById(R.id.tv_category_group);
            holder.mIvGroup = (ImageView) convertView.findViewById(R.id.iv_category_group);
            holder.mIvExpand = (ImageView) convertView.findViewById(R.id.iv_expand);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
            holder.mGroupName.setText(mGroupList.get(groupPosition).getName());
            ImageLoader.downloadImg(mContext, holder.mIvGroup, mGroupList.get(groupPosition).getImageUrl());
            if (isExpanded) {
                holder.mIvExpand.setImageResource(R.mipmap.expand_off);
            } else {
                holder.mIvExpand.setImageResource(R.mipmap.expand_on);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(mContext, R.layout.item_category_child, null);
            holder.mIvChild = (ImageView) convertView.findViewById(R.id.iv_category_child);
            holder.mChildName = (TextView) convertView.findViewById(R.id.tv_category_child);
            holder.layoutCategoryChild = (RelativeLayout) convertView.findViewById(R.id.layout_Category_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
            holder.mChildName.setText(mChildList.get(groupPosition).get(childPosition).getName());
            ImageLoader.downloadImg(mContext, holder.mIvChild, mChildList.get(groupPosition).get(childPosition).getImageUrl());
            final CategoryChildBean categoryChild = mChildList.get(groupPosition).get(childPosition);
            holder.layoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.startCategoryChildActivity(mContext,categoryChild.getId());
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> mGruopList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mGroupList.clear();
        this.mGroupList.addAll(mGruopList);
        this.mChildList.clear();
        this.mChildList.addAll(mChildList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        ImageView mIvGroup, mIvExpand;
        TextView mGroupName;
    }

    class ChildViewHolder {
        ImageView mIvChild;
        TextView mChildName;
        RelativeLayout layoutCategoryChild;
    }
}
