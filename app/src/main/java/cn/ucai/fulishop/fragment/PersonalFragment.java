package cn.ucai.fulishop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.FuLiShopApplication;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.SharePrefrenceUtils;

public class PersonalFragment extends BaseFragment {
    Context mContext;
    @BindView(R.id.person_)
    TextView person;
    @BindView(R.id.person_avatar)
    ImageView personAvatar;
    @BindView(R.id.person_name)
    TextView personName;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        mContext = getContext();
    }

    @Override
    protected void initData() {
        if (FuLiShopApplication.getUser() == null) {
            MFGT.gotoRegisterActivity(mContext);
        }
        UserAvatar user = FuLiShopApplication.getUser();
        personName.setText(user.getMuserNick());
        ImageLoader.setAvatar(ImageLoader.getUrl(user), mContext, personAvatar);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.person_)
    public void onClick() {
        SharePrefrenceUtils.getInstance(mContext).removeUser();
        FuLiShopApplication.setUser(null);
    }
}
