package cn.ucai.hailegou.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class LoginActivity extends BaseActivity {
    final String TAG = LoginActivity.class.getSimpleName();
    private final String TB_USER = "tb_user";

    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    String userName;
    String password;
    Context mContext;
    @BindView(R.id.check_box)
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userName = getIntent().getStringExtra(I.User.USER_NAME);
        L.e(TAG,"LoginActivity，执行");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        etUserName.setText(userName);
        mContext = this;
    }

    @Override
    protected void initData() {
        DisplayUtils.initBack(this);

    }

    @Override
    protected void setListener() {
        //  是否显示密码
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    // 光标一直显示在文本后面，新对象不能提到外面 开始没选中editable长度为了零
                    Editable editable = etPassword.getText();
                    Selection.setSelection(editable,editable.length());
                }else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Editable editable = etPassword.getText();
                    Selection.setSelection(editable,editable.length());
                }
            }
        });
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
        NetDao.login(this, userName, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Result result1 = ResultUtils.getResultFromJson(result, UserAvatar.class);
                if (result == null) {
                    return;
                } else if (result1.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                    CommonUtils.showShortToast("账户不存在");
                    return;
                } else if (result1.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                    CommonUtils.showShortToast("账户密码错误");
                    return;
                }
                UserAvatar user = (UserAvatar) result1.getRetData();
                L.e("user", "user=" + user.toString());

                 /*//  result.getRetData()为json的String文件
                 Gson gson = new Gson();
                 String json = result.getRetData().toString();
                 UserAvatar user = gson.fromJson(json,UserAvatar.class);*/
                UserAvatarDao userDao = new UserAvatarDao(mContext);
                userDao.saveUserAvatar(user);
                boolean isSuccess = userDao.saveUserAvatar(user);
                if (isSuccess) {
                    CommonUtils.showShortToast(user.toString());
                    SharePrefrenceUtils.getInstance(mContext).saveUser(user.getMuserName());
                    HiLeGouApplication.setUser(user);
                    setResult(I.REQUEST_CODE_REQUEST);
                    MFGT.finish((Activity) mContext);
                } else {
                    CommonUtils.showShortToast(R.string.user_database_error);
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                L.e("error=" + error);
            }
        });
    }
    public void onBackPressed() {
        finish();
    }
}
