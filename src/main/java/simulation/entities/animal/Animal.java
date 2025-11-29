package simulation.entities.animal;

import simulation.entities.Entity;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public abstract class Animal extends Entity {

    private boolean moved;
    protected Animal(String type, String name, double mass) {
        super(type, name, mass);
        this.moved = false;;
    }

    protected abstract double getBaseAttackPossibility();

    public double getAttackProbability() {
        if (!isScanned()) {
            return (100.0 - getBaseAttackPossibility()) / 10.0;
        }
        return getBaseAttackPossibility();
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void makeTurn(MapCell currentCell, SimulationMap map, int x, int y) {
        this.moved = true;
    }

}
