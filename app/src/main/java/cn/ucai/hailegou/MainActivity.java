package cn.ucai.hailegou;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

import cn.ucai.hailegou.fragment.BoutiqueFragment;
import cn.ucai.hailegou.fragment.CartsFragment;
import cn.ucai.hailegou.fragment.CategoryFragment;
import cn.ucai.hailegou.fragment.NewGoodsFragment;
import cn.ucai.hailegou.fragment.PersonalFragment;
import cn.ucai.hailegou.utils.CommonUtils;
import cn.ucai.hailegou.utils.I;
import cn.ucai.hailegou.utils.MFGT;

public class MainActivity extends AppCompatActivity {
    final  String TAG = MainActivity.class.getSimpleName();

    RadioButton mNewGoods,mBoutique,mCategory,mCart,mPersonal;

    RadioButton[] rbs;

    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    CartsFragment cartsFragment;
    PersonalFragment personalFragment;
    ArrayList<Fragment> fragments;
    FragmentTransaction fragmentTransaction;
    int indext;
    int currentIndext = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mNewGoods = (RadioButton) findViewById(R.id.rb_NewsGood);
        mBoutique = (RadioButton) findViewById(R.id.rb_Boutique);
        mCategory = (RadioButton) findViewById(R.id.rb_Category);
        mCart = (RadioButton) findViewById(R.id.rb_Cart);
        mPersonal = (RadioButton) findViewById(R.id.rb_Personal);

        rbs = new RadioButton[5];
        rbs[0] = mNewGoods;
        rbs[1] = mBoutique;
        rbs[2] = mCategory;
        rbs[3] = mCart;
        rbs[4] = mPersonal;

        newGoodsFragment = new NewGoodsFragment();
        boutiqueFragment = new BoutiqueFragment();
        categoryFragment = new CategoryFragment();
        cartsFragment = new CartsFragment();
        personalFragment = new PersonalFragment();

        fragments = new ArrayList<>();
        fragments.add(newGoodsFragment);
        fragments.add(boutiqueFragment);
        fragments.add(categoryFragment);
        fragments.add(cartsFragment);
        fragments.add(personalFragment);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,newGoodsFragment).show(newGoodsFragment);
        fragmentTransaction.commit();
    }

    public void onCheckedChange(View view){
       /* if(view.getId()!=R.id.rb_Cart){ // 没点购物车
            mCart.setChecked(false);
        }else{  // 购物车被点击
            mNewGoods.setChecked(false);
            mBoutique.setChecked(false);
            mCategory.setChecked(false);
            mPersonal.setChecked(false);
        }*/
        switch (view.getId()){
            case R.id.rb_NewsGood:
                indext = 0;
                break;
            case R.id.rb_Boutique:
                indext = 1;
                break;
            case R.id.rb_Category:
                indext = 2;
                break;
            case R.id.rb_Cart:
                if(HiLeGouApplication.getUser()==null){
                    MFGT.gotoLoginActivity(this);
                }else {
                    indext = 3;
                }
                break;
            case R.id.rb_Personal:
                if(HiLeGouApplication.getUser()==null){
                    MFGT.gotoLoginActivity(this);
                }else {
                    indext = 4;
                }
                break;
            default:
                indext = 0;
                break;
        }
        switchFragment(indext);
        setRadioButon();
    }

    private void setRadioButon() {
        for(int i=0;i<rbs.length;i++){
            if(i==indext){
                rbs[i].setChecked(true);
            }else {
                rbs[i].setChecked(false);
            }
        }
    }

    private void switchFragment(int indext) {
        if(indext == currentIndext){
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragments.get(indext);
        if(!fragment.isAdded()){
            ft.add(R.id.fragment_container,fragment);
        }
        ft.show(fragment).hide(fragments.get(currentIndext)).commit();
        currentIndext = indext;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume，执行");
        if(HiLeGouApplication.getUser()==null&&indext!=4){
            indext = currentIndext;
        }else if(HiLeGouApplication.getUser()==null&&indext==4){
            indext =0;
        }
            switchFragment(indext);
            setRadioButon();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(HiLeGouApplication.getUser()!=null){
            Log.e(TAG,"onActivityResult，执行");
            if(requestCode == I.REQUEST_CODE_REQUEST)
            indext = 4;

        }
        if(requestCode == I.REQUEST_CODE_REQUEST_FOR_CART){
            indext = 3;
        }
    }
    long mExitTime = 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-mExitTime>2000){
            CommonUtils.showShortToast("再次点击退出");
            mExitTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }
}
