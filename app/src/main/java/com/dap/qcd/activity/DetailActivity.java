/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dap.qcd.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.avos.avoscloud.AVObject;
import com.dap.club.R;
import com.dap.qcd.data.Detail;
import com.dap.qcd.util.DapLog;
import com.umeng.analytics.MobclickAgent;

public class DetailActivity extends AppCompatActivity {
    private WebView mViewPager;

    // details
    private Detail detail;
    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            //如果不需要其他对点击链接事件的处理返回true，否则返回false

            return true;
        }
    }
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mViewPager.canGoBack()) {
            mViewPager.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (detail == null) {
            return;
        }
        // 加载html
        mViewPager = (WebView) findViewById(R.id.webview);
        mViewPager.setWebViewClient(new webViewClient());

        WebSettings settings = mViewPager.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置宽度
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //设置字体大小
//        settings.setTextSize(WebSettings.TextSize.LARGEST);
        //为图片添加放大缩小功能
        settings.setSupportZoom(true); // 可以缩放
        settings.setBuiltInZoomControls(true); // 显示放大缩小 controler
//        mViewPager.loadUrl(detail.getUrl());
//        mViewPager.loadUrl("http://www.qiongsandai.com");
//        mViewPager.loadUrl("http://www.angularjs.cn/A2qy");
//        mViewPager.loadUrl("http://qiongsandai.com/post.html?id=56bffcf132132c0052b37471");
        if ("lib".equals(detail.getType())) {
            mViewPager.loadUrl("http://qicaidang.cn/device/" + detail.getId());
            detail.setUrl("http://qicaidang.cn/device/" + detail.getId());
        } else {
            mViewPager.loadUrl("http://qicaidang.cn/content/" + detail.getId());
            detail.setUrl("http://qicaidang.cn/content/" + detail.getId());
        }
//        mViewPager.loadData(detail.getHtml(), "text/html; charset=UTF-8", null);

        // 购买跳转
        FloatingActionButton buy = (FloatingActionButton) findViewById(R.id.buy);
        buy.hide();
//        if ("lib".equals(detail.getType()) || "news".equals(detail.getType())) {
//            buy.hide();
//        }
//        buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DapLog.e(detail.getBuy_url());
//                mViewPager.loadUrl(detail.getBuy_url());
////                mViewPager.loadUrl(detail.getFrom_url());
//            }
//        });
    }

    private void JsonToObj(AVObject avObject) {
        if (null != avObject) {
            Detail d = new Detail();
            try {
                d.setId(avObject.getObjectId());
                d.setTime(avObject.getCreatedAt().toString());
                d.setFrom_url(avObject.getString("url"));
                if ("Post".equals(avObject.getClassName())) {
                    d.setTitle(avObject.getString("title"));
                    d.setDetail(avObject.getString("content"));
                    d.setImg_url(avObject.getJSONObject("pic_left").optString("url"));
                    d.setBuy_url(avObject.getString("buy_url"));
                    d.setType(avObject.getString("doc_type"));
                    d.setUrl("http://www.qiongsandai.com/post.html?id=" + avObject.getObjectId());
                }
                if ("Lib".equals(avObject.getClassName())) {
                    d.setTitle(avObject.getString("brand") + avObject.getString("name") + avObject.getString("type"));
                    d.setDetail(avObject.getString("short_info"));
                    d.setImg_url(avObject.getJSONObject("pic_file").optString("url"));
                    d.setType("lib");
                    d.setUrl("http://www.qiongsandai.com/qicai.html?id=" + avObject.getObjectId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            detail=d;
        }
    }

    private void init() {
        Intent i = getIntent();
        AVObject av = i.getParcelableExtra("infos");
        JsonToObj(av);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menue_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DapLog.e("menu" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_open:
                Uri weburi = Uri.parse(detail.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, weburi);
                startActivity(webIntent);
            case R.id.action_share:
                Intent shareIntent = new Intent();

                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "器材党-"+detail.getTitle() + "-" +detail.getUrl());
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
