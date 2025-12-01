package simulation.entities.animal;

/**
 * represents a Carnivore animal entity.
 */
public final class Carnivore extends Animal {
    private static final double BASE_ATTACK_POSSIBILITY = 30.0;

    /**
     * constructs a new Carnivore entity
     * @param name the name of the carnivore
     * @param mass the mass of the carnivore
     */
    public Carnivore(final String name, final double mass) {
        super("Carnivores", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base attack possibility for Carnivores
     */
    @Override
    protected double getBaseAttackPossibility() {
        return BASE_ATTACK_POSSIBILITY;
    }
}
