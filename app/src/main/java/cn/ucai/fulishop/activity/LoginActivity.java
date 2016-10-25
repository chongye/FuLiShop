package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

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
import cn.ucai.fulishop.views.DisplayUtils;

public class LoginActivity extends BaseActivity {
    private final String TB_USER = "tb_user";

    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    String userName;
    String password;
    Context mContext;

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
        mContext = this;
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
        NetDao.login(this, userName, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Result result1 = ResultUtils.getResultFromJson(result, UserAvatar.class);
                if(result == null){
                    return;
                }else if(result1.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                    CommonUtils.showShortToast("账户不存在");
                    return;
                }else if(result1.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                    CommonUtils.showShortToast("账户密码错误");
                    return;
                }
                UserAvatar user = (UserAvatar) result1.getRetData();
                L.e("user","user="+user.toString());

                 /*//  result.getRetData()为json的String文件
                 Gson gson = new Gson();
                 String json = result.getRetData().toString();
                 UserAvatar user = gson.fromJson(json,UserAvatar.class);*/
                UserAvatarDao userDao = new UserAvatarDao(mContext);
                userDao.saveUserAvatar(user);
                boolean isSuccess = userDao.saveUserAvatar(user);
                if(isSuccess){
                    CommonUtils.showShortToast(user.toString());
                    SharePrefrenceUtils.getInstance(mContext).saveUser(user.getMuserName());
                    FuLiShopApplication.setUser(user);
                    setResult(I.REQUEST_CODE_REQUEST);
                    MFGT.finish((Activity) mContext);
                }else {
                    CommonUtils.showShortToast(R.string.user_database_error);
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                L.e("error="+error);
            }
        });
    }/*private String muserName;
    private String muserNick;
    private int mavatarId;
    private String mavatarPath;
    private String mavatarSuffix;
    private int mavatarType;
    private String mavatarLastUpdateTime;*/

    private void onSaveSQLite(UserAvatar user) {
        SQLiteDatabase sq = openOrCreateDatabase("user.db",MODE_PRIVATE,null);
        String sql = "create table "+TB_USER+"("
                +"id integer primary key autoincrement,"
                +"muserName varchar,"
                +"muserNick varchar,"
                +"mavatarId integer,"
                +"mavatarPath varchar,"
                +"mavatarSuffix varchar,"
                +"mavatarType integer,"
                +"mavatarLastUpdateTime varchar)";
        L.e("sql",sql);
        sq.execSQL(sql);
        sq.execSQL("insert into "+TB_USER+"(muserName,muserNick,mavatarId,mavatarPath,mavatarSuffix" +
                ",mavatarType,mavatarLastUpdateTime) values('"+user.getMuserName()+"','"+user.getMuserNick()+"','"
                +user.getMavatarId()+"','"+user.getMavatarPath()+"','"+user.getMavatarSuffix()+"','"+user.getMavatarType()
                +"','"+user.getMavatarLastUpdateTime()+"')");
    }
    public void onBackPressed(){
        finish();
    }
}
