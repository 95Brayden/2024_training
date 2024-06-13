package com.example.midemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager {

    private final OkHttpClient client;
    private final Context context;

    public NetworkManager(Context context) {
        this.client = new OkHttpClient();
        this.context = context;
    }

    public void makeRequest(String url, NetworkCallback callback) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "Request Failed", Toast.LENGTH_SHORT).show();
                    callback.onFailure(e);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body() != null ? response.body().string() : null;
                    runOnUiThread(() -> callback.onSuccess(json));
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Request Failed", Toast.LENGTH_SHORT).show();
                        callback.onFailure(new IOException("Unexpected code " + response));
                    });
                }
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).runOnUiThread(runnable);
        }
    }

    public interface NetworkCallback {
        void onSuccess(String response);
        void onFailure(IOException e);
    }
}
