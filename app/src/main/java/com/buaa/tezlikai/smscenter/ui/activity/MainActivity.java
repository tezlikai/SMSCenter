package com.buaa.tezlikai.smscenter.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buaa.tezlikai.smscenter.R;
import com.buaa.tezlikai.smscenter.adapter.MainPagerAdapter;
import com.buaa.tezlikai.smscenter.base.BaseActivity;
import com.buaa.tezlikai.smscenter.ui.fragment.ConversationFragment;
import com.buaa.tezlikai.smscenter.ui.fragment.GroupFragment;
import com.buaa.tezlikai.smscenter.ui.fragment.SearchFragment;
import com.buaa.tezlikai.smscenter.utils.LogUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {


    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    private TextView tv_tab_conversation;
    private TextView tv_tab_group;
    private TextView tv_tab_search;

    private MainPagerAdapter adapter;

    private LinearLayout ll_tab_conversation;
    private LinearLayout ll_tab_group;
    private LinearLayout ll_tab_search;

    private View v_indicate_line;


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        //取出布局文件中的组件
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        //会话、分组、搜索
        tv_tab_conversation = (TextView) findViewById(R.id.tv_tab_conversation);
        tv_tab_group = (TextView) findViewById(R.id.tv_tab_group);
        tv_tab_search = (TextView) findViewById(R.id.tv_tab_search);

        //获取按钮所在的布局
        ll_tab_conversation = (LinearLayout)findViewById(R.id.ll_tab_conversation);
        ll_tab_group = (LinearLayout)findViewById(R.id.ll_tab_group);
        ll_tab_search = (LinearLayout)findViewById(R.id.ll_tab_search);

        //标识的红线
        v_indicate_line = findViewById(R.id.v_indicate_line);

    }

    @Override
    public void initListener() {
        //viewPager界面切换时会触发
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动过程不断调用
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算红线位移的距离
                int distance = positionOffsetPixels / 3;

                //持续时间为0，立刻生效，因为红线的移动需要与用户滑动同步
                ViewPropertyAnimator.animate(v_indicate_line).translationX(distance + position * v_indicate_line.getWidth()).setDuration(200);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.i("lk",position + "");
                textLightAndScale();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //输入法管理器
                //隐藏输入法软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //给3个选项卡设置点击事件
        ll_tab_conversation.setOnClickListener(this);
        ll_tab_group.setOnClickListener(this);
        ll_tab_search.setOnClickListener(this);
    }

    @Override
    public void initData() {
        fragments = new ArrayList<Fragment>();

        //创建Fragment对象，存入集合
        ConversationFragment fragment1 = new ConversationFragment();
        GroupFragment fragment2 = new GroupFragment();
        SearchFragment fragment3 = new SearchFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        adapter = new MainPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

        //改变选项卡的文本的颜色和大小
        textLightAndScale();

        //设置红线的宽度
        computeIndicateLineWidth();
    }

    @Override
    public void processClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_conversation:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_tab_group:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_tab_search:
                viewPager.setCurrentItem(2);
                break;

        }

    }
    /**
     * 改变选项卡的文本的颜色和大小
     */
    private void textLightAndScale(){
        //获取viewPage当前显示界面的索引
        int item = viewPager.getCurrentItem();
        tv_tab_conversation.setTextColor(item == 0? Color.WHITE:0xaa666666);
        tv_tab_group.setTextColor(item == 1 ? Color.WHITE : 0xaa666666);
        tv_tab_search.setTextColor(item == 2 ? Color.WHITE : 0xaa666666);

        //  要操作的对象    改变宽度至指定比例
        ViewPropertyAnimator.animate(tv_tab_conversation).scaleX(item == 0? 1.2f:1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_group).scaleX(item == 1? 1.2f:1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_search).scaleX(item == 2? 1.2f:1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_conversation).scaleY(item == 0 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_group).scaleY(item == 1 ? 1.2f : 1).setDuration(200);
        ViewPropertyAnimator.animate(tv_tab_search).scaleY(item == 2 ? 1.2f : 1).setDuration(200);

    }

    /**
     * 计算红线宽度为屏幕1/3
     */
    private void computeIndicateLineWidth(){
        //拿到屏幕的三分之一
        int width = getWindowManager().getDefaultDisplay().getWidth();
        v_indicate_line.getLayoutParams().width = width/3;
    }
}
