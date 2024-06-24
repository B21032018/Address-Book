package com.example.connpeo;

public class Fenzued {

    String name;
    String beiyong;
    String tel;
    String img;
    String fenzu;

    public Fenzued(String name, String beiyong, String tel, String img, String fenzu) {
        this.name = name;
        this.beiyong = beiyong;
        this.tel = tel;
        this.img = img;
        this.fenzu = fenzu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getbeiyong() {
        return beiyong;
    }

    public String gettel() {
        return tel;
    }

    public String getimg() {
        return img;
    }

    public String getfenzu() {
        return fenzu;
    }

}