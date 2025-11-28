package simulation.entities.animal;

import simulation.entities.Entity;

public abstract class Animal extends Entity {

    protected Animal(String type, String name, double mass) {
        super(type, name, mass);
    }

    protected abstract double getBaseAttackPossibility();

    public double getAttackProbability() {
        return (100.0 - getBaseAttackPossibility()) / 10.0;
    }
}
