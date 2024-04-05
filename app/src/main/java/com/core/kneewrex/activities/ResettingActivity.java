package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.tcp.TcpClient;

public class ResettingActivity extends BaseActivity {

    TcpClient client;
    TextView tvStatus, tvResettingSeconds;
    Button btnSkip,btnClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetting);

        tvStatus = findViewById(R.id.tvStatus);
        tvResettingSeconds = findViewById(R.id.tvResettingSeconds);
        btnSkip = findViewById(R.id.btnSkip);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> {this.finishAffinity();});

        //skip to start next activity
        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(ResettingActivity.this, CalibrationActivity.class));
            finish();
        });

    }


    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on resetting activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("b"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("b"), 500));
        }
        else{
            Toast.makeText(ResettingActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.connected));
                tvStatus.setTextColor(ContextCompat.getColor(ResettingActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.not_connected));
                tvStatus.setTextColor(ContextCompat.getColor(ResettingActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("s")) {
            message = message.replace("s", "");
            tvResettingSeconds.setText(message);
        } else if (message.contains("ok")) {
            startActivity(new Intent(ResettingActivity.this, CalibrationActivity.class));
            finish();
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