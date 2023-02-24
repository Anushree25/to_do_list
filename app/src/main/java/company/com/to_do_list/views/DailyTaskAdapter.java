package company.com.to_do_list.views;

import androidx.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.List;

import company.com.to_do_list.R;
import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.database.ToDoListDatabase;
import company.com.to_do_list.onFragmentInteractionListener;
import company.com.to_do_list.views.fragments.AddTaskFragment;
import company.com.to_do_list.views.fragments.HomeFragment;

public class DailyTaskAdapter  extends RecyclerView.Adapter<DailyTaskAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private onFragmentInteractionListener mClickListener;
    private List<NotesDetails> mData;
    private  Context mContext;
    private  static ToDoListDatabase toDoListDatabase;

    public DailyTaskAdapter(Context context, List<NotesDetails> data,onFragmentInteractionListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        mClickListener = listener;

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_to_do_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        toDoListDatabase= Room.databaseBuilder(mContext, ToDoListDatabase.class,"Notes")
                .build();

        viewHolder.taskName.setText(mData.get(i).getTitle());
        viewHolder.mItem= mData.get(i);
        viewHolder.toggleButton.setChecked(mData.get(i).getChecked());
        viewHolder.taskTime.setText(mData.get(i).getStartTime());
        viewHolder.taskDate.setText(mData.get(i).getDate());

        viewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    viewHolder.mItem.setChecked(isChecked);
                    mClickListener.onTaskCompleted(viewHolder.mItem);
                    notifyItemChanged(viewHolder.getAdapterPosition());

                // new UpdateNotesAsyncTask(mContext,mData.get(i).getId(),isChecked).execute();

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.onListFragmentInteraction(viewHolder.mItem);
               Log.d("Item clicked","Adapter");

            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {

        ToggleButton toggleButton;
        TextView taskName;
        TextView taskTime;
        TextView taskDate;
        Context context;
        public  NotesDetails mItem;

        ViewHolder(View itemView) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.togglecheckbox);
            taskName = itemView.findViewById(R.id.taskName);
            taskTime = itemView.findViewById(R.id.taskTime);
            taskDate = itemView.findViewById(R.id.taskDate);
            this.context = mContext;
        }
    }

    // convenience method for getting data at click position
    public NotesDetails getItem(int id) {

        return mData.get(id);
    }

    public void removeItem(int id) {

           mData.remove(id);
           notifyDataSetChanged();
    }


    public void editEvent(int position){

        NotesDetails noteClickedDetails = mData.get(position);
        Fragment newFragment = new AddTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",noteClickedDetails.getTitle());
        bundle.putString("notes",noteClickedDetails.getNotes());
        bundle.putString("place",noteClickedDetails.getPlace());
        bundle.putString("date",noteClickedDetails.getDate());
        bundle.putString("starttime",noteClickedDetails.getStartTime());
        bundle.putString("endtime",noteClickedDetails.getEndTime());
        bundle.putString("type",noteClickedDetails.getWorkType());
        newFragment.setArguments(bundle);
        FragmentManager fragmentManager =  ((FragmentActivity)mContext).getSupportFragmentManager();
        switch (fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment).addToBackStack(null).commit()) {
        }


    }

}
