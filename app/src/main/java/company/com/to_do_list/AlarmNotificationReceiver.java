package company.com.to_do_list;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import company.com.to_do_list.views.activities.BottomBarActivity;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day != 0) {
            if (!this.isAppOnForeground(context, context.getPackageName())) {

                Log.d("Cal",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));

                if (calendar.get(Calendar.HOUR_OF_DAY) == 17 || calendar.get(Calendar.HOUR_OF_DAY) == 10) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "office";
                        String description = "attendence";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("office1", name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system; you can't change the importance
                        // or other notification behaviors after this
                        NotificationManager notificationManager1 = context.getSystemService(NotificationManager.class);
                        notificationManager1.createNotificationChannel(channel);

                        Intent resultIntent = new Intent(context, BottomBarActivity.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

                        Notification.Builder notification = new Notification.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Notification")
                                .setAutoCancel(true)
                                .setChannelId("office1")
                                .setContentText("Please check your to do list...")
                                .setContentIntent(resultPendingIntent)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT < 16) {
                            notificationManager.notify(1, notification.getNotification());
                        } else {
                            notificationManager.notify(1, notification.build());
                        }


                    } else {
                        Intent resultIntent = new Intent(context, BottomBarActivity.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

                        Notification.Builder notification = new Notification.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Notification")
                                .setAutoCancel(true)
                                .setContentText("Please check your to do list..")
                                .setContentIntent(resultPendingIntent)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT < 16) {
                            notificationManager.notify(1, notification.getNotification());
                        } else {
                            notificationManager.notify(1, notification.build());
                        }
                    }
                }else{
                    Log.d("Receiver","Time is different");
                }

            }else{
                Log.d("Receiver","App is in foreground");
            }

        }else{

            Log.d("Receiver","Days is sunday");

        }

    }



    private boolean isAppOnForeground(Context context,String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }
}
