package company.com.to_do_list.views.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import company.com.to_do_list.AlarmNotificationReceiver;
import company.com.to_do_list.R;
import company.com.to_do_list.database.ToDoListDatabase;

public class SettingsFragment extends Fragment {

    Switch  alarmSwitch,clearDataSwitch;
    SharedPreferences sharedPreferences;
    private AlarmManager alarmManager,alarmManager1;
    private PendingIntent pendingIntent,eveningAlarm;
    ToDoListDatabase toDoListDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoListDatabase= Room.databaseBuilder(getContext(), ToDoListDatabase.class,"Notes")
                .build();

        getActivity().setTitle(Html.fromHtml("<font color='#111111'>\t Settings</font>"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        alarmSwitch = view.findViewById(R.id.switch_Alarm);
        clearDataSwitch = view.findViewById(R.id.switch_ClearData);
        sharedPreferences = getActivity().getSharedPreferences("todo", Context.MODE_PRIVATE);
        onSwitchChange();

        return  view;
    }

    private  void onSwitchChange(){

        Boolean checkedValue = sharedPreferences.getBoolean("checked",false);
        alarmSwitch.setChecked(checkedValue);

        if(checkedValue) {
            sendNotifications();
        }else{

            cancelAlarms();
        }

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked){

//            if (dateCompareOne.equals(date) || dateCompareTwo.equals(date)) {
                    sendNotifications();
                    editor.putBoolean("checked", true);

                }else{
                    cancelAlarms();
                    editor.putBoolean("checked", false);
                }
                editor.commit();

            }
        });

        clearDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                    new DeleteDataTask(getContext()).execute();

                }

            }
        });

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void sendNotifications(){


        Log.d("note","Semd notifications is called");

         alarmManager = (AlarmManager)getContext(). getSystemService(Context.ALARM_SERVICE);
        alarmManager1 = (AlarmManager)getContext(). getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlarmNotificationReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        // Set the alarm to start at approximately 2:00 p.m.

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.setTimeZone(TimeZone.getDefault());


        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 15);
        calendar1.set(Calendar.MINUTE, 15);
        calendar1.set(Calendar.SECOND, 00);
        calendar1.setTimeZone(TimeZone.getDefault());

         eveningAlarm = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        // Start both alarms, set to repeat once every day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, eveningAlarm);
        //}
    }


    private  void cancelAlarms(){

        if(alarmManager!=null) {
            alarmManager.cancel(pendingIntent);
        }

        if(alarmManager1!=null) {
            alarmManager1.cancel(eveningAlarm);
        }

    }

    private class DeleteDataTask extends AsyncTask<Void,Void,String> {

        Context context;

        public DeleteDataTask(Context mContext){
            this.context = mContext;
        }

        @Override
        protected String doInBackground(Void... voids) {
            toDoListDatabase.getNotesDao().deleteData();
            return null;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            deleteCache(getContext());

        }
    }


}
