package simulation.entities.plant;

/**
 * represents an Algae plant entity
 */
public final class AlgaePlant extends Plant {
    private static final double BASE_STUCK_POSSIBILITY = 20.0;
    private static final double OXYGEN_FROM_PLANT = 0.5;

    /**
     * constructs a new AlgaePlant entity
     * @param name the name of the algae instance
     * @param mass the mass of the algae
     */
    public AlgaePlant(final String name, final double mass) {
        super("Algae", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base stuck possibility for Algae
     */
    @Override
    protected double getBaseStuckPossibility() {
        return BASE_STUCK_POSSIBILITY;
    }

    /**
     * {@inheritDoc}
     * returns the base oxygen production value for Algae
     */
    @Override
    protected double getOxygenFromPlant() {
        return OXYGEN_FROM_PLANT;
    }
}
