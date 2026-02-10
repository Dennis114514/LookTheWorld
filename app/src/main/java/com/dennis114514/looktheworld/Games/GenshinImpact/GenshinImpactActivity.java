package com.dennis114514.looktheworld.Games.GenshinImpact;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class GenshinImpactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genshin_impact);

        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.genshin_impact));

        Button buttonNotice = findViewById(R.id.buttonNotice);
        Button buttonEvents = findViewById(R.id.buttonEvents);
        Button buttonInfo = findViewById(R.id.buttonInfo);

        buttonNotice.setOnClickListener(v -> {
            Intent intent = new Intent(GenshinImpactActivity.this, GenshinImpactNotice.class);
            startActivity(intent);
        });

        buttonEvents.setOnClickListener(v -> {
            Intent intent = new Intent(GenshinImpactActivity.this, GenshinImpactEvents.class);
            startActivity(intent);
        });

        buttonInfo.setOnClickListener(v -> {
            Intent intent = new Intent(GenshinImpactActivity.this, GenshinImpactInfo.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}