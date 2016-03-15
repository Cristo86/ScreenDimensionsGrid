package ar.com.cristianduarte.screendimensionsgrid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

public class GridTestActivity extends AppCompatActivity {

    private SwitchCompat mGridSwitch;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private BroadcastReceiver mBr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_test);

        mGridSwitch = (SwitchCompat) findViewById(R.id.grid_switch);
        mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(GridTestActivity.this, GridService.class);
                    startService(intent);
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