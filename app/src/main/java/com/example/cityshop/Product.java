package com.example.cityshop;

public class Product {
    private  int id;
    private String title;
    private String image;
    private String status;

    public Product(int id, String title, String image, String status) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.status = status;
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
}
