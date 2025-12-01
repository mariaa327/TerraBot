package simulation.entities.plant;

/**
 * represents a Gymnosperm Plant entity
 */
public final class GymnospermPlant extends Plant {
    private static final double BASE_STUCK_POSSIBILITY = 60.0;
    private static final double OXYGEN_FROM_PLANT = 0.0;

    /**
     * constructs a new GymnospermPlant entity
     * @param name the name of the gymnosperm instance
     * @param mass the mass of the gymnosperm
     */
    public GymnospermPlant(final String name, final double mass) {
        super("GymnospermsPlants", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base stuck possibility for Gymnosperms
     */
    @Override
    protected double getBaseStuckPossibility() {
        return BASE_STUCK_POSSIBILITY;
    }

    /**
     * {@inheritDoc}
     * returns the base oxygen production value for Gymnosperms
     */
    @Override
    protected double getOxygenFromPlant() {
        return OXYGEN_FROM_PLANT;
    }
}
