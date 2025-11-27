package simulation.core;

import simulation.entities.Animal;
import simulation.entities.Plant;
import simulation.entities.Water;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Robot {

    private int energy;
    private int x;
    private int y;

    private final Map<String, Integer> inventory;
    private final Map<String, List<String>> knowledge;
    private final List<String> subjectOrder;

    public Robot(int energy, int x, int y) {
        this.energy = energy;
        this.x = x;
        this.y = y;
        inventory = new LinkedHashMap<>();
        knowledge = new LinkedHashMap<>();
        subjectOrder = new ArrayList<>();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        if (energy < 0) this.energy = 0;
        else this.energy = energy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }




}
