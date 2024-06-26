package com.example.midemo.entity;
/*
 * 表示收入或者支出具体类型的类
 * */
public class Type {
    int id;
    String typename;   //类型名称
    int imageId;    //未被选中图片id
    int simageId;    //被选中图片id
    int kind;     //收入-1  支出-0

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public int getImageId() {
        return imageId;
    }

    public int getSimageId() {
        return simageId;
    }

    public Type(int id, String typename, int imageId, int simageId, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.simageId = simageId;
        this.kind = kind;
    }
}
