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

public class CalibrationActivity extends BaseActivity {

    Button btnNext, btnReset, btnResetLoad, btnClose;
    TextView tvStatus, tvCalibration;
    TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        btnNext = findViewById(R.id.btnNext);
        btnReset = findViewById(R.id.btnReset);

        btnResetLoad = findViewById(R.id.btnResetLoadCell);
        tvStatus = findViewById(R.id.tvStatus);
        tvCalibration = findViewById(R.id.tvCalibration);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {this.finishAffinity();});
        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(CalibrationActivity.this, DisclaimerActivity.class));
            finish();
        });

        btnReset.setOnClickListener(v -> {
            startActivity(new Intent(CalibrationActivity.this, ResettingActivity.class));
            finish();
        });

//        if(client != null){
//        btnResetLoad.setOnClickListener(v -> client.sendMessage("r"));
//        }

    }

    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on resetting activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("c"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("c"), 500));
        } else{
            Toast.makeText(CalibrationActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }

        if(client != null){
            btnResetLoad.setOnClickListener(v -> client.sendMessage("r"));
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.connected));
                tvStatus.setTextColor(ContextCompat.getColor(CalibrationActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.not_connected));
                tvStatus.setTextColor(ContextCompat.getColor(CalibrationActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.startsWith("c")) {
            message = message.replace("c", "");
            tvCalibration.setText(message);
        } else if (message.contains("Calibration Complete")) {
            Toast.makeText(CalibrationActivity.this, message, Toast.LENGTH_SHORT).show();
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