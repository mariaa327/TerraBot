package simulation.entities;

/**
 * the abstract base class for all entities in the simulation (Air, Soil, Animal, Plant)
 * it provides core properties like type, name, mass, and scan status
 */
public abstract class Entity {
    private final String type;
    private final String name;
    private double mass;

    private boolean isScanned = false;

    /**
     * constructs a new Entity
     * @param type the category of the entity
     * @param name the unique name of the entity instance
     * @param mass the mass of the entity
     */
    protected Entity(final String type, final String name, final double mass) {
        this.type = type;
        this.name = name;
        this.mass = mass;
    }

    /**
     * gets the type/category of the entity
     * @return the entity type
     */
    public String getType() {
        return type;
    }
    /**
     * gets the name of the entity instance
     * @return the entity name
     */
    public String getName() {
        return name;
    }
    /**
     * gets the mass of the entity
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * sets the mass of the entity
     * @param mass the new mass value
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }

    /**
     * marks the entity as having been scanned by the robot
     */
    public void scan() {
        this.isScanned = true;
    }

    /**
     * @return true if the entity has been scanned false otherwise
     */
    public boolean isScanned() {
        return isScanned;
    }
}
