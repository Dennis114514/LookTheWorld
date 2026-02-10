package com.dennis114514.looktheworld.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;
import com.dennis114514.looktheworld.Utils.SettingsManager;

import com.dennis114514.looktheworld.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.settings));
        
        Button buttonAbout = findViewById(R.id.buttonAbout);

        buttonAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}