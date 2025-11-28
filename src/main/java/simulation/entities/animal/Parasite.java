package simulation.entities.animal;

public final class Parasite extends Animal {
    public Parasite(String name, double mass) {
        super("Parasites", name, mass);
    }

    @Override
    protected double getBaseAttackPossibility() {
        return 10.0;
    }
}
