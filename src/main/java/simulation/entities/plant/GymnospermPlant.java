package simulation.entities.plant;

public final class GymnospermPlant extends Plant {
    public GymnospermPlant(String name, double mass) {
        super("GymnospermsPlants", name, mass);
    }

    @Override
    protected double getBaseStuckPossibility() {
        return 60.0;
    }

    @Override
    protected double getOxygenFromPlant() {
        return 0.0;
    }
}
