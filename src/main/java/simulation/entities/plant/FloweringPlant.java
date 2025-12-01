package simulation.entities.plant;

/**
 * represents a Flowering Plant entity
 */
public final class FloweringPlant extends Plant {
    private static final double BASE_STUCK_POSSIBILITY = 90.0;
    private static final double OXYGEN_FROM_PLANT = 6.0;

    /**
     * constructs a new FloweringPlant entity
     * @param name the name of the flowering plant instance
     * @param mass the mass of the flowering plant
     */
    public FloweringPlant(final String name, final double mass) {
        super("FloweringPlants", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base stuck possibility for Flowering Plants
     */
    @Override
    protected double getBaseStuckPossibility() {
        return BASE_STUCK_POSSIBILITY;
    }

    /**
     * {@inheritDoc}
     * returns the base oxygen production value for Flowering Plants
     */
    @Override
    protected double getOxygenFromPlant() {
        return OXYGEN_FROM_PLANT;
    }
}
