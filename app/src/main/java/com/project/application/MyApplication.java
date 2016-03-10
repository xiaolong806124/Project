package com.project.application;

import java.net.Socket;

import android.app.Application;
import android.util.Log;

/**
 * Created by zwl on 2015/9/9.
 */
public class MyApplication extends Application {
    /**
     * Web server's IP address.
     */
    public static String ipAddressWeb = "115.28.153.25";// "192.168.0.105";//
    // ;"219.224.156.137";//
    public static String ipAddressTCP = "192.168.0.103";// "192.168.0.105";
    /**
     * TCP/IP Server's port used to communicate with clients.
     */
    public static int port = 9000;
    /**
     * Web server's base path.
     */
    public static String basePath = "http://" + ipAddressWeb + ":8080/Server";
    public static Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        /*
		 * if (isConnectedToNetwork()) { // check whether internet is connected.
		 * // if connected, create Socket to connect server. // if not, give a
		 * message to the user. creaeteSocket(); } else { Toast.makeText(this,
		 * "please check internet", Toast.LENGTH_SHORT).show(); }
		 */
    }

    /**
     * connect to server by socket based on TCP/IP protocol.
     */
    public void creaeteSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // application will be create without internet
                // connected after five times attempts to connect.
                for (int i = 0; i < 5; i++) {
                    try {
                        if (null == socket || !socket.isConnected()) {
                            socket = new Socket(ipAddressTCP, port);
                            Log.i("Service", "Application socket");
                        } else
                            break;
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }
}