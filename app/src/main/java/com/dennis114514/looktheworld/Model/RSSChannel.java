package com.dennis114514.looktheworld.Model;

import java.util.ArrayList;
import java.util.List;

public class RSSChannel {
    private String title;
    private String description;
    private String link;
    private String language;
    private String pubDate;
    private String generator;
    private List<RSSItem> items;
    
    public RSSChannel() {
        this.items = new ArrayList<>();
    }
    
    public RSSChannel(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.items = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getPubDate() {
        return pubDate;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    
    public String getGenerator() {
        return generator;
    }
    
    public void setGenerator(String generator) {
        this.generator = generator;
    }
    
    public List<RSSItem> getItems() {
        return items;
    }
    
    public void setItems(List<RSSItem> items) {
        this.items = items;
    }
    
    public void addItem(RSSItem item) {
        this.items.add(item);
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    @Override
    public String toString() {
        return "RSSChannel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", itemsCount=" + items.size() +
                '}';
    }
}