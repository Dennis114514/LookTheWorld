package com.dennis114514.looktheworld.Games.GenshinImpact;

import android.os.Bundle;

import com.dennis114514.looktheworld.News.BaseRSSActivity;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class GenshinImpactInfo extends BaseRSSActivity {
    @Override
    protected String getRSSUrl() {
        return "https://rsshub.rssforever.com/hoyolab/news/zh-cn/2/3";
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.info);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getActivityTitle());
    }
}