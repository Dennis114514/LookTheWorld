package com.dennis114514.looktheworld.News;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

import com.dennis114514.looktheworld.Adapter.RSSItemAdapter;
import com.dennis114514.looktheworld.Model.RSSChannel;
import com.dennis114514.looktheworld.Model.RSSItem;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.NetworkUtils;
import com.dennis114514.looktheworld.Utils.RSSParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public abstract class BaseRSSActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar progressBar;
    protected TextView textViewError;
    protected RSSItemAdapter adapter;
    
    protected abstract String getRSSUrl();
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

        new LoadRSSTask().execute(getRSSUrl());
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

    private class LoadRSSTask extends AsyncTask<String, Void, RSSChannel> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            showLoading(true);
        }

        @Override
        protected RSSChannel doInBackground(String... urls) {
            try {
                String xmlData = NetworkUtils.fetchDataFromUrl(urls[0]);
                return RSSParser.parse(xmlData);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(RSSChannel channel) {
            if (exception != null) {
                showError(getString(R.string.network_error) + ": " + exception.getMessage());
                return;
            }

            if (channel != null && channel.getItems() != null) {
                List<RSSItem> items = channel.getItems();
                adapter.updateItems(items);
                showContent();
            } else {
                showError(getString(R.string.network_error));
            }
        }
    }
}