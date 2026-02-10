package com.dennis114514.looktheworld.Settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.about));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}