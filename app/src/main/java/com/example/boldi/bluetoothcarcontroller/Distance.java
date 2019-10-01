package com.example.boldi.bluetoothcarcontroller;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity(tableName = "distance")
public class Distance {
    @PrimaryKey
    @ColumnInfo(name = "roboId")
    public String roboId;

    @ColumnInfo(name = "distance_travelled")
    public int distanceTravelled;

    @ColumnInfo(name = "cloth_events")
    public int clothEvents = 1;
}
// cloth events set to 1 default for the initially attached cloth - increment by 1 for every change of cloth