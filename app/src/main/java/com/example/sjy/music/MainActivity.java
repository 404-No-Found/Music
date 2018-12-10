package com.example.sjy.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_personal)
    ImageView ivPersonal;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.list)
    RadioButton list;
    @BindView(R.id.start)
    RadioButton start;
    @BindView(R.id.more)
    RadioButton more;
    @BindView(R.id.rGroup)
    RadioGroup rGroup;
    @BindView(R.id.main_navigation)
    NavigationView mainNavigation;
    @BindView(R.id.my_drawer_layout)
    DrawerLayout myDrawerLayout;
    @BindView(R.id.fl_instead)
    FrameLayout flInstead;
    @BindView(R.id.fl_all)
    FrameLayout llAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_personal, R.id.search, R.id.more, R.id.start,R.id.list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_personal:
                myDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.more:
                replaceFragment(new myFindFragment());
                break;
            case R.id.start:
                Intent intent=new Intent(MainActivity.this,play.class);
                startActivity(intent);
                break;
            case R.id.list:
                Intent intent2=new Intent(MainActivity.this,myList.class);
                startActivity(intent2);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_instead, fragment);
        fragmentTransaction.commit();
    }
}
