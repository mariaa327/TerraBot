package simulation.entities.animal;

public final class Detritivore extends Animal {
    public Detritivore(String name, double mass) {
        super("Detritivores", name, mass);
    }

    @Override
    protected double getBaseAttackPossibility() {
        return 90.0;
    }
}
