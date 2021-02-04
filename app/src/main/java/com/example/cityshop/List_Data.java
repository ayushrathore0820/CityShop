package com.example.cityshop;

public class List_Data {
    private String imageurl;
    private String imagenmae;
    private String imageid;

    public List_Data(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getImagenmae() {
        return imagenmae;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setImagenmae(String imagenmae) {
        this.imagenmae = imagenmae;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }
}
