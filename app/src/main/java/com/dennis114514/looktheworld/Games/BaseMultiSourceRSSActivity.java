package com.dennis114514.looktheworld.Games;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dennis114514.looktheworld.Adapter.RSSItemAdapter;
import com.dennis114514.looktheworld.Model.RSSChannel;
import com.dennis114514.looktheworld.Model.RSSItem;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;
import com.dennis114514.looktheworld.Utils.NetworkUtils;
import com.dennis114514.looktheworld.Utils.RSSParser;

import java.util.Arrays;
import java.util.List;

/**
 * 支持多源RSS尝试的游戏基础Activity
 * 提供7个RSS源的故障转移机制
 */
public abstract class BaseMultiSourceRSSActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar progressBar;
    protected TextView textViewError;
    protected RSSItemAdapter adapter;
    
    // 多源RSS链接头配置（按优先级排序）
    private static final String[] RSS_SOURCES = {
        "https://rss.injahow.cn/",
        "https://rsshub.rssforever.com/",
        "https://rss.shab.fun/",
        "https://hub.slarker.me/",
        "https://rsshub.anyant.xyz/",
        "http://rsshub.sksren.com/",
        "https://i.scnu.edu.cn/sub/"
    };
    
    protected abstract String getRSSPath(); // 获取相对路径部分
    protected abstract String getActivityTitle();
    protected boolean shouldLoadImages() { return false; } // 默认不加载图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_list);

        initViews();
        setupRecyclerView();
        setupSwipeRefresh();
        
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getActivityTitle());
        
        loadData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);
        textViewError = findViewById(R.id.textViewError);
    }

    private void setupRecyclerView() {
        adapter = new RSSItemAdapter(this, shouldLoadImages());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
    }

    protected void loadData() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showError(getString(R.string.network_error));
            return;
        }

        new MultiSourceLoadTask().execute();
    }

    protected void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        showLoading(false);
    }

    protected void showContent() {
        textViewError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        showLoading(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * 多源加载异步任务
     * 依次尝试不同的RSS源，直到成功或全部失败
     */
    private class MultiSourceLoadTask extends AsyncTask<Void, Integer, RSSChannel> {
        private Exception lastException;
        private String currentSourceUrl;
        private int attemptedSources = 0;

        @Override
        protected void onPreExecute() {
            showLoading(true);
            textViewError.setVisibility(View.GONE);
        }

        @Override
        protected RSSChannel doInBackground(Void... params) {
            String rssPath = getRSSPath();
            
            for (int i = 0; i < RSS_SOURCES.length; i++) {
                if (isCancelled()) {
                    return null;
                }
                
                String baseUrl = RSS_SOURCES[i];
                currentSourceUrl = baseUrl + rssPath;
                publishProgress(i + 1); // 更新进度
                
                try {
                    String xmlData = NetworkUtils.fetchDataFromUrl(currentSourceUrl);
                    
                    // 验证返回的是有效的XML数据
                    if (isValidXml(xmlData)) {
                        RSSChannel channel = RSSParser.parse(xmlData);
                        if (channel != null && channel.getItems() != null && !channel.getItems().isEmpty()) {
                            attemptedSources = i + 1;
                            return channel;
                        }
                    }
                } catch (Exception e) {
                    lastException = e;
                    // 继续尝试下一个源
                }
            }
            
            attemptedSources = RSS_SOURCES.length;
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // 可以在这里更新尝试进度的UI
        }

        @Override
        protected void onPostExecute(RSSChannel channel) {
            if (channel != null && channel.getItems() != null) {
                List<RSSItem> items = channel.getItems();
                adapter.updateItems(items);
                showContent();
            } else {
                String errorMessage;
                if (lastException != null) {
                    errorMessage = getString(R.string.network_error) + ": " + lastException.getMessage();
                } else {
                    errorMessage = getString(R.string.network_error) + ": 所有RSS源均无法访问";
                }
                showError(errorMessage);
            }
        }

        /**
         * 简单验证XML格式有效性
         */
        private boolean isValidXml(String xmlData) {
            if (xmlData == null || xmlData.trim().isEmpty()) {
                return false;
            }
            
            // 检查是否包含基本的XML标签
            return xmlData.contains("<?xml") || 
                   xmlData.contains("<rss") || 
                   xmlData.contains("<feed");
        }
    }
}