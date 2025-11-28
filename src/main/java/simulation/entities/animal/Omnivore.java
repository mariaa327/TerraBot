package simulation.entities.animal;

public final class Omnivore extends Animal {
    public Omnivore(String name, double mass) {
        super("Omnivores", name, mass);
    }

    @Override
    protected double getBaseAttackPossibility() {
        return 60.0;
    }
}
