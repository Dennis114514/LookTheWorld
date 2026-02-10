package com.dennis114514.looktheworld.Utils;

import android.util.Log;
import android.util.Xml;

import com.dennis114514.looktheworld.Model.RSSChannel;
import com.dennis114514.looktheworld.Model.RSSItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RSSParser {
    private static final String TAG = "RSSParser";
    private static final String ns = null;
    
    public static RSSChannel parse(String xmlData) throws XmlPullParserException, IOException {
        Log.d(TAG, "Starting RSS parsing");
        
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();
        
        return readFeed(parser);
    }
    
    private static RSSChannel readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        RSSChannel channel = new RSSChannel();
        
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                readChannel(parser, channel);
            } else {
                skip(parser);
            }
        }
        return channel;
    }
    
    private static void readChannel(XmlPullParser parser, RSSChannel channel) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    channel.setTitle(readText(parser));
                    break;
                case "description":
                    channel.setDescription(readText(parser));
                    break;
                case "link":
                    channel.setLink(readText(parser));
                    break;
                case "language":
                    channel.setLanguage(readText(parser));
                    break;
                case "pubDate":
                    channel.setPubDate(readText(parser));
                    break;
                case "generator":
                    channel.setGenerator(readText(parser));
                    break;
                case "item":
                    channel.addItem(readItem(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
    }
    
    private static RSSItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        RSSItem item = new RSSItem();
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    item.setTitle(readText(parser));
                    break;
                case "description":
                    item.setDescription(readText(parser));
                    break;
                case "link":
                    item.setLink(readText(parser));
                    break;
                case "pubDate":
                    item.setPubDate(readText(parser));
                    break;
                case "guid":
                    item.setGuid(readText(parser));
                    break;
                case "author":
                    item.setAuthor(readText(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return item;
    }
    
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    // Helper method to extract image URLs from description
    public static String extractImageUrl(String description) {
        if (description == null || description.isEmpty()) {
            return null;
        }
        
        // 匹配<img>标签中的src属性
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        java.util.regex.Matcher matcher = pattern.matcher(description);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    // Helper method to clean HTML tags but preserve images for IT home
    public static String cleanDescriptionForDisplay(String description, boolean loadImages) {
        if (description == null || description.isEmpty()) {
            return "";
        }
        
        if (loadImages) {
            // 对于IT之家，保留图片标签
            return description;
        } else {
            // 对于其他栏目，移除图片标签
            return description.replaceAll("<img[^>]*>", "").trim();
        }
    }
    
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    
    // Helper method to format date for display
    public static String formatDate(String pubDate) {
        if (pubDate == null || pubDate.isEmpty()) {
            return "";
        }
        
        try {
            // Handle different date formats
            SimpleDateFormat[] formats = {
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            };
            
            Date date = null;
            for (SimpleDateFormat format : formats) {
                try {
                    date = format.parse(pubDate);
                    break;
                } catch (ParseException e) {
                    // Continue trying other formats
                }
            }
            
            if (date != null) {
                SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                return outputFormat.format(date);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date: " + pubDate, e);
        }
        
        return pubDate; // Return original if parsing fails
    }
}