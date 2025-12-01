package simulation.entities.animal;

/**
 * represents a Detritivore animal entity
 */
public final class Detritivore extends Animal {
    private static final double BASE_ATTACK_POSSIBILITY = 90.0;

    /**
     * constructs a new Detritivore entity
     * @param name the name of the detritivore
     * @param mass the mass of the detritivore
     */
    public Detritivore(final String name, final double mass) {
        super("Detritivores", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base attack possibility for Detritivores - the highest
     */
    @Override
    protected double getBaseAttackPossibility() {
        return BASE_ATTACK_POSSIBILITY;
    }
}
