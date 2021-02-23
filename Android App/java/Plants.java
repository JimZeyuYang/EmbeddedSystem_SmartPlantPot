package com.example.escw1;

import java.util.ArrayList;

public class Plants {

    private static ArrayList<Plants> PlantDatabase;

    private int id;
    private String name;
    private Boolean sun;
    private Boolean rain;
    private Boolean wind;
    private int water_freq;
    private int water_amt;
    private int rotate_freq;

    public Plants(int id, String name, Boolean sun, Boolean rain, Boolean wind, int water_freq, int water_amt, int rotate_freq) {
        this.id = id;
        this.name = name;
        this.sun = sun;
        this.rain = rain;
        this.wind = wind;
        this.water_freq = water_freq;
        this.water_amt = water_amt;
        this.rotate_freq = rotate_freq;
    }

    public static void initPlants() {
        PlantDatabase = new ArrayList<>();

        Plants Pothos = new Plants(0, "Pothos", false, true, false, 6, 10, 1);
        PlantDatabase.add(Pothos);

        Plants Jade_Plant = new Plants(1, "Jade Plant", false, true, true, 12, 5, 2);
        PlantDatabase.add(Jade_Plant);

        Plants Aglaonema = new Plants(2, "Aglaonema", false, true, false, 12, 10, 1);
        PlantDatabase.add(Aglaonema);

        Plants Asparagus_Fern = new Plants(3, "Asparagus Fern", true, true, false, 3, 20, 2);
        PlantDatabase.add(Asparagus_Fern);

        Plants Snake_Plant = new Plants(3, "Snake_Plant", true, false, true, 12, 5, 1);
        PlantDatabase.add(Snake_Plant);

        Plants Peace_Lily = new Plants(3, "Peace Lily", false, false, false, 6, 10, 2);
        PlantDatabase.add(Peace_Lily);

        Plants Aloe = new Plants(3, "Aloe", true, true, true, 6, 15, 1);
        PlantDatabase.add(Aloe);

        Plants Rubber_Plant = new Plants(3, "Rubber Plant", true, true, true, 4, 15, 2);
        PlantDatabase.add(Rubber_Plant);

        Plants Orchid = new Plants(3, "Orchid", false, false, false, 4, 5, 2);
        PlantDatabase.add(Orchid);

        Plants Cactus = new Plants(3, "Cactus", true, false, true, 23, 5, 3);
        PlantDatabase.add(Cactus);

    }

    public static ArrayList<Plants> getPlantDatabase() {
        return PlantDatabase;
    }

    public static String[] PlantNames() {
        String[] names = new String[PlantDatabase.size()];
        for (int i = 0; i < PlantDatabase.size(); i++) {
            names[i] = PlantDatabase.get(i).name;
        }
        return names;
    }

    public String getName() {
        return name;
    }

    public Boolean getSun() {
        return sun;
    }

    public Boolean getRain() {
        return rain;
    }

    public Boolean getWind() {
        return wind;
    }

    public int getWater_freq() {
        return water_freq;
    }

    public int getWater_amt() {
        return water_amt;
    }

    public int getRotate_freq() {
        return rotate_freq;
    }
}
