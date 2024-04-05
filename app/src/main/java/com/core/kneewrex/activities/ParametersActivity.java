package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.databinding.ActivityParametersBinding;
import com.core.kneewrex.tcp.TcpClient;
import com.core.kneewrex.utils.StorageUtils;


public class ParametersActivity extends BaseActivity implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private ActivityParametersBinding binding;
    private TcpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParametersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnConfirm.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        binding.btnClose.setOnClickListener(this);

        binding.seekBarTime.setOnSeekBarChangeListener(this);
        binding.seekBarForce.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (binding.seekBarTime == seekBar) {
            progress = progress + 2;
            binding.tvTime.setText(String.valueOf(progress));
        } else if (binding.seekBarForce == seekBar) {
            progress = progress + 5;
            binding.tvForce.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnConfirm) {
            String minutes = binding.tvTime.getText().toString();
            String force = binding.tvForce.getText().toString();
            //send data to device
            if(client != null){
                client.sendMessage("f " + minutes + ", " + force + " >");
            }

            //save mints and force in shared preferences
            StorageUtils.saveDataInPreferences(ParametersActivity.this, "SELECTED_TIME", minutes);
            StorageUtils.saveDataInPreferences(ParametersActivity.this, "SELECTED_FORCE", force);
        } else if (v == binding.btnBack) {
            startActivity(new Intent(ParametersActivity.this, ProtocolsActivity.class));
            finish();
        } else if (v == binding.btnCancel) {
            //reset force
            binding.seekBarForce.setProgress(0);
            binding.tvForce.setText(String.valueOf(5));
            //reset time
            binding.seekBarTime.setProgress(0);
            binding.tvTime.setText(String.valueOf(2));
        }
        else if(v == binding.btnClose )
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
        //send initial message that we're on parameters activity
//        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("f 0, 0 >"), 500));
        if(client != null){
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("f 0, 0 >"), 500));
        }
        else{
            Toast.makeText(ParametersActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
        if (status) {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(ParametersActivity.this, R.color.green));
            });

        } else {
            runOnUiThread(() -> {
                binding.tvStatus.setText(getString(R.string.not_connected));
                binding.tvStatus.setTextColor(ContextCompat.getColor(ParametersActivity.this, android.R.color.holo_red_dark));
            });
        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("ok")) {
            startActivity(new Intent(ParametersActivity.this, StartActivity.class));
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