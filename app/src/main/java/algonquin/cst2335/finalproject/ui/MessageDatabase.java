package algonquin.cst2335.finalproject.ui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DefinitionMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase instance;

    public abstract DefinitionMessageDAO dmDAO();

    // Singleton pattern to ensure only one instance of the database is created
    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            // Create a new instance of the database if it doesn't exist
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "message_database")
                    .fallbackToDestructiveMigration() // Temporary solution for database migrations
                    .build();
        }
        return instance;
    }
}

