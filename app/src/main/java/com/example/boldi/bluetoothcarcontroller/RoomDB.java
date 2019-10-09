package com.example.boldi.bluetoothcarcontroller;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RoomTable.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase
{
    public abstract RoomDAO getRoomDAO();

    private static volatile RoomDB DB;

    static RoomDB getDB(final Context context)
    {
        DB = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, "RobotDB")
                .allowMainThreadQueries()//for testing purposes
                .build();
        return DB;
    }
}
