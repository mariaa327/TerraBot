package simulation.core;

import simulation.entities.air.Air;
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
    }

    private void updateCell(MapCell cell) {
        Plant plant = cell.getPlant();
        Soil soil = cell.getSoil();
        Air air = cell.getAir();

        if (plant != null && plant.isScanned()) {

            if (soil != null) {
                plant.grow();
            }
            if (air != null) {
                double oxygenProduced = plant.produceOxygen();
                double newOxygen = air.getOxygenLevel() + oxygenProduced;

                newOxygen = Math.round(newOxygen * 100.0) / 100.0;

                air.setOxygenLevel(newOxygen);
            }
        }
    }
}
