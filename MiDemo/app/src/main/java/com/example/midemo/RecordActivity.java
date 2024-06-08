package com.example.midemo;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.midemo.Adapter.RecordPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.example.midemo.frag_record.IncomeFragment;
import com.example.midemo.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1.查找控件
        tabLayout=findViewById(R.id.record_tabs);
        viewPager=findViewById(R.id.record_vp);
        //2.设置viewpager加载页面
        initPager();
    }

    private void initPager() {
        //初始化ViewPager页面的集合
        List<Fragment>fragmentList =new ArrayList<>();
        OutcomeFragment outFrag=new OutcomeFragment();//支出
        IncomeFragment inFrag=new IncomeFragment();//收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        // 创建适配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        // 设置适配器
        viewPager.setAdapter(pagerAdapter);
        //将TabLayout和ViwePager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.record_iv_back) {
            finish();
        }
    }

}