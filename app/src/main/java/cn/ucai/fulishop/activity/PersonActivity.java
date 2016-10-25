package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import bean.Result;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.Dao.UserAvatarDao;
import cn.ucai.fulishop.FuLiShopApplication;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.ImageLoader;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.OnSetAvatarListener;
import cn.ucai.fulishop.utils.ResultUtils;
import cn.ucai.fulishop.utils.SharePrefrenceUtils;

public class PersonActivity extends BaseActivity {

    @BindView(R.id.rl_nick)
    RelativeLayout rlNick;
    @BindView(R.id.bt_exit)
    Button btExit;
    UserAvatar user;
    @BindView(R.id.iv_person_avatar)
    ImageView ivPersonAvatar;
    @BindView(R.id.person_userName)
    TextView personUserName;
    @BindView(R.id.person_nick)
    TextView personNick;

    Context mContext;
    OnSetAvatarListener onsetAvatar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mContext = this;
        user = FuLiShopApplication.getUser();
    }

    @Override
    protected void initData() {
        if (user == null) {
            return;
        }
        personUserName.setText(user.getMuserName());
        personNick.setText(user.getMuserNick());
        ImageLoader.setAvatar(ImageLoader.getUrl(user), mContext, ivPersonAvatar);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode != RESULT_OK) {
            return;
        }
        onsetAvatar.setAvatar(requestCode, data, ivPersonAvatar);*/
        if(requestCode==I.REQUEST_CODE_REQUEST&&resultCode==RESULT_OK){
            CommonUtils.showShortToast("昵称更新成功");
        }
        /*if(requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }*/
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,user.getMavatarPath()+"/"+user.getMuserName()
        +I.AVATAR_SUFFIX_JPG));
        L.e("File","file1"+file.exists());
        L.e("File","file="+file);
        NetDao.updateAvatar(mContext, user, file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                if(result == null){
                    CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                }else{
                    UserAvatar u = (UserAvatar) result.getRetData();
                    if(result.isRetMsg()){
                        FuLiShopApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getUrl(u),mContext,ivPersonAvatar);
                        CommonUtils.showShortToast(R.string.update_user_avatar_success);
                    }else {
                        CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e("onResume","onResume....");
        showInfo();
}

    private void showInfo() {
        UserAvatar u = FuLiShopApplication.getUser();
        if (u != null) {
            personUserName.setText(u.getMuserName());
            personNick.setText(u.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getUrl(u), mContext, ivPersonAvatar);
        }
    }

    @OnClick({R.id.rl_nick, R.id.bt_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_nick:
                MFGT.startUpdateNickActivity((Activity) mContext);
                break;
            case R.id.bt_exit:
                FuLiShopApplication.setUser(null);
                SharePrefrenceUtils.getInstance(mContext).removeUser();
                UserAvatarDao dao = new UserAvatarDao(mContext);
                dao.deleteUserAvatar(user);
                MFGT.gotoLoginActivity(mContext, user.getMuserName());
                break;
        }
    }

    @OnClick(R.id.iv_person_avatar)
    public void onClick() {
        onsetAvatar = new OnSetAvatarListener((Activity) mContext,R.id.activity_person,user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
    }
}
