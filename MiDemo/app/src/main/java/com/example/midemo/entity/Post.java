package com.example.midemo.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @PrimaryKey
    public int id;
    public int userId;
    public String title;
    public String body;
}