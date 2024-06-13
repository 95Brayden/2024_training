package com.example.midemo.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.midemo.R;
import com.example.midemo.entity.Post;
import com.example.midemo.utils.AppDatabase;
import com.example.midemo.utils.DBOpenHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("Range")
public class PostActivity extends AppCompatActivity {

    private DBOpenHelper dbHelper;
    private AppDatabase roomDb;
    private TextView textView;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        dbHelper = new DBOpenHelper(this);
        roomDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "room-db").allowMainThreadQueries().build();
        textView = findViewById(R.id.textView);

        Button buttonA = findViewById(R.id.buttonA);
        buttonA.setOnClickListener(v -> fetchAndSaveDataSQLite());

        Button buttonB = findViewById(R.id.buttonB);
        buttonB.setOnClickListener(v -> displayDataSQLite());

        Button buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(v -> fetchAndSaveDataRoom());

        Button buttonD = findViewById(R.id.buttonD);
        buttonD.setOnClickListener(v -> displayDataRoom());
    }

    private void fetchAndSaveDataSQLite() {
        String url = "https://jsonplaceholder.typicode.com/posts/3";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(PostActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = response.body().string();
                    Post post = gson.fromJson(json, Post.class);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBOpenHelper.COLUMN_POST_ID, post.id);
                    values.put(DBOpenHelper.COLUMN_POST_USER_ID, post.userId);
                    values.put(DBOpenHelper.COLUMN_POST_TITLE, post.title);
                    values.put(DBOpenHelper.COLUMN_POST_BODY, post.body);

                    db.insertWithOnConflict(DBOpenHelper.TABLE_POST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();

                    runOnUiThread(() -> Toast.makeText(PostActivity.this, "Data Saved to SQLite", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void displayDataSQLite() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBOpenHelper.TABLE_POST,
                null, DBOpenHelper.COLUMN_POST_ID + "=?",
                new String[]{"3"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_POST_TITLE));
            String body = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_POST_BODY));
            textView.setText("SQLite Data\nTitle: " + title + "\n\nBody: " + body);
            cursor.close();
        } else {
            Toast.makeText(this, "No Data Found in SQLite", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void fetchAndSaveDataRoom() {
        String url = "https://jsonplaceholder.typicode.com/posts/3";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(PostActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = response.body().string();
                    Post post = gson.fromJson(json, Post.class);

                    roomDb.postDao().insert(post);

                    runOnUiThread(() -> Toast.makeText(PostActivity.this, "Data Saved to Room", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void displayDataRoom() {
        Post post = roomDb.postDao().getPostById(3);
        if (post != null) {
            textView.setText("Room Data\nTitle: " + post.title + "\n\nBody: " + post.body);
        } else {
            Toast.makeText(this, "No Data Found in Room", Toast.LENGTH_SHORT).show();
        }
    }
}
