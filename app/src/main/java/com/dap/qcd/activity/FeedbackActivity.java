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

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dap.qcd.R;
import com.umeng.analytics.MobclickAgent;

public class FeedbackActivity extends AppCompatActivity {
    private WebView mViewPager;

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
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
//        settings.setBuiltInZoomControls(true); // 显示放大缩小 controler
//        mViewPager.loadUrl(detail.getUrl());
//        mViewPager.loadUrl("http://www.qiongsandai.com");
//        mViewPager.loadUrl("http://www.angularjs.cn/A2qy");
//        mViewPager.loadUrl("http://qiongsandai.com/post.html?id=56bffcf132132c0052b37471");
        mViewPager.loadUrl("http://qicaidang.cn/feedback/");
//        mViewPager.loadData(detail.getHtml(), "text/html; charset=UTF-8", null);
        // 购买跳转
        FloatingActionButton buy = (FloatingActionButton) findViewById(R.id.buy);
        buy.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menue_detail, menu);
        return true;
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
