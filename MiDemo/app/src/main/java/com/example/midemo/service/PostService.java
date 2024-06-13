package com.example.midemo.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.room.Room;

import com.example.midemo.entity.Post;
import com.example.midemo.utils.AppDatabase;
import com.example.midemo.utils.DataBaseManager;
import com.example.midemo.utils.NetworkManager;
import com.google.gson.Gson;

import java.io.IOException;

@SuppressLint("Range")
public class PostService {

    private final Context context;
    private final DataBaseManager dbHelper;
    private final AppDatabase roomDb;
    private final NetworkManager networkManager;

    public PostService(Context context) {
        this.context = context;
        this.dbHelper = new DataBaseManager(context);
        this.roomDb = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "room.db")
                          .allowMainThreadQueries()
                          .build();
        this.networkManager = new NetworkManager(context);
    }

    /**
     * 从URL获取数据并保存到SQLite数据库
     */
    public void fetchAndSaveDataSQLite() {
        String url = "https://jsonplaceholder.typicode.com/posts/3";
        networkManager.makeRequest(url, new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Post post = gson.fromJson(response, Post.class);
                savePostToSQLite(post);
            }

            @Override
            public void onFailure(IOException e) {
                showToast("Failed to fetch data for SQLite");
            }
        });
    }

    /**
     * 将数据保存到SQLite数据库
     */
    private void savePostToSQLite(Post post) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseManager.COLUMN_POST_ID, post.id);
        values.put(DataBaseManager.COLUMN_POST_USER_ID, post.userId);
        values.put(DataBaseManager.COLUMN_POST_TITLE, post.title);
        values.put(DataBaseManager.COLUMN_POST_BODY, post.body);

        db.insertWithOnConflict(DataBaseManager.TABLE_POST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        showToast("Data Saved to SQLite");
    }

    /**
     * 从SQLite数据库中读取数据
     */
    public Post getPostFromSQLite(int postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DataBaseManager.TABLE_POST,
                null, DataBaseManager.COLUMN_POST_ID + "=?",
                new String[]{String.valueOf(postId)}, null, null, null);

        Post post = null;
        if (cursor != null && cursor.moveToFirst()) {
            post = new Post();
            post.id = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_POST_ID));
            post.userId = cursor.getInt(cursor.getColumnIndex(DataBaseManager.COLUMN_POST_USER_ID));
            post.title = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_POST_TITLE));
            post.body = cursor.getString(cursor.getColumnIndex(DataBaseManager.COLUMN_POST_BODY));
            cursor.close();
        }

        db.close();
        return post;
    }

    /**
     * 从URL获取数据并保存到Room数据库
     */
    public void fetchAndSaveDataRoom() {
        String url = "https://jsonplaceholder.typicode.com/posts/4";
        networkManager.makeRequest(url, new NetworkManager.NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                Post post = gson.fromJson(response, Post.class);
                savePostToRoom(post);
            }

            @Override
            public void onFailure(IOException e) {
                showToast("Failed to fetch data for Room");
            }
        });
    }

    /**
     * 将数据保存到Room数据库
     */
    private void savePostToRoom(Post post) {
        roomDb.postDao().insert(post);
        showToast("Data Saved to Room");
    }

    /**
     * 从Room数据库中读取数据
     */
    public Post getPostFromRoom(int postId) {
        return roomDb.postDao().getPostById(postId);
    }

    /**
     * 显示Toast消息
     */
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
