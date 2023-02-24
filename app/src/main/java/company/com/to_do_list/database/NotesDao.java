package company.com.to_do_list.database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(NotesDetails notesDetails);

    @Query("SELECT * FROM  Notes WHERE checked=:condition")
    List<NotesDetails>getAllNotes(Boolean condition);

    @Query("SELECT * FROM  Notes WHERE workType=:type")
    List<NotesDetails> getAllNotesByType(String type);

    @Query("UPDATE Notes SET checked= :condition  WHERE id=:id")
    void updateNotesToCompleted(int id,Boolean condition);

    @Query("DELETE FROM NOTES")
    void deleteData();

    @Delete
    void deleteTask(NotesDetails details);




}
