package com.project.application;

import android.app.Activity;
import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.project.data_process.SocketService;

import java.net.Socket;

/**
 * Created by zwl on 2015/9/9.
 */
public class MyApplication extends Application {
    /**
     * Web server's IP address.
     */
    public static String ipAddressWeb = "115.28.153.25";// "192.168.0.105";

    public static String ipAddressWIFI = "192.168.0.104";
    /**
     * TCP/IP Server's port used to communicate with clients.
     */
    public static int port = 9000;
    /**
     * Web server's base path.
     */
    public static String basePath = "http://" + ipAddressWeb + ":8080/Server";
    public static Socket socket;
    public static SocketService socketService;
    private ConnectivityManager conman;

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        // check whether internet is connected.
        // if connected, create Socket to connect server.
        // if not, give a message to the user.
        if (isConnectedToNetwork()) {
            createSocket();
        } else {
            Toast.makeText(this, "please check internet", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * connect to server by socket based on TCP/IP protocol.
     */
    public void createSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // application will be create without internet
                // connected after five times attempts to connect.
                for (int i = 0; i < 5; i++) {
                    try {
                        if (null == socket || !socket.isConnected()) {
                            socket = new Socket(ipAddressWIFI, port);
                            Log.i("Service", "Application socket");
                        } else
                            break;
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    /**
     * check whether net is working.
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager conman = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conman.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }
}