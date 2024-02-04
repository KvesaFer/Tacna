package com.mkves.tacna.db;

import android.content.Context;

import com.mkves.tacna.model.Event;
import com.mkves.tacna.model.UserFunds;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Event.class, UserFunds.class}, version = 5)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "eventsManager";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract EventDao eventDao();
    public abstract UserFundsDao userFundsDao();
}