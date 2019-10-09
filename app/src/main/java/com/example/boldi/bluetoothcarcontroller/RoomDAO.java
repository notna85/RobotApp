package com.example.boldi.bluetoothcarcontroller;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RoomDAO
{
    @Insert
    void insert(RoomTable... robotData);
    @Update
    void update(RoomTable... robotData);
    @Delete
    void delete(RoomTable... robotData);

    @Query("SELECT * FROM robotData WHERE robotID = :id")
    RoomTable getDataByID(String id);

    @Query("SELECT distanceTravelled FROM robotData WHERE robotID = :id")
    int getDistance(String id);

    @Query("SELECT robotID FROM robotData WHERE robotID = :id")
    String getID(String id);

    @Query("UPDATE robotData SET distanceTravelled = :distance WHERE robotID = :id")
    void updateDistance(int distance, String id);

    @Query("UPDATE robotData SET clothChanged = :cloth WHERE robotID = :id")
    void updateCloth(int cloth, String id);

    //For testing purposes
    @Query("DELETE FROM robotData")
    void deleteAll();
}
