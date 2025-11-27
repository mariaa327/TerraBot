package simulation.entities;

import simulation.core.Cell;
import simulation.core.SimContext;

public abstract class BaseEntity {

    private String name;
    private double mass;

    public BaseEntity(String name, double mass) {
        this.name = name;
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double m) {
        this.mass = m;
    }

}
