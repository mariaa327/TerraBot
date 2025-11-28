package simulation.core;

import simulation.map.SimulationMap;

/**
 * Simple wrapper tying together the map and the robot.
 * Later we’ll add clock, updaters etc., but tests 1–2
 * only need map + current robot position.
 */
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
