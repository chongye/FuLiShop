package cn.ucai.fulishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import bean.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.fragment.PersonalFragment;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.MFGT;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ResultUtils;
import cn.ucai.fulishop.views.DisplayUtils;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userName = getIntent().getStringExtra(I.User.USER_NAME);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etUserName.setText(userName);
    }

    @Override
    protected void initData() {
        DisplayUtils.initBack(this);

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.bt_login, R.id.bt_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                setLogin();
                break;
            case R.id.bt_register:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    private void setLogin() {
        NetDao.login(this, userName, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result == null){
                    return;
                }else if(result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                    CommonUtils.showShortToast("账户不存在");
                    return;
                }else if(result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                    CommonUtils.showShortToast("账户密码错误");
                    return;
                }
                CommonUtils.showShortToast("登录成功");
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    public void onBackPressed(){
        finish();
    }
}
