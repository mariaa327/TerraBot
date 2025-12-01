package simulation.map;

import simulation.entities.air.Air;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.Soil;
import simulation.entities.water.Water;

/**
 * a single cell on the {@link SimulationMap} grid
 * it acts as a container for all entities that can exist at that specific map location
 */
public final class MapCell {

    private Air air;
    private Soil soil;
    private Plant plant;
    private Animal animal;
    private Water water;

    /**
     * gets the Air entity in this cell
     * @return the Air entity, or null if not set
     */
    public Air getAir() {
        return air;
    }

    /**
     * sets the Air entity for this cell
     * @param air the Air entity to set
     */
    public void setAir(final Air air) {
        this.air = air;
    }

    /**
     * gets the Soil entity in this cell
     * @return the Soil entity or null if not set
     */
    public Soil getSoil() {
        return soil;
    }

    /**
     * sets the Soil entity for this cell
     * @param soil the Soil entity to set
     */
    public void setSoil(final Soil soil) {
        this.soil = soil;
    }

    /**
     * gets the Plant entity in this cell
     * @return the Plant entity or null if not set
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * sets the Plant entity for this cell
     * @param plant the Plant entity to set
     */
    public void setPlant(final Plant plant) {
        this.plant = plant;
    }

    /**
     * gets the Animal entity in this cell
     * @return the Animal entity or null if not set
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * sets the Animal entity for this cell
     * @param animal the Animal entity to set
     */
    public void setAnimal(final Animal animal) {
        this.animal = animal;
    }

    /**
     * gets the Water entity in this cell
     * @return the Water entity or null if not set
     */
    public Water getWater() {
        return water;
    }

    /**
     * sets the Water entity for this cell
     * @param water the Water entity to set
     */
    public void setWater(final Water water) {
        this.water = water;
    }

    /**
     * counts the number of optional entities present in the cell
     * @return the count of optional entities
     */
    public int getOptionalObjectsCount() {
        int c = 0;
        if (plant != null) {
            c++;
        }
        if (animal != null) {
            c++;
        }
        if (water != null) {
            c++;
        }
        return c;
    }
}
