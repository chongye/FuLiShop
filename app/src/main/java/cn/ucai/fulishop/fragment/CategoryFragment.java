package cn.ucai.fulishop.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.CategoryChildBean;
import bean.CategoryGroupBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.adapter.CategoryAdapter;
import cn.ucai.fulishop.utils.ConvertUtils;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends BaseFragment {
    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> mGruopList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    Context mContext;

    @BindView(R.id.Elv)
    ExpandableListView Elv;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        mContext = getContext();
        mGruopList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext,mGruopList,mChildList);
        Elv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        NetDao.downLoadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                /*ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                mGruopList.addAll(groupList);*/
                mGruopList = ConvertUtils.array2List(result);
                for (int i = 0; i < mGruopList.size(); i++) {
                    mChildList.add(new ArrayList<CategoryChildBean>());
                    getChildList(mGruopList.get(i).getId(), i);
                    /*NetDao.downLoadCategoryChild(mContext, result[i].getId(), new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
                        @Override
                        public void onSuccess(CategoryChildBean[] result) {
                            ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                            mChildList.add(list);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });*/
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void getChildList(int id, final int indext) {
        NetDao.downLoadCategoryChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                mChildList.set(indext,list);
                L.e("Category","list="+mChildList.get(0).toString());
                mAdapter.initData(mGruopList,mChildList);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void setListener() {

    }

}
