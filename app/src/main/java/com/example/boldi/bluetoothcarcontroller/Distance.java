package com.example.boldi.bluetoothcarcontroller;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity(tableName = "distance")
public class Distance {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "robo_Id")
    private String Robo_Id;

    public void setRobo_Id (String Robo_Id){ this.Robo_Id = Robo_Id; }
    public String getRobo_Id() { return Robo_Id; }

    public void setDistance_travelled (int Distance_travelled){ this.Distance_travelled = Distance_travelled; }
    public int getDistance_travelled() { return Distance_travelled; }

    public void setCloth_events (int Cloth_events){ this.Cloth_events = Cloth_events; }
    public int getCloth_events() { return Cloth_events; }

    @ColumnInfo(name = "distance_travelled")
    private int Distance_travelled;

    @ColumnInfo(name = "cloth_events")
    private int Cloth_events;


/*    public Distance(){
        String roboId = Robo_Id;
        int distance_travelled = Distance_travelled;
        int cloth_events = Cloth_events;
    }

    public Distance(String roboId){
        this.Robo_Id = roboId;
        this.Cloth_events = 1;
        this.Distance_travelled = 0;
    }*/


}
// cloth events set to 1 default for the initially attached cloth - increment by 1 for every change of cloth