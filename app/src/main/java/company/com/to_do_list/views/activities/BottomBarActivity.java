package company.com.to_do_list.views.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;
import company.com.to_do_list.R;
import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.onFragmentInteractionListener;
import company.com.to_do_list.views.fragments.AddTaskFragment;
import company.com.to_do_list.views.fragments.HomeFragment;
import company.com.to_do_list.views.fragments.SettingsFragment;


public  class BottomBarActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener, onFragmentInteractionListener {
    BottomNavigationView navView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_dashboard:

                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment = new AddTaskFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        navView = findViewById(R.id.nav_view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setIcon(getDrawable(R.drawable.ic_group));
        }

        Fragment fragment = new HomeFragment();
        loadFragment(fragment);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        AddTaskFragment fragment =  (AddTaskFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.setStartTime(hourOfDay,minute);
        Log.d("Fragment","check");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        AddTaskFragment fragment =  (AddTaskFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.setDate(dayOfMonth,month,year);

    }


    @Override
    public void onListFragmentInteraction(NotesDetails item) {

    }

    @Override
    public void onTaskCompleted(NotesDetails item) {

    }
}
