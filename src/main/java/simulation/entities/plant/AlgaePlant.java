package simulation.entities.plant;

public final class AlgaePlant extends Plant {
    public AlgaePlant(String name, double mass) {
        super("Algae", name, mass);
    }

    @Override
    protected double getBaseStuckPossibility() {
        return 20.0;
    }

}
