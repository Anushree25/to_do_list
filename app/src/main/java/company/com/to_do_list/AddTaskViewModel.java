package company.com.to_do_list;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import company.com.to_do_list.database.NotesDetails;
import company.com.to_do_list.repository.NotesRepository;

public class AddTaskViewModel extends ViewModel {

    NotesRepository repository;

    public AddTaskViewModel(Context context,String type){

        if(type.equals("All")) {

            repository = new NotesRepository(context);

        }else{
            repository = new NotesRepository(context, type);
        }

    }

   public void addNotes(NotesDetails notesDetails){
        repository.addNotes(notesDetails);

    }

    public LiveData<List<NotesDetails>> getNotesByType(){
        return repository.getTypeNotes();

    }

    public LiveData<List<NotesDetails>> getAllNotes(){
      return  repository.getAllNotes();
    }

    public void editNotes(NotesDetails notesDetails){

        repository.updateNotes(notesDetails);
     }

    public void deleteNotes(NotesDetails notesDetails){

        repository.deleteNotes(notesDetails);
    }

    public LiveData<Long> checkAdded(){
        return repository.result;
    }



}
