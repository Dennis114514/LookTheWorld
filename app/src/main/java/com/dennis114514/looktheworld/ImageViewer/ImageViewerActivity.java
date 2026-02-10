package com.dennis114514.looktheworld.ImageViewer;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dennis114514.looktheworld.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageViewerActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    private ImageView imageView;
    private FloatingActionButton fabSave;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float lastY = 0;
    private boolean isScaling = false;
    private String imageUrl;
    private Bitmap loadedBitmap;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        
        imageView = findViewById(R.id.imageViewFullscreen);
        fabSave = findViewById(R.id.fabSave);
        imageUrl = getIntent().getStringExtra("image_url");

        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            loadImage();
        }
        
        setupScaleGestureDetector();
        setupGestureDetector();
        setupSaveButton();
    }
    
    private void setupScaleGestureDetector() {
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 5.0f));
                applyScale();
                return true;
            }
        });
    }
    
    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 双击缩放功能
                if (scaleFactor > 1.0f) {
                    scaleFactor = 1.0f; // 双击恢复到原始大小
                } else {
                    scaleFactor = 2.0f; // 双击放大到2倍
                }
                applyScale();
                return true;
            }
        });
    }
    
    private void setupSaveButton() {
        fabSave.setOnClickListener(v -> {
            checkPermissionAndSaveImage();
        });
    }
    
    private void applyScale() {
        imageView.setScaleX(scaleFactor);
        imageView.setScaleY(scaleFactor);
    }
    
    private void loadImage() {
        new ImageLoadTask().execute(imageUrl);
    }
    
    private void checkPermissionAndSaveImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11及以上，使用MediaStore，理论上不需要传统存储权限
            // 但为了保险起见，还是检查一下
            if (Environment.isExternalStorageManager()) {
                saveImage();
            } else {
                // 引导用户开启所有文件访问权限
                Toast.makeText(this, "请在设置中允许访问所有文件权限", Toast.LENGTH_LONG).show();
                // 可以在这里添加跳转到设置页面的代码
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0-10，检查存储权限
            int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            
            if (writePermission != PackageManager.PERMISSION_GRANTED || 
                readPermission != PackageManager.PERMISSION_GRANTED) {
                // 权限未授予，申请权限
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
            } else {
                // 权限已授予，直接保存
                saveImage();
            }
        } else {
            // Android 6.0以下，直接保存
            saveImage();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                Toast.makeText(this, "权限已获得，正在保存图片...", Toast.LENGTH_SHORT).show();
                saveImage();
            } else {
                Toast.makeText(this, "需要存储权限才能保存图片，请在设置中手动授予权限", Toast.LENGTH_LONG).show();
                // 可以引导用户去设置页面手动授权
                // Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                // Uri uri = Uri.fromParts("package", getPackageName(), null);
                // intent.setData(uri);
                // startActivity(intent);
            }
        }
    }
    
    private void saveImage() {
        if (loadedBitmap != null) {
            new SaveImageTask().execute(loadedBitmap);
        } else {
            Toast.makeText(this, "图片尚未加载完成", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理双击手势
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        
        // 处理双指缩放
        if (scaleGestureDetector.onTouchEvent(event)) {
            return true;
        }
        
        // 处理单指缩放
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                isScaling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && Math.abs(event.getY() - lastY) > 50) {
                    isScaling = true;
                }
                if (isScaling) {
                    float deltaY = event.getY() - lastY;
                    // 向上滑动放大，向下滑动缩小
                    scaleFactor += deltaY * 0.01f;
                    scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 5.0f));
                    applyScale();
                    lastY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                isScaling = false;
                break;
        }
        
        return true;
    }
    
    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                connection.disconnect();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                loadedBitmap = bitmap;
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(ImageViewerActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10及以上使用MediaStore
                    return saveImageWithMediaStore(bitmap);
                } else {
                    // Android 9及以下使用传统方式
                    return saveImageWithLegacyMethod(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        
        private boolean saveImageWithMediaStore(Bitmap bitmap) {
            try {
                ContentValues values = new ContentValues();
                String fileName = "image_" + System.currentTimeMillis() + ".jpg";
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/LookTheWorld");
                
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.close();
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        
        private boolean saveImageWithLegacyMethod(Bitmap bitmap) {
            try {
                // 使用固定的下载目录
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                
                // 创建文件名
                String fileName = "image_" + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(downloadDir, fileName);
                
                // 保存图片
                FileOutputStream out = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                
                // 通知系统媒体库更新
                sendBroadcast(new android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
                    Uri.fromFile(imageFile)));
                
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(ImageViewerActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ImageViewerActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}