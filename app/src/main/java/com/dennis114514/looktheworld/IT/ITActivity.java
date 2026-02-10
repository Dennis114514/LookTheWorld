package com.dennis114514.looktheworld.IT;

import android.os.Bundle;

import com.dennis114514.looktheworld.News.BaseRSSActivity;
import com.dennis114514.looktheworld.R;

public class ITActivity extends BaseRSSActivity {
    @Override
    protected String getRSSUrl() {
        return "https://www.ithome.com/rss/";
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.it_home);
    }
    
    @Override
    protected boolean shouldLoadImages() {
        return true; // IT之家启用图片加载
    }
    
    // 移除onCreate重写，使用父类的实现
}