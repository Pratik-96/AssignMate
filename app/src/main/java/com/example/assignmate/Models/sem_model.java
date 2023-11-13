package com.example.assignmate.Models;

public class sem_model {
    private String sub_name;
    private String img_url;

    public sem_model()
    {

    }
    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public sem_model(String sub_name, String img_url) {
        this.sub_name = sub_name;
        this.img_url = img_url;
    }
}
