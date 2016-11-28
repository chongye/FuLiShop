package cn.ucai.hailegou.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import bean.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.hailegou.Dao.NetDao;
import cn.ucai.hailegou.R;
import cn.ucai.hailegou.utils.CommonUtils;
import cn.ucai.hailegou.utils.I;
import cn.ucai.hailegou.utils.MFGT;
import cn.ucai.hailegou.utils.OkHttpUtils;
import cn.ucai.hailegou.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_Nick)
    EditText etNick;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_pass)
    EditText etConfirmPass;

    String userName;
    String nick;
    String password;
    String confirPassword;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mContext = this;
    }

    @Override
    protected void initData() {
        DisplayUtils.initBackWithTitle(this,"账户注册");
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_password)
    public void onClick() {
        userName = etUserName.getText().toString().trim();
        nick = etNick.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirPassword  = etConfirmPass.getText().toString().trim();
        inPutConfir();
    }
    private void inPutConfir() {
        if(TextUtils.isEmpty(userName)){
            etUserName.requestFocus();
            etUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            return;
        }else if(!userName.matches("[A-Za-z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            return;
        }else if(TextUtils.isEmpty(nick)){
            etNick.requestFocus();
            etNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            return;
        }else if(TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            return;
        }else if(TextUtils.isEmpty(confirPassword)){
            etConfirmPass.requestFocus();
            etConfirmPass.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            return;
        }else if(!password.equals(confirPassword)){
            CommonUtils.showShortToast(R.string.two_input_password);
            return;
        }
        register();
    }

    private void register() {
        NetDao.register(this, userName, nick, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result==null){
                    return;
                }else if(result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS){
                    CommonUtils.showShortToast(R.string.register_fail_exists);
                    return;
                }
                MFGT.gotoLoginActivity(mContext,userName);
                CommonUtils.showLongToast(R.string.register_success);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
