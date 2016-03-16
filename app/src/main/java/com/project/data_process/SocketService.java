package com.project.data_process;

import java.io.IOException;
import java.net.Socket;

import com.project.application.MyApplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 1. open a socket.
 * 2. start input and output thread
 * 3. wait for the message from server
 * 4. process message
 * 5. close all threads.
 * 6. close the socket.
 *
 * @author zwl
 */
public class SocketService extends Service {
    private Handler handler;
    private ClientInputThread inputThread;
    private ClientOutputThread outThread;
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != MyApplication.socket && MyApplication.socket.isConnected()) {
                        inputThread = new ClientInputThread(MyApplication.socket);
                        outThread = new ClientOutputThread(MyApplication.socket);
                        inputThread.start();
                        outThread.start();
                        Log.i("Service", "create if");
                    } else {
                        MyApplication.socket = new Socket(MyApplication.ipAddressWIFI, MyApplication.port);
                        Log.i("Service", "create else");
                    }
                } catch (IOException e) {

                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service on startcommand.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (inputThread != null) {
            inputThread.setFlag(false);
        }
        if (outThread != null) {
            outThread.setFlag(false);
        }
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public ClientInputThread getInputThread() {
        return inputThread;
    }

    public void setInputThread(ClientInputThread inputThread) {
        this.inputThread = inputThread;
    }

    public ClientOutputThread getOutThread() {
        return outThread;
    }

    public void setOutThread(ClientOutputThread outThread) {
        this.outThread = outThread;
    }
}
