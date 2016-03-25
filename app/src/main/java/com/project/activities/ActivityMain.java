package com.project.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.project.R;
import com.project.others.ConnectionChangeReceiver;
import com.project.others.Pager;

public class ActivityMain extends FragmentActivity {

    private String TAG = "ActivityMain";
    ConnectionChangeReceiver myReceiver;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // 1.start this activity without opening the keyboard.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 2.init pagers in main activity.
        new Pager(ActivityMain.this).setPager();

        textView = (TextView) findViewById(R.id.id_txtInternet);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                //that is android 3.0
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(Settings.ACTION_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.Settings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                ActivityMain.this.startActivity(intent);
            }
        });
        // 3. register a broadreceiver.
        registerMyReceiver();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private void registerMyReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (textView != null) {
            myReceiver = new ConnectionChangeReceiver(this, textView);
        }
        this.registerReceiver(myReceiver, filter);
    }
}
