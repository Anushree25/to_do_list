package company.com.to_do_list.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import java.util.List;
import company.com.to_do_list.AppExecutors;
import company.com.to_do_list.database.NotesDao;
import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.database.ToDoListDatabase;

public class NotesRepository {

    private static final String TAG = "NotesRepository";

    private NotesDao notesDao;
    private String mtype;
    MutableLiveData<List<NotesDetails>> allNotes;
    MutableLiveData<List<NotesDetails>>typeNotes;
    public MutableLiveData<Long> result;


    public NotesRepository(Context context){
        init(context);
        allNotes = new MutableLiveData<List<NotesDetails>>();
        getAllNotesFromDb();
    }

    public NotesRepository(Context context, String type){
        init(context);
        this.mtype = type;
        typeNotes = new MutableLiveData<List<NotesDetails>>();
        getTypeNotesFromDb();

    }


    void init(Context context){

        notesDao = Room.databaseBuilder(context, ToDoListDatabase.class,"Notes")
                .build().getNotesDao();


    }

     void getAllNotesFromDb(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<NotesDetails> list = notesDao.getAllNotes(false);
                allNotes.postValue(list);
            }
        });

    }

    public MutableLiveData<List<NotesDetails>> getAllNotes(){
        return allNotes;
     }


    void getTypeNotesFromDb(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<NotesDetails> list = notesDao.getAllNotesByType(mtype);
                typeNotes.postValue(list);
            }
        });

    }

    public MutableLiveData<List<NotesDetails>> getTypeNotes(){
        return typeNotes;
    }

    public void addNotes(NotesDetails notesDetails){

        result = new MutableLiveData<Long>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
              Long res = notesDao.insert(notesDetails);
              result.postValue(res);
              if(res != 0) {

                 // Toast.makeText(mContext, "Notes added", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    public void updateNotes(NotesDetails notesDetails){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
               notesDao.updateNotesToCompleted(notesDetails.getId(),notesDetails.getChecked());

            }
        });
    }

    public void deleteNotes(NotesDetails notesDetails){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                notesDao.deleteTask(notesDetails);

            }
        });
    }




}
