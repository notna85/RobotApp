package com.example.boldi.bluetoothcarcontroller;

import androidx.room.Database;
import androidx.room.RoomDatabase;



@Database(entities = {Distance.class}, version = 1, exportSchema = false)
public abstract class DistanceDb extends RoomDatabase{
    //private static Distance INSTANCE;
    public abstract DistanceDao getDistanceDao();


}
