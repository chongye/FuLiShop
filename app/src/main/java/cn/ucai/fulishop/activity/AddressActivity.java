package cn.ucai.fulishop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import bean.MessageBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulishop.Dao.NetDao;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.CommonUtils;
import cn.ucai.fulishop.utils.I;
import cn.ucai.fulishop.utils.L;
import cn.ucai.fulishop.utils.OkHttpUtils;

public class AddressActivity extends BaseActivity implements PaymentHandler{
    final String TAG = AddressActivity.class.getSimpleName();

    private static String URL = "http://218.244.151.190/demo/charge";

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

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
            gotoBuy();
        }
    }

    private void gotoBuy() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", orderPrice*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        int resultCode = data.getExtras().getInt("code");
        switch (resultCode){
            case 1:
                // 支付成功，删除购物车内传过来的商品
                paySuccess();
                CommonUtils.showShortToast("支付成功");
                break;
            case -1:
                CommonUtils.showShortToast("支付失败");
                finish();
                break;
        }
    }

    private void paySuccess() {
        for(String id:goodArr){
            NetDao.deleteCart(mcontext, Integer.parseInt(id), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        finish();
    }
}
