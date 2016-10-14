package cn.ucai.fulishop;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.ArrayList;

import bean.NewGoodsBean;
import cn.ucai.fulishop.fragment.BoutiqueFragment;
import cn.ucai.fulishop.fragment.CartsFragment;
import cn.ucai.fulishop.fragment.CategoryFragment;
import cn.ucai.fulishop.fragment.NewGoodsFragment;
import cn.ucai.fulishop.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {
    RadioButton mNewGoods,mBoutique,mCategory,mCart,mPersonal;

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
                indext = 3;
                break;
            case R.id.rb_Personal:
                indext = 4;
                break;
            default:
                indext = 0;
                break;
        }
        switchFragment(indext);
        if(view.getId()!=R.id.rb_Cart){
            mCart.setChecked(false);
        }else{
            mNewGoods.setChecked(false);
            mBoutique.setChecked(false);
            mCategory.setChecked(false);
            mPersonal.setChecked(false);
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
}
