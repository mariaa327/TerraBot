package simulation.core;

import simulation.map.Coordinates;

public class TerraBot {
    private Coordinates position;
    private int energy;

    public TerraBot(Coordinates startPosition, int startEnergy) {
        this.position = startPosition;
        this.energy = startEnergy;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void consumeEnergy(int amount) {
        this.energy -= amount;
    }
}