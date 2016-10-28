package cn.ucai.fulishop.activity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.sql.Array;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.L;

public class AddressActivity extends BaseActivity {
    final String TAG = AddressActivity.class.getSimpleName();
    Context mcontext;
    @BindView(R.id.backClickArea)
    ImageView backClickArea;
    @BindView(R.id.et_Name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_street)
    EditText etStreet;
    @BindView(R.id.bt_buy)
    Button btBuy;

    String goods;
    String[] goodArr;
    int orderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mcontext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        goods = getIntent().getStringExtra(I.Cart.GOODS_ID);
        orderPrice = getIntent().getIntExtra(I.Cart.Goods_SUM_PRICE,0);
        goodArr = goods.split(",");
        L.e(TAG, "goods:" + goods);
        L.e(TAG,"orderPrice:"+orderPrice);
        L.e(TAG,"goodArr:"+ Arrays.toString(goodArr));
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_buy)
    public void setBtBuy() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String street = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            etName.requestFocus();
            etName.setError("收货人不能为空");
            return;
        }else if(TextUtils.isEmpty(phone)){
            etPhone.requestFocus();
            etPhone.setError("手机号不能为空");
            return;
        }else if(TextUtils.isEmpty(street)){
            etStreet.requestFocus();
            etStreet.setError("街道地址不能为空");
            return;
        }else {

        }
    }
}
