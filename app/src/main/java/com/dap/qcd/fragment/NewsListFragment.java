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

package com.dap.qcd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.dap.qcd.R;
import com.dap.qcd.activity.DetailActivity;
import com.dap.qcd.adapter.HomeAdapter;
import com.dap.qcd.data.Home;
import com.dap.qcd.util.DapLog;
import com.dap.qcd.util.Worker;
import com.dap.qcd.view.DividerItemDecoration;
import com.dap.qcd.view.refresh.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener{
    private View root;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private boolean isSlidingToLast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cheese_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        load();
    }

    private void init() {
        root = getView();

        adapter = new HomeAdapter(getActivity());

        // 加载图
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);

        // 监听点击事件
        adapter.setItemClick(new HomeAdapter.ClickListener() {
            @Override
            public void onClick(int data, View v) {
                DapLog.e("!!!!!!!!!!!!!!!!!!!!!!!" + data);
                if (homes != null && homes.size() > data) {
                    Home h = homes.get(data);
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("infos", h.getInfos());
                    startActivity(intent);
                }
            }
        });
        adapter.setPraiseClick(new HomeAdapter.ClickListener() {
            @Override
            public void onClick(final int data, View v) {
                DapLog.e(data + "=======================" +  homes.get(data).getId());
                if (homes != null && homes.size() > data) {
                    AVObject post = AVObject.createWithoutData("Post", homes.get(data).getId());
                    post.increment("good");
//                    post.put("good",homes.get(data).getGood() + 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                homes.get(data).setGood(homes.get(data).getGood() + 1);
                                Worker.postMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.setmData(homes);
                                    }
                                });
                            } else {
                                Log.e("LeanCloud", "Save failed.");
                            }
                        }
                    });
                }
            }
        });
    }

    private int pageNum;
    private static final int LIMITSIZE = 20;
    private boolean isLoading;

    private void load() {
        DapLog.e("searchQuery:" + searchQuery + isLoading + refreshLayout.isRefreshing());
        if (isLoading) {
            return;
        }
        refreshLayout.setRefreshing(true);
        isLoading = true;
        Worker.postWorker(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("Post");
                if (searchQuery.length() > 0) {
                    AVQuery<AVObject> titleQuery = AVQuery.getQuery("Post");
                    titleQuery.whereMatches("title", "(?i)" + searchQuery);

                    AVQuery<AVObject> tagQuery = AVQuery.getQuery("Post");
                    tagQuery.whereMatches("tag", "(?i)" + searchQuery);

                    AVQuery<AVObject> mallQuery = AVQuery.getQuery("Post");
                    mallQuery.whereMatches("mall", "(?i)" + searchQuery);

                    List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
                    queries.add(titleQuery);
                    queries.add(tagQuery);
//                    queries.add(mallQuery);
                    query = AVQuery.or(queries);
                    // dosearch
//                    query.whereMatches("title", "(?i)" + searchQuery);
                }
                query.whereEqualTo("doc_type", "news");
                query.limit(LIMITSIZE);
                query.skip(pageNum * LIMITSIZE);
                query.orderByDescending("time");
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> avObjects, AVException e) {
                        Worker.postMain(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });
                        if (e == null) {
                            JsonToObj(avObjects);
                            pageNum++;
                        }
                        isLoading = false;
                    }
                });
            }
        });

    }

    private List<Home> homes;

    private void JsonToObj(final List<AVObject> avObjects) {
        if (null != avObjects) {
            if (null == homes) {
                homes = new ArrayList<>();
            }
            for (int i = 0; i < avObjects.size(); i++) {
                Home home = new Home();
                try {
                    home.setId(avObjects.get(i).getObjectId());
                    home.setTime(avObjects.get(i).getCreatedAt().toString());
                    home.setTitle(avObjects.get(i).getString("title"));
                    String detail = avObjects.get(i).getString("content");
                    if (detail.length() > 50) {
                        detail = avObjects.get(i).getString("content").substring(0,50) + "...";
                    }
                    home.setDetail(detail);
                    home.setType(avObjects.get(i).getString("doc_type"));
                    home.setGood(avObjects.get(i).getNumber("good").intValue());
                    home.setUrl(avObjects.get(i).getJSONObject("pic_left").optString("url"));
                    home.setInfos(avObjects.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                homes.add(home);
            }
            Worker.postMain(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                    adapter.setmData(homes);

                }
            });
        }
    }

    private HomeAdapter adapter;

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager manager=new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(manager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), OrientationHelper.VERTICAL));
        recyclerView.setItemAnimator(animator);

        recyclerView.setAdapter(adapter);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == (totalItemCount -1) && isSlidingToLast) {
                    //加载更多功能的代码
                    load();
                }

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Worker.postMain(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                }else{
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        pageNum=0;
        homes=null;
        load();
    }

    @Override
    public void search(String query) {
        this.searchQuery=query;
        DapLog.e("search" + query);
        if (this.isResumed()) {
            pageNum=0;
            homes=null;
            load();
        }
    }

    @Override
    public void clearSearch() {
        this.searchQuery="";
        DapLog.e("clear");
        if (this.isResumed()) {
            pageNum=0;
            homes=null;
            load();
        }
    }
    @Override
    public void toTop() {
        DapLog.e("toTop");
        if (this.isResumed()) {
            recyclerView.setAdapter(adapter);
        }
    }
}
