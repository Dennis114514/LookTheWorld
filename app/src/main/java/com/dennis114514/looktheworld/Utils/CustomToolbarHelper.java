package com.dennis114514.looktheworld.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dennis114514.looktheworld.R;

public class CustomToolbarHelper {
    
    public static void setupToolbar(Activity activity, String title) {
        setupToolbar(activity, title, null);
    }
    
    public static void setupToolbar(Activity activity, String title, View.OnClickListener actionListener) {
        View toolbar = activity.findViewById(R.id.customToolbar);
        if (toolbar == null) return;
        
        TextView titleTextView = toolbar.findViewById(R.id.textViewTitle);
        ImageButton backButton = toolbar.findViewById(R.id.buttonBack);
        ImageButton actionButton = toolbar.findViewById(R.id.buttonAction);
        
        // 设置标题
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
        
        // 设置返回按钮
        if (backButton != null) {
            backButton.setOnClickListener(v -> activity.onBackPressed());
        }
        
        // 设置整个toolbar的点击返回功能（除了action按钮区域）
        toolbar.setOnClickListener(v -> {
            // 检查点击位置是否在action按钮上
            if (actionListener != null && actionButton != null && actionButton.getVisibility() == View.VISIBLE) {
                // 如果有action按钮且可见，则不处理toolbar点击
                return;
            }
            activity.onBackPressed();
        });
        
        // 设置action按钮
        if (actionListener != null && actionButton != null) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(actionListener);
        }
    }
    
    public static void setupToolbarWithoutBack(Activity activity, String title) {
        View toolbar = activity.findViewById(R.id.customToolbar);
        if (toolbar == null) return;
        
        TextView titleTextView = toolbar.findViewById(R.id.textViewTitle);
        ImageButton backButton = toolbar.findViewById(R.id.buttonBack);
        
        // 设置标题
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
        
        // 隐藏返回按钮
        if (backButton != null) {
            backButton.setVisibility(View.GONE);
        }
    }
}