package com.dennis114514.looktheworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dennis114514.looktheworld.Details.DetailsActivity;
import com.dennis114514.looktheworld.Model.RSSItem;
import com.dennis114514.looktheworld.R;
import com.dennis114514.looktheworld.Utils.RSSParser;

import java.util.ArrayList;
import java.util.List;

public class RSSItemAdapter extends RecyclerView.Adapter<RSSItemAdapter.ViewHolder> {
    private List<RSSItem> rssItems;
    private Context context;
    private OnItemClickListener listener;
    private boolean loadImageEnabled = false; // 默认不加载图片
    
    public interface OnItemClickListener {
        void onItemClick(RSSItem item);
    }
    
    public RSSItemAdapter(Context context) {
        this.context = context;
        this.rssItems = new ArrayList<>();
    }
    
    public RSSItemAdapter(Context context, boolean loadImageEnabled) {
        this.context = context;
        this.rssItems = new ArrayList<>();
        this.loadImageEnabled = loadImageEnabled;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    public void updateItems(List<RSSItem> newItems) {
        this.rssItems.clear();
        this.rssItems.addAll(newItems);
        notifyDataSetChanged();
    }
    
    public void addItems(List<RSSItem> newItems) {
        int startPosition = this.rssItems.size();
        this.rssItems.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rss_article, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RSSItem item = rssItems.get(position);
        
        // Set title
        holder.titleTextView.setText(item.getTitle());
        
        // Set publication date
        String formattedDate = RSSParser.formatDate(item.getPubDate());
        holder.dateTextView.setText(formattedDate);
        
        // 处理描述和图片
        String cleanDescription = RSSParser.cleanDescriptionForDisplay(item.getDescription(), loadImageEnabled);
        String description = stripHtmlTags(cleanDescription);
        if (description.length() > 100) {
            description = description.substring(0, 100) + "...";
        }
        holder.descriptionTextView.setText(description);
        
        // 列表页不加载图片，始终隐藏图片视图
        holder.imageView.setVisibility(View.GONE);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
            
            // Navigate to DetailsActivity
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("link", item.getLink());
            intent.putExtra("pubDate", item.getPubDate());
            intent.putExtra("author", item.getAuthor());
            intent.putExtra("loadImages", loadImageEnabled);
            context.startActivity(intent);
        });
    }
    
    @Override
    public int getItemCount() {
        return rssItems.size();
    }
    
    private String stripHtmlTags(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        return Html.fromHtml(html).toString().trim();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        ImageView imageView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.imageViewArticle);
        }
    }
}