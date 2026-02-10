package com.dennis114514.looktheworld.Games.ZZZ;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;

public class ZZZActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);

        TextView textView = findViewById(R.id.textViewUnderConstruction);
        textView.setText(R.string.under_construction);
        
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, getString(R.string.zzz));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}