package com.example.shop;

import java.io.Serializable;

public class Post implements Serializable {

    private String resourceDrawable;
    private String postText;

    public Post() {}
    public Post(String postText, String resourceDrawable) {

        this.postText = postText;
        this.resourceDrawable = resourceDrawable;
    }

    public Post(Post post) {

        this.postText = post.getPostText();
        this.resourceDrawable = post.getResourceDrawable();
    }

    public String getPostText() {

        return postText;
    }
    public String getResourceDrawable() {

        return resourceDrawable;
    }
    public void setPostText(String postText){

        this.postText = postText;
    }

    public void setResourceDrawable(String resourceDrawable) {

        this.resourceDrawable = resourceDrawable;
    }
}
