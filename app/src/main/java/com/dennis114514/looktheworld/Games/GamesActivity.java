package com.dennis114514.looktheworld.Games;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.Games.GenshinImpact.GenshinImpactActivity;
import com.dennis114514.looktheworld.Games.HonkaiStarRailway.HonkaiStarRailwayActivity;
import com.dennis114514.looktheworld.Games.ZZZ.ZZZActivity;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.games));

        Button buttonGenshin = findViewById(R.id.buttonGenshin);
        Button buttonHonkai = findViewById(R.id.buttonHonkai);
        Button buttonZZZ = findViewById(R.id.buttonZZZ);

        buttonGenshin.setOnClickListener(v -> {
            Intent intent = new Intent(GamesActivity.this, GenshinImpactActivity.class);
            startActivity(intent);
        });

        buttonHonkai.setOnClickListener(v -> {
            Intent intent = new Intent(GamesActivity.this, HonkaiStarRailwayActivity.class);
            startActivity(intent);
        });

        buttonZZZ.setOnClickListener(v -> {
            Intent intent = new Intent(GamesActivity.this, ZZZActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}