package com.shunrai.note.po;

public class DiaryType {
    private int type_id;
    private String type_name;
    private int user_id;
    private int diaryCount;//每个类别下有多少条日志

    public DiaryType() {
        super();
        // TODO Auto-generated constructor stub
    }
    public DiaryType(String type_name) {
        this.type_name=type_name;
    }


    public int getDiaryCount() {
        return diaryCount;
    }

    public void setDiaryCount(int diaryCount) {
        this.diaryCount = diaryCount;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
