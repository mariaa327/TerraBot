package simulation.core;

import simulation.entities.Air;
import simulation.entities.Animal;
import simulation.entities.Plant;
import simulation.entities.Soil;
import simulation.entities.Water;

public final class Cell {

    private Soil soil;
    private Plant plant;
    private Animal animal;
    private Water water;
    private Air air;

    public Soil getSoil() {
        return soil;
    }

    public void setSoil(Soil soil) {
        this.soil = soil;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Water getWater() {
        return water;
    }

    public void setWater(Water water) {
        this.water = water;
    }

    public Air getAir() {
        return air;
    }

    public void setAir(Air air) {
        this.air = air;
    }
}
