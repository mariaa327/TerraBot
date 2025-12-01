package simulation.core;

import simulation.entities.air.Air;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.Soil;
import simulation.entities.water.Water;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public final class Simulation {

    private static final double PRECISION_FACTOR = 100.0;
    private static final double WATER_HUMIDITY_INCREASE = 0.1;
    private static final double WATER_RETENTION_INCREASE = 0.1;
    private static final double HUMIDITY_ROUND_PRECISION = 100.0;
    private static final double RETENTION_ROUND_PRECISION = 100.0;

    private final SimulationMap map;
    private final TerraBot bot;

    private boolean running;

    public Simulation(final SimulationMap map, final TerraBot bot) {
        this.map = map;
        this.bot = bot;
        this.running = false;
    }

    public SimulationMap getMap() {
        return map;
    }

    public TerraBot getBot() {
        return bot;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    /**
     * executes one complete simulation step
     * it iterates over all cells
     * to update entities and resets the movement status of all animals for the next step
     * @return void
     */
    public void update() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                updateCell(cell);
            }
        }
        resetAnimalMovement();
    }

    /**
     * orchestrates the update of entities in a single map cell by delegating
     * specific update logic to helper methods
     * @param cell the map cell to update
     */
    private void updateCell(final MapCell cell) {
        Plant plant = cell.getPlant();
        Soil soil = cell.getSoil();
        Air air = cell.getAir();
        simulation.entities.water.Water water = cell.getWater();

        updatePlantAndOxygen(cell, plant, soil, air);
        updateWaterEffects(cell, plant, soil, air, water);
    }

    /**
     * resets the movement flag for all animals on the map
     */
    private void resetAnimalMovement() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Animal animal = map.getCell(x, y).getAnimal();
                if (animal != null) {
                    animal.setMoved(false);
                }
            }
        }
    }

    /**
     * handles plant growth, life cycle, and oxygen production
     */
    private void updatePlantAndOxygen(final MapCell cell, Plant plant,
                                      final Soil soil, final Air air) {
        if (plant != null && plant.isScanned()) {
            if (soil != null) {
                plant.grow();
                if ("dead".equals(plant.getGrowthStage())) {
                    cell.setPlant(null);
                    plant = null;
                }
            }

            if (air != null && plant != null) {
                double oxygenProduced = plant.produceOxygen();
                double newOxygen = air.getOxygenLevel() + oxygenProduced;
                newOxygen = Math.round(newOxygen * PRECISION_FACTOR) / PRECISION_FACTOR;
                air.setOxygenLevel(newOxygen);
            }
        }
    }

    /**
     * handles water "tick" and its subsequent effects on air (humidity) and soil (retention).
     */
    private void updateWaterEffects(final MapCell cell, final Plant plant, final Soil soil,
                                    final Air air, final Water water) {
        if (water != null && water.isScanned()) {
            water.tick();

            if (plant != null) {
                plant.grow();
            }

            if (water.shouldUpdateEnvironment()) {
                if (air != null) {
                    double newHum = air.getHumidity() + WATER_HUMIDITY_INCREASE;
                    newHum = Math.round(newHum * HUMIDITY_ROUND_PRECISION)
                            / HUMIDITY_ROUND_PRECISION;
                    air.setHumidity(newHum);
                }

                if (soil != null) {
                    double newRet = soil.getWaterRetention() + WATER_RETENTION_INCREASE;
                    newRet = Math.round(newRet * RETENTION_ROUND_PRECISION)
                            / RETENTION_ROUND_PRECISION;
                    soil.setWaterRetention(newRet);
                }
            }
        }
    }
}
