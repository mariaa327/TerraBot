package simulation.entities.soil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents a Forest Soil entity
 */
public final class ForestSoil extends Soil {

    private static final double RAW_SCORE_NITROGEN_FACTOR = 1.2;
    private static final double RAW_SCORE_WATER_RETENTION_FACTOR = 1.5;
    private static final double RAW_SCORE_LEAF_LITTER_FACTOR = 0.3;
    private static final double STUCK_PROBABILITY_WATER_FACTOR = 0.6;
    private static final double STUCK_PROBABILITY_LITTER_FACTOR = 0.4;
    private static final double STUCK_PROBABILITY_DIVISOR = 80.0;
    private static final double STUCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double leafLitter;

    /**
     * constructs a new ForestSoil entity
     * @param name the name of the soil instance
     * @param mass the mass of the soil
     * @param nitrogen the nitrogen content level
     * @param waterRetention the water retention capacity
     * @param soilPH the pH level of the soil
     * @param organicMatter the organic matter content
     * @param leafLitter the amount of leaf litter on the surface
     */
    public ForestSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilPH,
                      final double organicMatter, final double leafLitter) {
        super("ForestSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.leafLitter = leafLitter;
    }

    /**
     * @return the leaf litter amount
     */
    public double getLeafLitter() {
        return leafLitter;
    }

    /**
     * {@inheritDoc}
     * @return the raw soil quality score
     */
    @Override
    protected double rawScore() {
        return getNitrogen() * RAW_SCORE_NITROGEN_FACTOR
                + getOrganicMatter() * 2
                + getWaterRetention() * RAW_SCORE_WATER_RETENTION_FACTOR
                + leafLitter * RAW_SCORE_LEAF_LITTER_FACTOR;
    }

    /**
     * {@inheritDoc}
     * @return the stuck probability
     */
    @Override
    public double getStuckProbability() {
        return (getWaterRetention() * STUCK_PROBABILITY_WATER_FACTOR
                + getLeafLitter() * STUCK_PROBABILITY_LITTER_FACTOR)
                / STUCK_PROBABILITY_DIVISOR
                * STUCK_PROBABILITY_MULTIPLIER;
    }

    /**
     * {@inheritDoc}
     * adds the leaf litter amount as a specific condition
     * @param mapper used for node creation.
     * @return containing the "leafLitter".
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("leafLitter", this.getLeafLitter());
        return specific;
    }
}
