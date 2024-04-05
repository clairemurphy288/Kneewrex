package com.core.kneewrex.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.databinding.ActivityProtocolsBinding;
import com.core.kneewrex.tcp.TcpClient;
import com.core.kneewrex.utils.StorageUtils;

public class ProtocolsActivity extends BaseActivity implements View.OnClickListener {

    private ActivityProtocolsBinding binding;
    private int selectedPreset = 1;
    private Button selectedButton;
    private TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProtocolsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnNext.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.btnPreset1.setOnClickListener(this);
        binding.btnPreset2.setOnClickListener(this);
        binding.btnPreset3.setOnClickListener(this);
        binding.btnPreset4.setOnClickListener(this);
        binding.btnPreset5.setOnClickListener(this);
        binding.btnPreset6.setOnClickListener(this);
        binding.btnPreset7.setOnClickListener(this);
        binding.btnPreset8.setOnClickListener(this);


        //initial selected button is preset 1
        selectedButton = binding.btnPreset1;
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnNext) {
            startActivity(new Intent(ProtocolsActivity.this, ParametersActivity.class));
            finish();
        } else if (v == binding.btnReset) {
            startActivity(new Intent(ProtocolsActivity.this, ResettingActivity.class));
            finish();
        } else if((v == binding.btnPreset1) && (client != null)) {
            client.sendMessage("e 1 >");
        } else if ((v == binding.btnPreset2) && (client != null)) {
            client.sendMessage("e 2 >");
        } else if ((v == binding.btnPreset3) && (client != null)) {
            client.sendMessage("e 3 >");
        } else if ((v == binding.btnPreset4) && (client != null)) {
            client.sendMessage("e 4 >");
        } else if ((v == binding.btnPreset5) && (client != null)) {
            client.sendMessage("e 5 >");
        } else if ((v == binding.btnPreset6) && (client != null)) {
            client.sendMessage("e 6 >");
        } else if ((v == binding.btnPreset7) && (client != null)) {
            client.sendMessage("e 7 >");
        } else if ((v == binding.btnPreset8) && (client != null)) {
            client.sendMessage("e 8 >");
        }
    }

    @Override
    void getClientInstance(TcpClient client) {
        this.client = client;
    }

    @Override
    void Connected(boolean status) {
        //send initial message that we're on protocols activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("e 1 >"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("e 1 >"), 500));
        }
        else{
            Toast.makeText(ProtocolsActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(ProtocolsActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.not_connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(ProtocolsActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        if (message.contains("1")) {
            binding.imgPreset.setImageResource(R.drawable.preset_1);
            selectedPreset = 1;
            selectedButton = binding.btnPreset1;
        } else if (message.contains("2")) {
            binding.imgPreset.setImageResource(R.drawable.preset_2);
            selectedPreset = 2;
            selectedButton = binding.btnPreset2;
        } else if (message.contains("3")) {
            binding.imgPreset.setImageResource(R.drawable.preset_3);
            selectedPreset = 3;
            selectedButton = binding.btnPreset3;
        } else if (message.contains("4")) {
            binding.imgPreset.setImageResource(R.drawable.preset_4);
            selectedPreset = 4;
            selectedButton = binding.btnPreset4;
        } else if (message.contains("5")) {
            binding.imgPreset.setImageResource(R.drawable.preset_5);
            selectedPreset = 5;
            selectedButton = binding.btnPreset5;
        } else if (message.contains("6")) {
            binding.imgPreset.setImageResource(R.drawable.preset_6);
            selectedPreset = 6;
            selectedButton = binding.btnPreset6;
        } else if (message.contains("7")) {
            binding.imgPreset.setImageResource(R.drawable.preset_7);
            selectedPreset = 7;
            selectedButton = binding.btnPreset7;
        } else if (message.contains("8")) {
            binding.imgPreset.setImageResource(R.drawable.preset_8);
            selectedPreset = 8;
            selectedButton = binding.btnPreset8;
        }
        selectedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        //save selected preset in shared preferences
        StorageUtils.saveDataInPreferences(ProtocolsActivity.this, "SELECTED_PRESET", selectedPreset);
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