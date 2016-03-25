package com.project.others;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.project.application.MyApplication;
import com.project.data_process.SocketService;

import java.io.IOException;
import java.net.Socket;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    private String TAG = "MainActivity";
    TextView txtView;
    Context context;
    private ServiceConnection conn;

    public ConnectionChangeReceiver(Context context, TextView textView) {
        this.txtView = textView;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean flag = false;
        // 1. Check internet.
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            flag = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        // 2. do something based on the status of internet.
        if (flag) {
            //1. if working
            txtView.setVisibility(View.GONE);
            bindSocketService1();
        } else {
            // 2. if not working.
            txtView.setVisibility(View.VISIBLE);
            destroySocketService();
        }
    }

    /**
     * connect to server by socket based on TCP/IP protocol.
     */
    public void bindSocketService1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1. create socket.
                try {
                    // Watch out. The usage of method isconnected() and isclosed
                    if (null == MyApplication.socket || !MyApplication.socket.isConnected() || MyApplication.socket.isClosed()) {
                        MyApplication.socket = new Socket(MyApplication.ipAddressWIFI, MyApplication.port);
                    }
                    Log.i(TAG, "create application socket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 2. Bind service which will use the initialized socket.
                if (MyApplication.socket != null) {
                    bindSocketService();
                }
            }
        }).start();
    }


    private void bindSocketService() {
        Log.i(TAG, "start a service to connect Socket");
        Intent intent1 = new Intent(context, SocketService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyApplication.socketService = ((SocketService.MyBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                MyApplication.socketService = null;
            }
        };
        context.bindService(intent1, conn, Context.BIND_AUTO_CREATE);
    }

    private void destroySocketService() {
        Log.i(TAG, "stop a service to connect Socket");
        if (conn != null) {
            try {
                MyApplication.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            context.unbindService(conn);
        }
    }
}