package algonquin.cst2335.finalproject.ui;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DefinitionMessage.class}, version=1)
public abstract class MessageDatabase  extends RoomDatabase {
    public abstract DefinitionMessageDAO dmDAO();
}
