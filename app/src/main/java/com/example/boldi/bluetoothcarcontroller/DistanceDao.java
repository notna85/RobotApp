package com.example.boldi.bluetoothcarcontroller;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;


@Dao
    public interface DistanceDao {
        @Query("SELECT robo_Id FROM distance WHERE robo_Id LIKE :robo_id")
        String roboId(String robo_id);

        @Query("SELECT distance_travelled, cloth_events FROM distance WHERE robo_Id LIKE :robo_id")
        public List<Integer> getData(String robo_id);

        @Query("SELECT distance_travelled FROM distance WHERE robo_Id LIKE :robo_id")
        public int getDistance(String robo_id);

        @Query("SELECT cloth_events FROM distance WHERE robo_Id LIKE :robo_id")
        public int travelled(String robo_id);

        @Insert
        public void insertDistance(String robo_id, int distance);

        @Query("UPDATE distance SET distance_travelled = distance_travelled + :travelupdate WHERE robo_Id LIKE :robo_id")
        public void updateDistance(int travelupdate, String robo_id);

        @Query("Update distance Set cloth_events = cloth_events + 1 Where robo_Id = :id")
        public void incrementValue(int id);


    }

