package com.core.kneewrex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.core.kneewrex.R;
import com.core.kneewrex.tcp.TcpClient;

public class Angle_Calibration extends BaseActivity {

    TcpClient client;
    TextView tvStatus, tvResettingSeconds;
    Button btnSkip,btnClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyro_calibration);

        Button btnCalibrate = findViewById(R.id.g_btn_calibration);

        if(client !=null) {
            btnCalibrate.setOnClickListener(v -> {
                client.sendMessage("x");

            });

        }


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
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> client.sendMessage("h"), 500));
        }
        else{
//            Toast.makeText(ResettingActivity.this, "Device not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void ListenConnection(boolean status) {
//        if (status) {
//            runOnUiThread(() -> {
//                tvStatus.setText(getString(R.string.connected));
//                tvStatus.setTextColor(ContextCompat.getColor(ResettingActivity.this, R.color.green));
//            });
//
//        } else {
//            runOnUiThread(() -> {
//                tvStatus.setText(getString(R.string.not_connected));
//                tvStatus.setTextColor(ContextCompat.getColor(ResettingActivity.this, android.R.color.holo_red_dark));
//            });
//        }
    }

    @Override
    void messageReceived(String message) {
        if (message.contains("s")) {
//            message = message.replace("s", "");
//            tvResettingSeconds.setText(message);
        } else if (message.contains("ok")) {
            Log.e("Calibration Code", "CLient received the message from the app");
//            startActivity(new Intent(ResettingActivity.this, CalibrationActivity.class));
//            finish();


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