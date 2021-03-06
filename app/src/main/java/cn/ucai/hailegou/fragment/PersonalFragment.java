package cn.ucai.hailegou.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bean.MessageBean;
import bean.Result;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.hailegou.Dao.NetDao;
import cn.ucai.hailegou.Dao.UserAvatarDao;
import cn.ucai.hailegou.HiLeGouApplication;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.utils.ImageLoader;
import cn.ucai.hailegou.utils.MFGT;
import cn.ucai.hailegou.utils.OkHttpUtils;
import cn.ucai.hailegou.utils.ResultUtils;

public class PersonalFragment extends BaseFragment {
    Context mContext;
    @BindView(R.id.person_set)
    TextView person;
    @BindView(R.id.person_avatar)
    ImageView personAvatar;
    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;

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
        if (HiLeGouApplication.getUser() == null) {
            MFGT.gotoLoginActivity((Activity) mContext);
        } else {
            UserAvatar user = HiLeGouApplication.getUser();
            personName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getUrl(user), mContext, personAvatar);
        }
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onResume() {
        super.onResume();
        /*syncUserInfo(FuLiShopApplication.getUser().getMuserName());*/
        if (HiLeGouApplication.getUser() != null) {
            UserAvatar user = HiLeGouApplication.getUser();
            personName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getUrl(user), mContext, personAvatar);
            getCollectCount(user.getMuserName());
        }
    }

    private void syncUserInfo(final String muserName) {
        NetDao.syncUserInfo(mContext, muserName, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                if (result == null) {
                    return;
                } else {
                    UserAvatar u = (UserAvatar) result.getRetData();
                    UserAvatarDao dao = new UserAvatarDao(mContext);
                    dao.saveUserAvatar(u);
                    if (!HiLeGouApplication.getUser().equals(u)) {
                        HiLeGouApplication.setUser(u);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick(R.id.person_set)
    public void peronSetting() {
        MFGT.startPersonActivity(mContext);
    }

    private void getCollectCount(String userName) {
        NetDao.getCollectCount(mContext, userName, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    tvCollectCount.setText(result.getMsg());
                } else {
                    tvCollectCount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                tvCollectCount.setText(String.valueOf(0));
            }
        });
    }

    @OnClick(R.id.collect_goods)
    public void collectGoods() {
        MFGT.gotoCollects(mContext);
    }
}
