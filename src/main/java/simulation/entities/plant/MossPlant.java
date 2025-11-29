package simulation.entities.plant;

public final class MossPlant extends Plant {
    public MossPlant(String name, double mass) {
        super("Mosses", name, mass);
    }

    @Override
    protected double getBaseStuckPossibility() {
        return 40.0;
    }

    @Override
    protected double getOxygenFromPlant() {
        return 0.8;
    }
}
