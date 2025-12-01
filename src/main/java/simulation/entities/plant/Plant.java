package simulation.entities.plant;

import simulation.entities.Entity;

/**
 * the abstract base class for all plant types in the simulation
 * it extends {@link simulation.entities.Entity} and manages the plant's
 * growth stage, growth level, and oxygen production capabilities
 */
public abstract class Plant extends Entity {

    private static final double STUCK_PROBABILITY_DIVISOR = 100.0;
    private static final double GROWTH_INCREMENT = 0.2;
    private static final double GROWTH_THRESHOLD = 1.0;
    private static final double OXYGEN_RATE_YOUNG = 0.2;
    private static final double OXYGEN_RATE_MATURE = 0.7;
    private static final double OXYGEN_RATE_OLD = 0.4;
    private static final double OXYGEN_RATE_DEAD = 0.0;

    protected String growthStage = "young";
    protected double growthLevel = 0.0;

    /**
     * constructs a new Plant entity
     * @param type the type of the plant
     * @param name the name of the plant instance
     * @param mass the mass of the plant
     */
    protected Plant(final String type, final String name, final double mass) {
        super(type, name, mass);
    }
    /**
     * retrieves the base possibility for a robot getting stuck on this plant type
     * this value is specific to each concrete plant type
     * @return the base stuck possibility
     */
    protected abstract double getBaseStuckPossibility();
    /**
     * retrieves the base oxygen production value specific to this plant type
     * @return the base oxygen value from the plant species
     */
    protected abstract double getOxygenFromPlant();
    /**
     * @return the current growth stage
     */
    public String getGrowthStage() {
        return growthStage;
    }

    /**
     * calculates the probability of a robot getting stuck on this plant
     * @return the stuck probability
     */
    public double getStuckProbability() {
        return getBaseStuckPossibility() / STUCK_PROBABILITY_DIVISOR;
    }

    /**
     * advances the plant's growth level
     * if the growth level reaches or exceeds the plant evolves to the next stage
     * and the growth level resets to 0.0
     * ead plants do not grow
     */
    public void grow() {
        if ("dead".equals(growthStage)) {
            return;
        }

        this.growthLevel += GROWTH_INCREMENT;

        if (this.growthLevel >= GROWTH_THRESHOLD) {
            evolve();
            this.growthLevel = 0.0;
        }
    }

    /**
     * transitions the plant to the next growth stage
     */
    private void evolve() {
        switch (growthStage) {
            case "young":
                growthStage = "mature";
                break;
            case "mature":
                growthStage = "old";
                break;
            case "old":
                growthStage = "dead";
                break;
            default:
                break;
        }
    }

    /**
     * calculates the total oxygen produced by the plant in the current turn
     * this is the sum of the plant's base oxygen value and a maturity dependent rate
     * dead plants produce 0.0 oxygen
     * @return the total amount of oxygen produced
     */
    public double produceOxygen() {
        if ("dead".equals(growthStage)) {
            return OXYGEN_RATE_DEAD;
        }

        double maturityRate = OXYGEN_RATE_DEAD;

        switch (growthStage) {
            case "young":
                maturityRate = OXYGEN_RATE_YOUNG;
                break;
            case "mature":
                maturityRate = OXYGEN_RATE_MATURE;
                break;
            case "old":
                maturityRate = OXYGEN_RATE_OLD;
                break;
            default:
                break;
        }

        return getOxygenFromPlant() + maturityRate;
    }
}
