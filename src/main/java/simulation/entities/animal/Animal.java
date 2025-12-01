package simulation.entities.animal;

import simulation.entities.Entity;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

/**
 * the base class for all animal entities in the simulation
 * it extends {@link simulation.entities.Entity} and provides common properties
 * and methods for movement and attack probability calculation
 */
public abstract class Animal extends Entity {

    private static final double MAX_RISK_BASE = 100.0;
    private static final double UNSCANNED_RISK_DIVISOR = 10.0;

    private boolean moved;

    /**
     * constructs a new Animal entity
     * @param type the type of the animal
     * @param name the name of the animal instance
     * @param mass the mass of the animal
     */
    public Animal(final String type, final String name, final double mass) {
        super(type, name, mass);
        this.moved = false;
    }

    /**
     * retrieves the base possibility of an animal attacking
     * this value is specific to each concrete animal type
     * @return the base attack possibility
     */
    protected abstract double getBaseAttackPossibility();

    /**
     * calculates the attack probability, which differs based on whether
     * the animal has been scanned
     * if scanned then the attack probability is the one just calculated
     * if unscanned than the attack probability is the base one
     * @return the calculated attack probability
     */
    public double getAttackProbability() {
        if (!isScanned()) {
            return (MAX_RISK_BASE - getBaseAttackPossibility()) / UNSCANNED_RISK_DIVISOR;
        }
        return getBaseAttackPossibility();
    }

    /**
     * hecks if the animal has moved during the current turn
     * @return true if the animal has moved, false otherwise.
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * sets the moved status of the animal
     * @param moved the new moved status.
     */
    public void setMoved(final boolean moved) {
        this.moved = moved;
    }

    /**
     * executes the animal's actions for a simulation turn
     * this only marks the animal as having moved
     * @param currentCell the map cell the animal is currently on
     * @param map the entire simulation map
     * @param x the x-coordinate of the current cell
     * @param y the y-coordinate of the current cell
     */
    public void makeTurn(final MapCell currentCell, final SimulationMap map,
                         final int x, final int y) {
        this.moved = true;
    }
}
