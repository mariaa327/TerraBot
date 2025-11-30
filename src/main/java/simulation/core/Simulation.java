package simulation.core;

import simulation.entities.air.Air;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.Soil;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public class Simulation {
    private final SimulationMap map;
    private final TerraBot bot;

    private boolean running;

    public Simulation(SimulationMap map, TerraBot bot) {
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void update() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                updateCell(cell);
            }
        }

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Animal animal = map.getCell(x, y).getAnimal();
                if (animal != null) {
                    animal.setMoved(false);
                }
            }
        }
    }

    private void updateCell(MapCell cell) {
        Plant plant = cell.getPlant();
        Soil soil = cell.getSoil();
        Air air = cell.getAir();
        simulation.entities.water.Water water = cell.getWater();

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
                newOxygen = Math.round(newOxygen * 100.0) / 100.0;
                air.setOxygenLevel(newOxygen);
            }
        }

        if (water != null && water.isScanned()) {
            water.tick();
            if (plant != null) {
                plant.grow();
            }
            if (water.shouldUpdateEnvironment()) {
                if (air != null) {
                    double newHum = air.getHumidity() + 0.1;
                    newHum = Math.round(newHum * 100.0) / 100.0;
                    air.setHumidity(newHum);
                }

                if (soil != null) {
                    double newRet = soil.getWaterRetention() + 0.1;
                    newRet = Math.round(newRet * 100.0) / 100.0;
                    soil.setWaterRetention(newRet);
                }
            }
        }
    }
}
