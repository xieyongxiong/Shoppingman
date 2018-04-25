package com.codenofree.shoppingman.model;

/**
 * Created by xieyongxiong on 2018/4/25.
 */

public class pdModel {
    String title;
    String price;
    String image;
    String href;
    String source;

    public pdModel(String title, String price, String image, String href, String source) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.href = href;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
