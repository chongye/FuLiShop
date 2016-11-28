package cn.ucai.hailegou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import bean.Result;
import bean.UserAvatar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.hailegou.Dao.NetDao;
import cn.ucai.hailegou.Dao.UserAvatarDao;
import cn.ucai.hailegou.HiLeGouApplication;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.utils.CommonUtils;
import cn.ucai.hailegou.utils.I;
import cn.ucai.hailegou.utils.L;
import cn.ucai.hailegou.utils.MFGT;
import cn.ucai.hailegou.utils.OkHttpUtils;
import cn.ucai.hailegou.utils.ResultUtils;
import cn.ucai.hailegou.utils.SharePrefrenceUtils;
import cn.ucai.hailegou.views.DisplayUtils;

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
        oldNick = HiLeGouApplication.getUser().getMuserNick();
        if(TextUtils.isEmpty(oldNick) || HiLeGouApplication.getUser()==null){
            finish();
        }
        etUpdateNick.setText(oldNick);
        user = HiLeGouApplication.getUser();
        mContext = this;
    }

    @Override
    protected void initData() {
        DisplayUtils.initBack(this);

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
                            HiLeGouApplication.setUser(u);
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
