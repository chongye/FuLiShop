package cn.ucai.fulishop.activity;

import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_Nick)
    EditText etNick;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_pass)
    EditText etConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        DisplayUtils.initBackWithTitle(this,"账户户注册");
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_password)
    public void onClick() {
    }
}
