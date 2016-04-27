package com.charan.alarmmanager;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.charan.alarmmanager.R;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements OnClickListener {
    final static private long ONE_SECOND = 1000;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;

    int hr = 0;
    int min = 0;
    int sec = 0;
    int result = 1;

    EditText ethr;
    EditText etmin;
    EditText etsec;

    private Date startTime;
    private Date endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BroadCastReceiversetup();
        ethr = (EditText) findViewById(R.id.ethr);
        etmin = (EditText) findViewById(R.id.etmin);
        etsec = (EditText) findViewById(R.id.etsec);

        findViewById(R.id.the_button).setOnClickListener(this);
    }


    private void BroadCastReceiversetup() {
        br = new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent i) {
                Toast.makeText(c, "Alarm Raised!", Toast.LENGTH_LONG).show();
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c)
                                                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                                                        .setContentTitle("Time to open app")
                                                        .setContentText("Click here to open the app")
                                                        .setAutoCancel(true)
                                                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setColor(c.getResources().getColor(android.R.color.holo_blue_bright));

                Intent resultIntent = new Intent(c, MainActivity.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(c, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }
        };
        registerReceiver(br, new IntentFilter("com.charan.alarmmanager"));
        pi = PendingIntent.getBroadcast(this, 0, new Intent("com.charan.alarmmanager"),
                0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Alarm Set",Toast.LENGTH_LONG).show();
        String shr = ethr.getText().toString();
        String smin = etmin.getText().toString();
        String ssec = etsec.getText().toString();

        if(shr.equals(""))
            hr = 0;
        else {
            hr = Integer.parseInt(ethr.getText().toString());
            //hr=hr*60*60*1000;
        }

        if(smin.equals(""))
            min = 0;
        else {
            min = Integer.parseInt(etmin.getText().toString());
            //min = min*60*1000;
        }

        if(ssec.equals(""))
            sec = 0;
        else {
            sec = Integer.parseInt(etsec.getText().toString());
            //sec = sec * 1000;
        }
        //result = hr+min+sec;
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR, hr );
        calendar.set( Calendar.MINUTE, min );
        calendar.set(Calendar.SECOND,sec);
        this.startTime = calendar.getTime();

        am.set(AlarmManager.RTC_WAKEUP,startTime.getTime(),pi);
        //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ result , pi);
    }

    @Override
    protected void onDestroy() {
        am.cancel(pi);
        unregisterReceiver(br);
        super.onDestroy();
    }

}