package com.dennis114514.looktheworld.News;

import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;
import android.os.Bundle;

public class ImportNewsActivity extends BaseRSSActivity {
    @Override
    protected String getRSSUrl() {
        return "https://www.chinanews.com.cn/rss/importnews.xml";
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.import_news);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getActivityTitle());
    }
}