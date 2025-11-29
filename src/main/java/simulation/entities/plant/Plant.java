package simulation.entities.plant;

import simulation.entities.Entity;

public abstract class Plant extends Entity {
    protected Plant(String type, String name, double mass) {
        super(type, name, mass);
    }

    protected abstract double getBaseStuckPossibility();

    public double getStuckProbability() {
        return getBaseStuckPossibility() / 100.0;
    }
}