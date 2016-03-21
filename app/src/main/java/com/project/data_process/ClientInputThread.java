package com.project.data_process;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import android.os.Handler;

public class ClientInputThread extends Thread {
    private Socket socket;
    private Handler handler;
    private boolean flag = true;

    public ClientInputThread() {
    }

    public ClientInputThread(Socket socket) {
        this.socket = socket;
    }

    public ClientInputThread(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            // socket could continue to receiving message.
            while (!socket.isClosed() && socket.isConnected()) {
                byte[] info = new byte[1024 * 4];
                // If receive the FINISH command, we will close the socket.
                if (Constant.Integer2Constant(info[0]) == Constant.FINISH) {
                    socket.close();
                    break;
                }
                // receive message.
                int len = in.read(info);
                if (len > 0) {
                    InfoProcessor.processInfo(handler, info, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
