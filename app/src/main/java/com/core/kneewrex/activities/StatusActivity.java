package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.databinding.ActivityStatusBinding;
import com.core.kneewrex.tcp.TcpClient;

public class StatusActivity extends BaseActivity implements View.OnClickListener {

    ActivityStatusBinding binding;
    private TcpClient client;
    boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isResume = extras.getBoolean("RESUME_ACTIVITY");
        }

        binding.btnReset.setOnClickListener(this);
        binding.btnPause.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == binding.btnPause) {
            if (binding.btnPause.getText().equals("Pause")) {
                binding.btnPause.setText(getString(R.string.resume));
                binding.btnPause.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_outline_button));
                binding.btnPause.setTextColor(getResources().getColor(R.color.blue));
            } else if (binding.btnPause.getText().equals("Resume")) {
                binding.btnPause.setText(getString(R.string.pause));
                binding.btnPause.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_button));
                binding.btnPause.setTextColor(getResources().getColor(R.color.white));
            }
            if(client != null){
                client.sendMessage("p");
            }
        }
        if (v == binding.btnReset) {
            startActivity(new Intent(StatusActivity.this, ResettingActivity.class));
        }
        if( v == binding.btnClose)
        {
            this.finishAffinity();
        }
    }

    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on status activity
//        if (isResume)
//            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("p"), 500));
//        else
//            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("h"), 500));

        if(client != null){
            if (isResume)
                runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("p"), 500));
            else
                runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("h"), 500));
        }
        else{
            Toast.makeText(StatusActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(StatusActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.not_connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(StatusActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.startsWith("p")) {
            String preset = message.replace("p", "").trim();
            binding.tvPresetType.setText("Preset " + preset);
        } else if (message.startsWith("t")) {
            String time = message.replace("t", "").trim();
            binding.tvTreatmentTime.setText(time);
        } else if (message.startsWith("f")) {
            String force = message.replace("f", "").trim();
            binding.tvForce.setText(force);
        } else if (message.startsWith("status")) {
            String status = message.replace("status", "").trim();
            binding.tvDeviceStatus.setText(status);
        } else if (message.startsWith("selectedForce")) {
            String selectedForce = message.replace("selectedForce", "").trim();
            binding.progressSelectedForce.setProgress(Integer.parseInt(selectedForce));
        } else if (message.startsWith("commandForce")) {
            String commandForce = message.replace("commandForce", "").trim();
            binding.progressCommandForce.setProgress(Integer.parseInt(commandForce));
        } else if (message.startsWith("currentForce")) {
            String currentForce = message.replace("currentForce", "").trim();
            binding.progressCurrentForce.setProgress(Integer.parseInt(currentForce));
        } else if (message.startsWith("minutes")) {
            String minutes = message.replace("minutes", "").trim();
            binding.progressTimeBar.setProgress(Integer.parseInt(minutes));
        } else if (message.startsWith("seconds")) {
            String seconds = message.replace("seconds", "").trim();
            binding.progressCountDown.setProgress(Integer.parseInt(seconds));
        } else if (message.contains("stop")) {
            startActivity(new Intent(StatusActivity.this, EmergencyActivity.class));
            finish();
        } else if (message.contains("complete")) {
            startActivity(new Intent(StatusActivity.this, ResettingActivity.class));
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