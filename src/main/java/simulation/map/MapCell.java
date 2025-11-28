package simulation.map;

import simulation.entities.air.Air;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.Soil;
import simulation.entities.water.Water;

public class MapCell {
    private Air air;
    private Soil soil;
    private Plant plant;
    private Animal animal;
    private Water water;

    public Air getAir() {
        return air;
    }

    public void setAir(Air air) {
        this.air = air;
    }

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

    /**
     * Number of optional objects: plant, animal, water.
     */
    public int getOptionalObjectsCount() {
        int c = 0;
        if (plant != null) c++;
        if (animal != null) c++;
        if (water != null) c++;
        return c;
    }
}
