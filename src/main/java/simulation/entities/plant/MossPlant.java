package simulation.entities.plant;

/**
 * represents a Moss Plant entity
 */
public final class MossPlant extends Plant {
    private static final double BASE_STUCK_POSSIBILITY = 40.0;
    private static final double OXYGEN_FROM_PLANT = 0.8;

    /**
     * constructs a new MossPlant entity
     * @param name the name of the moss instance
     * @param mass the mass of the moss
     */
    public MossPlant(final String name, final double mass) {
        super("Mosses", name, mass);
    }

    /**
     * {@inheritDoc}
     * returns the base stuck possibility for Mosses
     */
    @Override
    protected double getBaseStuckPossibility() {
        return BASE_STUCK_POSSIBILITY;
    }

    /**
     * {@inheritDoc}
     * returns the base oxygen production value for Mosses
     */
    @Override
    protected double getOxygenFromPlant() {
        return OXYGEN_FROM_PLANT;
    }
}
