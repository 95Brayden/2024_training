package com.example.midemo.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.midemo.dao.PostDao;
import com.example.midemo.entity.Post;

// AppDatabase.java
@Database(entities = {Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDao postDao();
}