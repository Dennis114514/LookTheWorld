package com.dennis114514.looktheworld.Model;

public class RSSItem {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String guid;
    private String author;
    
    public RSSItem() {}
    
    public RSSItem(String title, String description, String link, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
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
    
    public String getPubDate() {
        return pubDate;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    
    public String getGuid() {
        return guid;
    }
    
    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "RSSItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}