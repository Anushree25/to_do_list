package company.com.to_do_list.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {NotesDetails.class},version = 1)
public abstract class ToDoListDatabase extends RoomDatabase {

    public abstract NotesDao getNotesDao();

    private  ToDoListDatabase databaseInstance = null;

    public ToDoListDatabase getDatabaseInstance(Context context) {

        databaseInstance = Room.databaseBuilder(context, ToDoListDatabase.class, "Notes")
                .fallbackToDestructiveMigration()
                .build();
        return databaseInstance;
    }

}
