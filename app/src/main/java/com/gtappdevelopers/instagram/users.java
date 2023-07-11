package com.gtappdevelopers.instagram;

public class users {

    String userId;
    String Image;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;
    String Bio;
    String Address;
    String Website;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    String Phone;

    public users(){

    }

    public users(String userId, String Image, String Name) {
        this.userId = userId;
        this.Image = Image;
        this.Name = Name;
    }



    public users(String userId, String image, String name, String bio, String address, String website, String phone) {
        this.userId = userId;
        Image = image;
        Name = name;
        Bio = bio;
        Address = address;
        Website = website;
        Phone = phone;
    }

    public users(String Image, String Name)
    {

        this.Image = Image;
        this.Name = Name;
}


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




}
