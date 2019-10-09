package com.example.boldi.bluetoothcarcontroller;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "robotData")
public class RoomTable
{
    @PrimaryKey @NonNull private String robotID;
    private int distanceTravelled;
    private int clothChanged;

    public void setRobotID(String robotID) { this.robotID = robotID; }
    public String getRobotID() { return robotID; }

    public void setDistanceTravelled(int distanceTravelled) { this.distanceTravelled = distanceTravelled; }
    public int getDistanceTravelled() { return distanceTravelled; }

    public void setClothChanged(int clothChanged) { this.clothChanged = clothChanged; }
    public int getClothChanged() { return clothChanged; }
}
