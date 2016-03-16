package ar.com.cristianduarte.screendimensionsgrid;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

public class GridTestActivity extends AppCompatActivity {

    private SwitchCompat mGridSwitch;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private BroadcastReceiver mBr;

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 9876;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_test);

        mGridSwitch = (SwitchCompat) findViewById(R.id.grid_switch);
        mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startGridService();
                } else {
                    Intent intent = new Intent(GridTestActivity.this, GridService.class);
                    stopService(intent);
                }
            }
        };
        mBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mGridSwitch.setOnCheckedChangeListener(null);
                mGridSwitch.setChecked(false);
                mGridSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
            }
        };
        mGridSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    public void startGridService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startGridServiceM();
        } else {
            Intent intent = new Intent(GridTestActivity.this, GridService.class);
            startService(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startGridServiceM() {
        if (Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(GridTestActivity.this, GridService.class);
            startService(intent);
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void processOverlayPermissionResult() {
        if (Settings.canDrawOverlays(this)) {
            // You have permission
            startGridServiceM();
        } else {
            Toast.makeText(this, "Permission to draw not granted", Toast.LENGTH_LONG).show();
            mGridSwitch.setOnCheckedChangeListener(null);
            mGridSwitch.setChecked(false);
            mGridSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            processOverlayPermissionResult();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBr, new IntentFilter(GridService.BROADCAST_TURNED_OFF));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBr);
    }

}