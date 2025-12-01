package simulation.entities.animal;

/**
 * represents a Parasite animal entity
 */
public final class Parasite extends Animal {
    private static final double BASE_ATTACK_POSSIBILITY = 10.0;

    /**
     * constructs a new Parasite entity
     * @param name the name of the parasite
     * @param mass the mass of the parasite
     */
    public Parasite(final String name, final double mass) {
        super("Parasites", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base attack possibility for Parasites - the lowest
     */
    @Override
    protected double getBaseAttackPossibility() {
        return BASE_ATTACK_POSSIBILITY;
    }
}
