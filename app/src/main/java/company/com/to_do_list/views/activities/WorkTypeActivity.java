package company.com.to_do_list.views.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import company.com.to_do_list.AddTaskViewModel;
import company.com.to_do_list.AddTaskViewModelFactory;
import company.com.to_do_list.onFragmentInteractionListener;
import company.com.to_do_list.views.DailyTaskAdapter;
import company.com.to_do_list.R;
import company.com.to_do_list.database.NotesDetails;

public class WorkTypeActivity extends AppCompatActivity implements onFragmentInteractionListener {

    private RecyclerView recyclerView;

    private String workType;

    private TextView taskMessage;

    private AddTaskViewModel addTaskViewModel;

    private onFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_type);

        init();
        setActionBar();
        getData();

    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        taskMessage = findViewById(R.id.tasksMessage);
        workType = getIntent().getStringExtra("type");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mListener = this;
    }

    private void setActionBar(){

        getSupportActionBar().setTitle("");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#111111'>"+ "\t\t"+workType +"</font>"));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getData(){

        AddTaskViewModelFactory factory= new AddTaskViewModelFactory(this,workType);
        addTaskViewModel = new ViewModelProvider(this,factory).get(AddTaskViewModel.class);

        addTaskViewModel.getNotesByType().observe(this, new Observer<List<NotesDetails>>() {
            @Override
            public void onChanged(List<NotesDetails> notesDetails) {
                if(notesDetails.size()==0) {
                    recyclerView.setVisibility(View.GONE);
                    taskMessage.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    taskMessage.setVisibility(View.GONE);
                    DailyTaskAdapter adapter = new DailyTaskAdapter(WorkTypeActivity.this, notesDetails, mListener);
                    recyclerView.setAdapter(adapter);
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onListFragmentInteraction(NotesDetails item) {

    }

    @Override
    public void onTaskCompleted(NotesDetails item) {
       addTaskViewModel.editNotes(item);
    }
}
