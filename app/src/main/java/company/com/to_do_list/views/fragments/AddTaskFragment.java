package company.com.to_do_list.views.fragments;

import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.time.LocalDateTime;
import company.com.to_do_list.AddTaskViewModel;
import company.com.to_do_list.AddTaskViewModelFactory;
import company.com.to_do_list.R;
import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.database.ToDoListDatabase;
import company.com.to_do_list.views.utils.DatePickerFragment;
import company.com.to_do_list.views.utils.TimePickerFragment;

public class AddTaskFragment extends Fragment {

    private AddTaskViewModel mViewModel;
    private EditText startTime,endTime,editDate,editPlace,editNotes,editTitle;
    private ImageButton imgBtn1,imgBtn2,imgBtn;
    private Spinner workType;
    private Button addNotesButton;
    private  static ToDoListDatabase toDoListDatabase;
    private String clickEvent="";

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.add_task_fragment, container, false);
        imgBtn = view.findViewById(R.id.img_btn);
        imgBtn1 = view.findViewById(R.id.img_btn1);
        imgBtn2 = view.findViewById(R.id.img_btn2);
        startTime = view.findViewById(R.id.editTime);
        endTime   = view.findViewById(R.id.editEndTime);
        workType = view.findViewById(R.id.editWorkType);
        editDate = view.findViewById(R.id.editDate);
        editPlace = view.findViewById(R.id.editPlace);
        editNotes = view.findViewById(R.id.editNotes);
        editTitle = view.findViewById(R.id.editTitle);
        addNotesButton = view.findViewById(R.id.addNotesButton);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(Html.fromHtml("<font color='#111111'>\t Add Notes</font>"));
        init();
        buttonClicks();

    }

    private void init(){
        AddTaskViewModelFactory factory= new AddTaskViewModelFactory(getContext(),"All");
        mViewModel = new ViewModelProvider(this,factory).get(AddTaskViewModel.class);

        toDoListDatabase= Room.databaseBuilder(getContext(), ToDoListDatabase.class,"Notes")
                .build();

        String[] workTypeList ={"Personal","Work","Shopping","Movies"};

        ArrayAdapter adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,workTypeList);
        workType.setAdapter(adapter);

        GradientDrawable shape2 =  new GradientDrawable();
        shape2.setCornerRadius(40);
        shape2.setColor(getResources().getColor(R.color.faint_blue));
        addNotesButton.setBackground(shape2);
        LocalDateTime now  = LocalDateTime.now();
        if(now.getMonthValue()<10) {
            editDate.setText(String.valueOf(now.getDayOfMonth()) + "/0" + String.valueOf(now.getMonthValue()) + "/" + String.valueOf(now.getYear()));
        }else{
            editDate.setText(String.valueOf(now.getDayOfMonth()) + "/" + String.valueOf(now.getMonthValue()) + "/" + String.valueOf(now.getYear()));
        }
        if(getArguments()!= null) {
            editTitle.setText(getArguments().get("title").toString());
            editPlace.setText(getArguments().get("place").toString());
            editDate.setText(getArguments().get("date").toString());
            startTime.setText(getArguments().get("starttime").toString());
            endTime.setText(getArguments().get("endtime").toString());
            editNotes.setText(getArguments().get("notes").toString());
            //wo.setText(getArguments().get("notes").toString());
            if(getArguments().get("type").equals("Personal")) {
                workType.setSelection(0);
            }else if(getArguments().get("type").equals("Work")){

                workType.setSelection(1);
            }else if(getArguments().get("type").equals("Shopping")){

                workType.setSelection(2);
            }else{
                workType.setSelection(3);
            }
        }

    }

    private void buttonClicks(){

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(),"datepicker");


            }
        });

        imgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent = "start";
                DialogFragment picker = new TimePickerFragment();
                picker.show(getFragmentManager(),"timepicker1");
            }
        });

        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent = "end";
                DialogFragment picker = new TimePickerFragment();
                picker.show(getFragmentManager(),"timepicker2");

            }
        });


        addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewModel.addNotes(new NotesDetails(
                        editTitle.getText().toString(),
                        editPlace.getText().toString(),
                        editDate.getText().toString(),
                        startTime.getText().toString(),
                        endTime.getText().toString(),
                        editNotes.getText().toString(),
                        String.valueOf(workType.getSelectedItem()),false));

                mViewModel.checkAdded().observe(getActivity(), new Observer<Long>() {
                    @Override
                    public void onChanged(Long aLong) {
                        if(aLong!=0){
                            showDialog();
                        }

                    }
                });

            }
        });


    }


    private void showDialog(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        Button buttonCancel = dialogView.findViewById(R.id.button_Cancel);
        Button buttonOk = dialogView.findViewById(R.id.button_Ok);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(dialogView);

        if(alertDialog!= null && alertDialog.isShowing()){
            alertDialog.dismiss();

        }else{

            alertDialog.show();
        }

        alertDialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //Currently commenting this code
//                HomeFragment homeFragment = new HomeFragment();
//                getActivity().getSupportFragmentManager()
//
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, homeFragment)
//                        .commit();

            }
        });

    }

    public void setStartTime(int hour, int min) {

        String hrValue = "";
        String minValue = "";

        if(hour>0 && hour<10){
           hrValue = "0"+String.valueOf(hour);
        }else{
            hrValue = String.valueOf(hour);
        }

        if(min>0 && min<10){
            minValue = "0"+String.valueOf(min);
        }else{
            minValue = String.valueOf(min);
        }

        if(clickEvent.equals("start")) {

           startTime.setText(hrValue + ":" + minValue);

        }else{

            endTime.setText(hrValue + ":" + minValue);
        }

    }

    public void setDate(int day,int month,int year){

        if(month<10) {
            editDate.setText(String.valueOf(day) + "/0" + String.valueOf(month + 1) + "/" + String.valueOf(year));
        }else{
            editDate.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
        }

    }

}
