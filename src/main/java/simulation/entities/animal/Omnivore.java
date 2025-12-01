package simulation.entities.animal;

/**
 * represents an Omnivore animal entity
 */
public final class Omnivore extends Animal {
    private static final double BASE_ATTACK_POSSIBILITY = 60.0;

    /**
     * constructs a new Omnivore entity
     * @param name the name of the omnivore
     * @param mass the mass of the omnivore
     */
    public Omnivore(final String name, final double mass) {
        super("Omnivores", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base attack possibility for Omnivores
     */
    @Override
    protected double getBaseAttackPossibility() {
        return BASE_ATTACK_POSSIBILITY;
    }
}
