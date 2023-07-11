package com.gtappdevelopers.instagram;

import java.util.Map;

public class FacebookFeedModal {
    public String getAuthorId() {
        return authorId;
    }

    // Constructor, getters, and setters


    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    private String authorId;
    //variables for storing our author image,author name, postDate,postDescription,post image,post likes,post comments.

    public FacebookFeedModal()
    {

    }
    private String authorImage;
    private String authorName;

    private String postDate;
    private String postDescription;
    private String postIV;
    private String postLikes;
    private String postComments;
    //creating getter and setter methods.

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    private String PostId;
    public String getPostIV() {
        return postIV;
    }

    public void setPostIV(String postIV) {
        this.postIV = postIV;
    }

    public String getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(String postLikes) {
        this.postLikes = postLikes;
    }

    public String getPostComments() {
        return postComments;
    }

    public void setPostComments(String postComments) {
        this.postComments = postComments;
    }

    //creating a constructor class.
    public FacebookFeedModal(String PostId, String authorId,String authorImage, String authorName, String postDate, String postDescription, String postIV, String postLikes, String postComments,String authorID) {

        this.PostId=PostId;

        this.authorId=authorId;
        this.authorImage = authorImage;
        this.authorName = authorName;
        this.postDate = postDate;
        this.postDescription = postDescription;
        this.postIV = postIV;
        this.postLikes = postLikes;
        this.postComments = postComments;
        this.authorId=authorID;
    }
}
