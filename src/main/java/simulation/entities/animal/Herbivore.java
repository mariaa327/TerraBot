package simulation.entities.animal;

public final class Herbivore extends Animal {
    public Herbivore(String name, double mass) {
        super("Herbivores", name, mass);
    }

    @Override
    protected double getBaseAttackPossibility() {
        return 85.0;
    }
}
