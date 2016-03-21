package com.project.application;

import android.app.Application;

import com.project.data_process.SocketService;

import java.net.Socket;

/**
 * Created by zwl on 2015/9/9.
 */
public class MyApplication extends Application {

    private String TAG = "Application";
    /**
     * Web server's IP address.
     */
    public static String ipAddressWeb = "115.28.153.25";
    public static String ipAddressWIFI = "192.168.0.109";
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

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}