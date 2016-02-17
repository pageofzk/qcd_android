package com.dap.club.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dap.club.R;
import com.dap.club.fragment.BaseListFragment;
import com.dap.club.fragment.BuyListFragment;
import com.dap.club.fragment.LibsListFragment;
import com.dap.club.fragment.NewsListFragment;
import com.dap.club.util.DapLog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener{
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        if (null!=navigationView){
            setupDrawerContent(navigationView);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mTabLayout= (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new NewsListFragment(), "资讯");
        adapter.addFragment(new BuyListFragment(), "剁手");
        adapter.addFragment(new LibsListFragment(), "器材库");
//        adapter.addFragment(new CheeseListFragment(), "发现");
        viewPager.setAdapter(adapter);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        if (menuItem.getItemId() == R.id.nav_feedback) {
                            Intent intent = new Intent(MainActivity.this, CheeseDetailActivity.class);
                            startActivity(intent);
                        }
                        if (menuItem.getItemId() == R.id.nav_about) {
                            Intent intent = new Intent(MainActivity.this, CheeseDetailActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        // 为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("搜索");

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                DapLog.e("=======================open");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                DapLog.e("=======================close");
                adapter.clearSearch();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        DapLog.e("=======================" + query);
        if (query != null && query.length() > 0) {
            adapter.search(query);
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DapLog.e("menu" + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class Adapter extends FragmentPagerAdapter {
        private final List<BaseListFragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private FragmentManager fragmentManager;
        private android.support.v4.app.FragmentTransaction beginTransaction;
        public Adapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
            beginTransaction = fragmentManager.beginTransaction();
        }

        public void search(String query) {
            for (BaseListFragment fragment:mFragments) {
                fragment.search(query);
            }
        }

        public void clearSearch() {
            for (BaseListFragment fragment:mFragments) {
                fragment.clearSearch();
            }
        }

        public void addFragment(BaseListFragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
