package simulation.core;

import simulation.entities.air.Air;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.Soil;
import simulation.entities.water.Water;
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

}