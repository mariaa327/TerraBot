package simulation.entities;

public abstract class Entity {
    private final String type;
    private final String name;
    private double mass;

    private boolean isScanned = false;

    protected Entity(String type, String name, double mass) {
        this.type = type;
        this.name = name;
        this.mass = mass;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void scan() {
        this.isScanned = true;
    }

    public boolean isScanned() {
        return isScanned;
    }
}