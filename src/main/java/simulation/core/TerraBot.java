package simulation.core;

import simulation.map.Coordinates;

public class TerraBot {
    private Coordinates position;
    private int energy;

    private int busyUntil = -1;

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

    public void recharge(int amount, int currentTimestamp) {
        this.energy += amount;
        this.busyUntil = currentTimestamp + amount;
    }

    public boolean isCharging(int currentTimestamp) {
        if (busyUntil == -1)
            return false;
        return currentTimestamp < busyUntil;
    }
}