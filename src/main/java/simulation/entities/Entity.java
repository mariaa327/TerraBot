package simulation.entities;

public abstract class Entity {
    private final String type;
    private final String name;
    private double mass;

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
}