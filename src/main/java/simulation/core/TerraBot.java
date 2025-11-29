package simulation.core;

import simulation.entities.Entity;
import simulation.map.Coordinates;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TerraBot {
    private Coordinates position;
    private int energy;

    private List<Entity> inventory;
    private Set<Coordinates> visited;

    private int busyUntil = -1;

    public TerraBot(Coordinates startPosition, int startEnergy) {
        this.position = startPosition;
        this.energy = startEnergy;
        this.inventory = new ArrayList<>();
        this.visited = new HashSet<>();
        this.visited.add(startPosition);
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
        this.visited.add(position);
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
        if (currentTimestamp < busyUntil) {
            return true;
        }
        return false;
    }

}