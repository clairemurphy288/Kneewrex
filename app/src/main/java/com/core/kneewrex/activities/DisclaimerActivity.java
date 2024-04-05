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

public class DisclaimerActivity extends BaseActivity {

    Button btnReset, btnSkip, btnClose;
    TextView tvStatus;
    TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        btnSkip = findViewById(R.id.btnSkip);
        btnReset = findViewById(R.id.btnReset);
        tvStatus = findViewById(R.id.tvStatus);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(v -> {this.finishAffinity();});

        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(DisclaimerActivity.this, ProtocolsActivity.class));
            finish();
        });

        btnReset.setOnClickListener(v -> {
            startActivity(new Intent(DisclaimerActivity.this, ResettingActivity.class));
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
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("d"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("d"), 500));
        }
        else{
            Toast.makeText(DisclaimerActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.connected));
                tvStatus.setTextColor(ContextCompat.getColor(DisclaimerActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                tvStatus.setText(getString(R.string.not_connected));
                tvStatus.setTextColor(ContextCompat.getColor(DisclaimerActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("ok")) {
            Toast.makeText(DisclaimerActivity.this, message.toUpperCase(), Toast.LENGTH_SHORT).show();
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