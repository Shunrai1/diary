package com.shunrai.note.po;

import java.util.Date;

public class Diary {
    private int note_id;
    private String title;
    private String content;
    private int type_id= -1;//负值就是没有类型;
    private Date pub_time;//发布时间
    private String releaseDateStr;//日期文本，我们要格式化
    private int diaryCount;//日志计数
    private String typeName;//类别的名称
    private float lon;//发布地点经度
    private float lat;//发布地点纬度
    private String address;//地址

    public Diary() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Diary(String title, String content, int parseInt,float lon,float lat, String address ) {
        this.title =title;
        this.content = content;
        this.type_id = parseInt;
        this.lon = lon;
        this.lat = lat;
        this.address = address;
    }

    public String getReleaseDateStr() {
        return releaseDateStr;
    }

    public void setReleaseDateStr(String releaseDateStr) {
        this.releaseDateStr = releaseDateStr;
    }

    public int getDiaryCount() {
        return diaryCount;
    }

    public void setDiaryCount(int diaryCount) {
        this.diaryCount = diaryCount;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }



    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public Date getPub_time() {
        return pub_time;
    }

    public void setPub_time(Date pub_time) {
        this.pub_time = pub_time;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
