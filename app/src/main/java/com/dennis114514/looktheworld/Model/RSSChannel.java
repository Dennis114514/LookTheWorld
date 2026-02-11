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

    
    // Getters and Setters
    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public void setLink(String link) {
        this.link = link;
    }

    
    public void setLanguage(String language) {
        this.language = language;
    }

    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    
    public void setGenerator(String generator) {
        this.generator = generator;
    }
    
    public List<RSSItem> getItems() {
        return items;
    }

    
    public void addItem(RSSItem item) {
        this.items.add(item);
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