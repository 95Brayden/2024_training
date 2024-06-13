package com.example.midemo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.midemo.entity.Post;

// PostDao.java
@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("SELECT * FROM Post WHERE id = :id")
    Post getPostById(int id);
}