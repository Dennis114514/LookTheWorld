package com.dennis114514.looktheworld.News;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.news));

        Button buttonScrollNews = findViewById(R.id.buttonScrollNews);
        Button buttonImportNews = findViewById(R.id.buttonImportNews);

        buttonScrollNews.setOnClickListener(v -> {
            Intent intent = new Intent(NewsActivity.this, ScrollNewsActivity.class);
            startActivity(intent);
        });

        buttonImportNews.setOnClickListener(v -> {
            Intent intent = new Intent(NewsActivity.this, ImportNewsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}