/*
 * File: MessageDatabase.java
 * Author: Zhenni Lu
 * Lab Section: 032
 * Creation Date: April 4, 2024
 *
 * Description:
 * This class represents the Room database for storing DefinitionMessage objects.
 */

package algonquin.cst2335.finalproject.ui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database for storing DefinitionMessage objects.
 */
@Database(entities = {DefinitionMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase instance;

    /**
     * Abstract method to provide access to the DAO interface.
     *
     * @return The DefinitionMessageDAO object.
     */
    public abstract DefinitionMessageDAO dmDAO();

    /**
     * Singleton pattern to ensure only one instance of the database is created.
     *
     * @param context The application context.
     * @return The MessageDatabase instance.
     */
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
