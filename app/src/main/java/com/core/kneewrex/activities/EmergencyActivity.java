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

public class EmergencyActivity extends BaseActivity {

    Button btnReset, btnResume, btnClose;
    TcpClient client;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        btnReset = findViewById(R.id.btnReset);
        btnResume = findViewById(R.id.btnResume);
        tvStatus = findViewById(R.id.tvStatus);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> {this.finishAffinity();});

        btnReset.setOnClickListener(v -> {
            startActivity(new Intent(EmergencyActivity.this, ResettingActivity.class));
            finish();
        });

        btnResume.setOnClickListener(v -> {
            Intent intent = new Intent(EmergencyActivity.this, StatusActivity.class);
            intent.putExtra("RESUME_ACTIVITY", true);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on parameters activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("i"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("i"), 500));
        }
        else{
            Toast.makeText(EmergencyActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.connected));
                tvStatus.setTextColor(ContextCompat.getColor(EmergencyActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.not_connected));
                tvStatus.setTextColor(ContextCompat.getColor(EmergencyActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("ok")){

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