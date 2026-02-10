package com.dennis114514.looktheworld.Details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

import com.dennis114514.looktheworld.ImageViewer.ImageViewerActivity;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.CustomToolbarHelper;
import com.dennis114514.looktheworld.Utils.RSSParser;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView textViewTitle = findViewById(R.id.textViewDetailTitle);
        TextView textViewDate = findViewById(R.id.textViewDetailDate);
        TextView textViewAuthor = findViewById(R.id.textViewDetailAuthor);
        TextView textViewDescription = findViewById(R.id.textViewDetailDescription);
        ImageView imageViewDetail = findViewById(R.id.imageViewDetail);
        Button buttonOpenLink = findViewById(R.id.buttonOpenLink);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String link = getIntent().getStringExtra("link");
        String pubDate = getIntent().getStringExtra("pubDate");
        String author = getIntent().getStringExtra("author");
        boolean loadImages = getIntent().getBooleanExtra("loadImages", false);

        // Set data to views
        textViewTitle.setText(title != null ? title : "");
        
        String formattedDate = RSSParser.formatDate(pubDate);
        textViewDate.setText(formattedDate);
        
        if (author != null && !author.isEmpty()) {
            textViewAuthor.setText("作者: " + author);
            textViewAuthor.setVisibility(android.view.View.VISIBLE);
        }
        
        if (description != null) {
            if (loadImages) {
                // 显示完整内容包括图片
                textViewDescription.setText(Html.fromHtml(description));
                
                // 加载首张图片到详情页顶部
                String imageUrl = RSSParser.extractImageUrl(description);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    imageViewDetail.setVisibility(View.VISIBLE);
                    imageViewDetail.setImageResource(R.drawable.ic_launcher_background); // 设置占位图
                    // 异步加载图片
                    new DetailImageLoadTask(imageViewDetail).execute(imageUrl);
                    
                    // 点击图片打开图片查看器
                    imageViewDetail.setOnClickListener(v -> {
                        Intent intent = new Intent(DetailsActivity.this, ImageViewerActivity.class);
                        intent.putExtra("image_url", imageUrl);
                        startActivity(intent);
                    });
                } else {
                    imageViewDetail.setVisibility(View.GONE);
                }
            } else {
                // 不加载图片，只显示清理后的文本
                String cleanDescription = RSSParser.cleanDescriptionForDisplay(description, false);
                textViewDescription.setText(Html.fromHtml(cleanDescription));
                imageViewDetail.setVisibility(View.GONE);
            }
        } else {
            imageViewDetail.setVisibility(View.GONE);
        }

        buttonOpenLink.setOnClickListener(v -> {
            if (link != null && !link.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
        
        // 设置自定义顶栏
        CustomToolbarHelper.setupToolbar(this, "文章详情");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    // 详情页图片加载异步任务
    private class DetailImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        
        public DetailImageLoadTask(ImageView imageView) {
            this.imageView = imageView;
        }
        
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
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}