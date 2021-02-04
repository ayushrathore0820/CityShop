package com.example.cityshop;

public class Service {
    private  int id;
    private String title;
    private String image;
    private String status;
    private String mobile;

    public Service(int id, String title, String image, String status,String mobile) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.status = status;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getMobile() {
        return mobile;
    }
}
