package com.example.boldi.bluetoothcarcontroller;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
    public interface DistanceDao {
        @Query("SELECT * FROM distance WHERE roboId LIKE :robo_id")
        List<Distance> getDistance(String robo_id);

        @Query("SELECT distance_travelled FROM distance WHERE roboId LIKE :robo_id")
        List<Integer> travelled(String robo_id);

        @Insert
        public void insertDistance(String robo_id, int distance);

        @Query("Update distance Set cloth_events = cloth_events + 1 Where roboId = :id")
        public void incrementValue(long id);


    }

