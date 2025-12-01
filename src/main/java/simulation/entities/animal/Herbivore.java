package simulation.entities.animal;

/**
 * represents a Herbivore animal entity
 */
public final class Herbivore extends Animal {
    private static final double BASE_ATTACK_POSSIBILITY = 85.0;

    /**
     * constructs a new Herbivore entity
     * @param name the name of the herbivore
     * @param mass the mass of the herbivore
     */
    public Herbivore(final String name, final double mass) {
        super("Herbivores", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base attack possibility for Herbivores
     */
    @Override
    protected double getBaseAttackPossibility() {
        return BASE_ATTACK_POSSIBILITY;
    }
}
