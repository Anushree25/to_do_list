package company.com.to_do_list.views.fragments;

import android.app.AlertDialog;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import company.com.to_do_list.AddTaskViewModel;
import company.com.to_do_list.AddTaskViewModelFactory;
import company.com.to_do_list.onFragmentInteractionListener;
import company.com.to_do_list.views.DailyTaskAdapter;
import company.com.to_do_list.R;
import company.com.to_do_list.views.activities.WorkTypeActivity;
import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.database.ToDoListDatabase;

import company.com.to_do_list.views.utils.*;

public class HomeFragment extends Fragment implements onFragmentInteractionListener {

    private DailyTaskAdapter adapter;

    private static RecyclerView to_do_list;

    private static TextView message;

    private Button button1,button2,button3,button4;

    private onFragmentInteractionListener mListener;

    private Paint p = new Paint();
    AddTaskViewModel addTaskViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_fragment, container, false);
       to_do_list = view.findViewById(R.id.to_do_lists);
       button1 = view.findViewById(R.id.button1);
       button2 = view.findViewById(R.id.button2);
       button3 = view.findViewById(R.id.button3);
       button4 = view.findViewById(R.id.button4);
       message = view.findViewById(R.id.notesMessage);

      return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(Html.fromHtml("<font color='#111111'>\t Home</font>"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        to_do_list.setLayoutManager(linearLayoutManager);

        AddTaskViewModelFactory factory= new AddTaskViewModelFactory(getContext(),"All");
        addTaskViewModel = new ViewModelProvider(this,factory).get(AddTaskViewModel.class);
        setButtonsBackground();
        ButtonClicks();
        getNotes();

    }

    private void getNotes(){
        addTaskViewModel.getAllNotes().observe(this, new Observer<List<NotesDetails>>() {
            @Override
            public void onChanged(List<NotesDetails> notesDetails) {

                if(notesDetails.size() == 0){
                    message.setVisibility(View.VISIBLE);
                    to_do_list.setVisibility(View.GONE);
                }else{
                    message.setVisibility(View.GONE);
                    to_do_list.setVisibility(View.VISIBLE);
                }
                adapter = new DailyTaskAdapter(getContext(), notesDetails, mListener);
                adapter.notifyDataSetChanged();
                to_do_list.setAdapter(adapter);

                enableSwipe();
            }
        });

    }

    private void ButtonClicks(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , WorkTypeActivity.class);
                intent.putExtra("type","Personal");
                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , WorkTypeActivity.class);
                intent.putExtra("type","Work");
                startActivity(intent);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , WorkTypeActivity.class);
                intent.putExtra("type","Shopping");
                startActivity(intent);


            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext() , WorkTypeActivity.class);
                intent.putExtra("type","Movies");
                startActivity(intent);

            }
        });

    }

    private void setButtonsBackground(){

        GradientDrawable shape1 =  new GradientDrawable();
        shape1.setCornerRadius(20);
        shape1.setColor(getResources().getColor(R.color.dark_blue));
        button1.setBackground(shape1);


        GradientDrawable shape2 =  new GradientDrawable();
        shape2.setCornerRadius(20);
        shape2.setColor(getResources().getColor(R.color.dark_green));
        button2.setBackground(shape2);


        GradientDrawable shape3 =  new GradientDrawable();
        shape3.setCornerRadius(20);
        shape3.setColor(getResources().getColor(R.color.dark_pink));
        button3.setBackground(shape3);

        GradientDrawable shape4 =  new GradientDrawable();
        shape4.setCornerRadius(20);
        shape4.setColor(getResources().getColor(R.color.navy_blue));
        button4.setBackground(shape4);

    }

    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    showDialog(position, "Do you want to delete?");

                } else {

                    showDialog(position, "Do you want to edit?");

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft() / 4, (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_input_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(to_do_list);


    }


    public void showDialog(final int position, final String message) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        // add a button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (message.equals("Do you want to delete?")) {

                    addTaskViewModel.deleteNotes(adapter.getItem(position));
                    adapter.removeItem(position);
                    adapter.notifyItemRemoved(position);
                   // adapter.deleteTask(position);

                } else {
                    adapter.editEvent(position);

                }
                Log.d("Dialog", "");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                adapter.notifyDataSetChanged();
                Log.d("Dialog", "");
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public void onListFragmentInteraction(NotesDetails item) {

    }

    @Override
    public void onTaskCompleted(NotesDetails item) {

        addTaskViewModel.editNotes(item);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentInteractionListener) {
            mListener = (onFragmentInteractionListener) this;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
