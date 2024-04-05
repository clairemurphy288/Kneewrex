package com.core.kneewrex.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.core.kneewrex.tcp.TcpClient;
import com.core.kneewrex.tcp.TcpListener;

public abstract class BaseActivity extends AppCompatActivity {

    TcpClient client;

    abstract void getClientInstance(TcpClient client);

    abstract void Connected(boolean status);

    abstract void ListenConnection(boolean status);

    abstract void messageReceived(String message);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ConnectClient().execute();


    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    public class ConnectClient extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {
            //we create a TCP Client object
            client = new TcpClient(new TcpListener() {
                @Override
                public void connected(boolean status) {
                    getClientInstance(client);
                    Connected(status);
                }

                @Override
                public void listenConnection(boolean status) {
                    ListenConnection(status);
                }

                @Override
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            if(client != null){
                client.run();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String response = values[0];
            messageReceived(response);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

