package simulation.entities.plant;

public final class FernPlant extends Plant {
    public FernPlant(String name, double mass) {
        super("Ferns", name, mass);
    }

    @Override
    protected double getBaseStuckPossibility() {
        return 30.0;
    }
}
