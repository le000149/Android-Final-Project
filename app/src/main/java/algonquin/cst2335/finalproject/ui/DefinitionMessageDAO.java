package algonquin.cst2335.finalproject.ui;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DefinitionMessageDAO {
    @Insert
    long insertMessage(DefinitionMessage d);

    @Query("Select * from DefinitionMessage")
    List<DefinitionMessage> getAllMessages();

    @Query("DELETE FROM DefinitionMessage WHERE word = :word AND definition = :definition")
    void deleteWordAndDefinition(String word, String definition);

}
