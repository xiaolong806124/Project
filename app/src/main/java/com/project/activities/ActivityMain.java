package com.project.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.telecom.ConnectionService;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.project.R;
import com.project.data_process.SocketService;
import com.project.others.Pager;

@SuppressLint("InflateParams")
public class ActivityMain extends FragmentActivity {

    private String TAG = "ActivityMain";
    private Intent service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // start this activity without opening the keyboard.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // init pagers in main activity.
        new Pager(ActivityMain.this).setPager();

        // Test. Bind service
        service = new Intent(ActivityMain.this, SocketService.class);
        bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyService();
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

    private void bindService() {
        Log.i(TAG, "start a service to connect Socket");
        bindService(service, conn, Context.BIND_AUTO_CREATE);
    }

    private void destroyService(){
        Log.i(TAG, "stop a service to connect Socket");
        stopService(service);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
