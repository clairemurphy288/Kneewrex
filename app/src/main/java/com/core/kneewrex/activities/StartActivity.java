package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.databinding.ActivityStartBinding;
import com.core.kneewrex.tcp.TcpClient;


public class StartActivity extends BaseActivity implements View.OnClickListener {

    private ActivityStartBinding binding;
    private TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStart.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnStart) {
            startActivity(new Intent(StartActivity.this, StatusActivity.class));
            finish();
        }
        if (v == binding.btnCancel) {
            startActivity(new Intent(StartActivity.this, ProtocolsActivity.class));
            finish();
        }
        if (v == binding.btnBack) {
            startActivity(new Intent(StartActivity.this, ParametersActivity.class));
            finish();
        }
        if(v == binding.btnClose) {
            this.finishAffinity();
        }
    }

    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on parameters activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("g"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("g"), 500));
        }
        else{
            Toast.makeText(StartActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(StartActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.not_connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(StartActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.startsWith("p")) {
            String preset = message.replace("p", "");
            binding.tvPresetType.setText("Preset " + preset);
        } else if (message.startsWith("t")) {
            String time = message.replace("t", "");
            binding.tvTreatmentTime.setText(time);
        } else if (message.startsWith("f")) {
            String force = message.replace("f", "");
            binding.tvForce.setText(force);
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