package ar.com.cristianduarte.screendimensionsgrid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android. view.WindowManager;

public class GridService extends Service {

    private WindowManager windowManager;
    public static final String BROADCAST_TURNED_OFF = "TurnedOff";
    public static final String STOP_EXTRA = "STOP";
    private View v;

    private static final int NOTIFICATION_ID = 123;

    public GridService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void grid() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //v = new View(this);
        //v.setBackgroundColor(Color.BLUE);
        v = new GridDimView(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //200,
                //200,
                //WindowManager.LayoutParams.TYPE_PHONE,
                //WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,/* |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,*/
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(v, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        if (intent.getBooleanExtra(STOP_EXTRA, false)) {

            Intent tellUi = new Intent();
            tellUi.setAction(BROADCAST_TURNED_OFF);
            LocalBroadcastManager.getInstance(this).sendBroadcast(tellUi);

            stopSelf();
        } else {
            grid();
            showNotification();
        }
        return ret;
    }



    private void showNotification() {
        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_grid_notification)
                        .setContentTitle("On Screen Grid")
                        .setContentText("Grid is running")
                .setOngoing(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, GridTestActivity.class);

        Intent stopAction = new Intent(this, GridService.class);
        stopAction.putExtra(STOP_EXTRA, true);
        // http://stackoverflow.com/questions/2882459/getextra-from-intent-launched-from-a-pendingintent#2882594
        stopAction.setAction("" + Math.random());
        PendingIntent pi = PendingIntent.getService(this, 123, stopAction, 0);
        mBuilder.addAction(R.drawable.ic_stop, "Stop Grid", pi);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(GridTestActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (v != null) windowManager.removeView(v);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
