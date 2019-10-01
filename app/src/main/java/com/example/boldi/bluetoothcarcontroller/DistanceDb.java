package com.example.boldi.bluetoothcarcontroller;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Distance.class}, version = 1)
public abstract class DistanceDb extends RoomDatabase {
    public abstract DistanceDao distanceDao();
}
