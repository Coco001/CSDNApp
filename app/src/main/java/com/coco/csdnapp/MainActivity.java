package com.coco.csdnapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.coco.csdnapp.adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView headTV;
    private TabLayout id_indicator;
    private ViewPager id_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        id_pager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        id_indicator.setupWithViewPager(id_pager);
    }

    private void initView() {
        headTV = (TextView) findViewById(R.id.headTV);
        id_indicator = (TabLayout) findViewById(R.id.id_indicator);
        id_pager = (ViewPager) findViewById(R.id.id_pager);
    }
}
