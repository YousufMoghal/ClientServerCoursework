/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.repository;

import com.example.smartcampus.model.Room;
import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;



public class DatabaseStorage {

    public static Map<String, Room> ROOMS = new ConcurrentHashMap<>();
    public static Map<String, Sensor> SENSORS = new ConcurrentHashMap<>();
    public static Map<String, List<SensorReading>> SENSOR_READINGS = new ConcurrentHashMap<>();

    static {
        Room room1 = new Room("LIB-301", "Library Study Room", 40);
        Room room2 = new Room("ENG-201", "Engineering Lab", 30);

        ROOMS.put(room1.getId(), room1);
        ROOMS.put(room2.getId(), room2);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, room1.getId());
        Sensor sensor2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, room1.getId());

        SENSORS.put(sensor1.getId(), sensor1);
        SENSORS.put(sensor2.getId(), sensor2);

        SENSOR_READINGS.put(sensor1.getId(), new CopyOnWriteArrayList<>());
        SENSOR_READINGS.put(sensor2.getId(), new CopyOnWriteArrayList<>());
    }
}