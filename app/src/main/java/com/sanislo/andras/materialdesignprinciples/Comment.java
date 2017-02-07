package com.sanislo.andras.materialdesignprinciples;

/**
 * Created by root on 07.02.17.
 */

public class Comment {
    private String photoUrl;
    private String authorName;
    private String commentText;

    public Comment(String photoUrl, String authorName, String commentText) {
        this.photoUrl = photoUrl;
        this.authorName = authorName;
        this.commentText = commentText;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "photoUrl='" + photoUrl + '\'' +
                ", authorName='" + authorName + '\'' +
                ", commentText='" + commentText + '\'' +
                '}';
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
