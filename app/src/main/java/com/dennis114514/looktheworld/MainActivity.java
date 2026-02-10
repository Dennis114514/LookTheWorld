package com.dennis114514.looktheworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

import com.dennis114514.looktheworld.Games.GamesActivity;
import com.dennis114514.looktheworld.IT.ITActivity;
import com.dennis114514.looktheworld.News.NewsActivity;
import com.dennis114514.looktheworld.Settings.SettingsActivity;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置自定义顶栏（主页不需要返回按钮）
        CustomToolbarHelper.setupToolbarWithoutBack(this, getString(R.string.app_name));

        Button buttonNews = findViewById(R.id.buttonNews);
        Button buttonTechnology = findViewById(R.id.buttonTechnology);
        Button buttonGames = findViewById(R.id.buttonGames);
        Button buttonSettings = findViewById(R.id.buttonSettings);
        Button buttonExit = findViewById(R.id.buttonExit);

        buttonNews.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        buttonTechnology.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ITActivity.class);
            startActivity(intent);
        });

        buttonGames.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GamesActivity.class);
            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        buttonExit.setOnClickListener(v -> {
            handleDoubleBackToExit();
        });
    }

    private void handleDoubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            // 第二次点击，退出应用
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();

        // 2秒后重置状态
        mHandler.postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理Handler回调
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}