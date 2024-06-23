package com.example.connpeo;

import java.io.Serializable;

public class RecentlyViewed implements Serializable, Comparable<RecentlyViewed> {

    String name;
    String beiyong;

    String tel;
    String img;
    String fenzu;
    String pinyin;  //中文转换为拼音
    String start; //首字母


    public RecentlyViewed(String name, String beiyong, String tel, String img, String fenzu) {
        this.name = name;
        this.beiyong = beiyong;
        this.tel = tel;
        this.img = img;
        this.fenzu = fenzu;
        pinyin = Cn2Spell.getPinYin(name);
        start = pinyin.substring(0, 1).toUpperCase();
        if (!start.matches("[A-Z]")) {
            start = "#";
        }
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

    public String getPinyin() {
        return pinyin;
    }

    public String getStart() {
        return start;
    }

    @Override
    public int compareTo(RecentlyViewed user) {
        if (start.equals("#") && !user.getStart().equals("#")) {
            return 1;
        } else if (!start.equals("#") && user.getStart().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(user.getPinyin());
        }
    }
}