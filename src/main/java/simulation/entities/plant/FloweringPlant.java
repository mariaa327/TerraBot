package simulation.entities.plant;

public final class FloweringPlant extends Plant {
    public FloweringPlant(String name, double mass) {
        super("FloweringPlants", name, mass);
    }

    @Override
    protected double getBaseStuckPossibility() {
        return 90.0;
    }

    @Override
    protected double getOxygenFromPlant() {
        return 6.0;
    }
}
