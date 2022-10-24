package com.matt.unipay.model;

public class DashboardItem {
    String title;
    String content;

    public DashboardItem() {
    }

    public DashboardItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
