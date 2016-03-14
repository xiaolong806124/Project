package com.project.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.project.R;
import com.project.application.MyApplication;
import com.project.data_process.SocketService;
import com.project.others.Pager;

import java.util.Timer;

@SuppressLint("InflateParams")
public class ActivityMain extends FragmentActivity {

    private String TAG = "ActivityMain";

    private ServiceConnection conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // start this activity without opening the keyboard.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // init pagers in main activity.
        new Pager(ActivityMain.this).setPager();
        // Test. Bind service
        bindSocketService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect the service.
        if (conn != null)
            destroySocketService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.itemSwitchDevice:
                intent = new Intent(ActivityMain.this, ActivityDeviceSwitch.class);
                ActivityMain.this.startActivity(intent);
                break;
            case R.id.itemWatchDevice:
                intent = new Intent(ActivityMain.this, ActivityDeviveShow.class);
                ActivityMain.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindSocketService() {
        Log.i(TAG, "start a service to connect Socket");

        Intent intent1 = new Intent(ActivityMain.this, SocketService.class);

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

        bindService(intent1, conn, Context.BIND_AUTO_CREATE);
    }

    private void destroySocketService() {
        Log.i(TAG, "stop a service to connect Socket");
        if (conn != null)
            unbindService(conn);
    }
}
