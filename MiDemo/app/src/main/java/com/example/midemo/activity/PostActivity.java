package com.example.midemo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.midemo.R;
import com.example.midemo.entity.Post;
import com.example.midemo.service.PostService;

@SuppressLint("Range")
public class PostActivity extends AppCompatActivity {

    private TextView textView;
    private PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postService = new PostService(this);
        textView = findViewById(R.id.textView);

        Button buttonA = findViewById(R.id.buttonA);
        buttonA.setOnClickListener(v -> postService.fetchAndSaveDataSQLite());

        Button buttonB = findViewById(R.id.buttonB);
        buttonB.setOnClickListener(v -> displayDataSQLite());

        Button buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(v -> postService.fetchAndSaveDataRoom());

        Button buttonD = findViewById(R.id.buttonD);
        buttonD.setOnClickListener(v -> displayDataRoom());
    }

    /**
     * 从SQLite数据库中读取数据并显示
     */
    private void displayDataSQLite() {
        Post post = postService.getPostFromSQLite(3);
        if (post != null) {
            textView.setText("SQLite Data\nTitle: " + post.title + "\n\nBody: " + post.body);
        } else {
            Toast.makeText(this, "No Data Found in SQLite", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从Room数据库中读取数据并显示
     */
    private void displayDataRoom() {
        Post post = postService.getPostFromRoom(4);
        if (post != null) {
            textView.setText("Room Data\nTitle: " + post.title + "\n\nBody: " + post.body);
        } else {
            Toast.makeText(this, "No Data Found in Room", Toast.LENGTH_SHORT).show();
        }
    }
}
