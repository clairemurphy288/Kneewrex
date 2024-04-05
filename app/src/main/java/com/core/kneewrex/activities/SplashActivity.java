package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.tcp.TcpClient;

public class SplashActivity extends BaseActivity {

    private TextView tvStatus;
    private TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvStatus = findViewById(R.id.tvStatus);
        messageReceived("ok");
    }


    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on splash activity
        //runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("a"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("a"), 500));
        }
        else{
            Toast.makeText(SplashActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.connected));
                tvStatus.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.not_connected));
                tvStatus.setTextColor(ContextCompat.getColor(SplashActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("ok")) {
            new Handler(Looper.myLooper()).postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, Angle_Calibration.class));
                finish();
            }, 3000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //client.stopClient();
        if(client != null) {
            client.stopClient();
        }
    }
}