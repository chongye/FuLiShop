package cn.ucai.fulishop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

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
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ResultUtils;
import cn.ucai.fulishop.utils.SharePrefrenceUtils;

public class UpdateNickActivity extends BaseActivity {

    @BindView(R.id.et_updateNick)
    EditText etUpdateNick;
    @BindView(R.id.bt_updateNick)
    Button btUpdateNick;

    String oldNick;
    UserAvatar user;
    UpdateNickActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        oldNick = FuLiShopApplication.getUser().getMuserNick();
        if(TextUtils.isEmpty(oldNick) || FuLiShopApplication.getUser()==null){
            finish();
        }
        etUpdateNick.setText(oldNick);
        user = FuLiShopApplication.getUser();
        mContext = this;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_updateNick)
    public void onClick() {
        String newNick = etUpdateNick.getText().toString();
        if(newNick.equals(oldNick)){
            CommonUtils.showShortToast("昵称修改前后不能一致");
            return;
        }else if(TextUtils.isEmpty(newNick)){
            etUpdateNick.requestFocus();
            etUpdateNick.setError("不能为空");
            return;
        }else {
            updateNick(newNick);
        }
    }

    private void updateNick(String nick) {

        NetDao.updateNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s,UserAvatar.class);
                if(result==null){
                    return;
                }else{
                    if(result.isRetMsg()){
                        UserAvatar u = (UserAvatar) result.getRetData();
                        UserAvatarDao dao = new UserAvatarDao(mContext);
                        boolean isSuccess = dao.updateUserAvatar(u);
                        L.e("isSuccess","isSuccess:"+isSuccess);
                        if(isSuccess){
                            SharePrefrenceUtils.getInstance(mContext).saveUser(u.getMuserName());
                            FuLiShopApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showShortToast(R.string.user_database_error);
                        }
                    }else{
                        if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                            CommonUtils.showShortToast("昵称修改失败");
                        }else if(result.getRetCode()==I.MSG_USER_SAME_PASSWORD){
                            CommonUtils.showShortToast("昵称为修改");
                        }else if(result.getRetCode()==I.MSG_USER_UPDATE_PASSWORD_FAIL){
                            CommonUtils.showShortToast("昵称修改失败");
                        }
                    }
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
