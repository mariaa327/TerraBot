package simulation.entities.plant;

/**
 * represents a Fern plant entity
 */
public final class FernPlant extends Plant {
    private static final double BASE_STUCK_POSSIBILITY = 30.0;
    private static final double OXYGEN_FROM_PLANT = 0.0;

    /**
     * constructs a new FernPlant entity
     * @param name the name of the fern instance
     * @param mass the mass of the fern
     */
    public FernPlant(final String name, final double mass) {
        super("Ferns", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base stuck possibility for Ferns
     */
    @Override
    protected double getBaseStuckPossibility() {
        return BASE_STUCK_POSSIBILITY;
    }

    /**
     * {@inheritDoc}
     * returns the base oxygen production value for Ferns
     */
    @Override
    protected double getOxygenFromPlant() {
        return OXYGEN_FROM_PLANT;
    }
}
