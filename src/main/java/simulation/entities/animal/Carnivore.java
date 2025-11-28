package simulation.entities.animal;

public final class Carnivore extends Animal {
    public Carnivore(String name, double mass) {
        super("Carnivores", name, mass);
    }

    @Override
    protected double getBaseAttackPossibility() {
        return 30.0;
    }
}
