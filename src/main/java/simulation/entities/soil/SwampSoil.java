package simulation.entities.soil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents a Swamp Soil entity
 */
public final class SwampSoil extends Soil {

    private static final double RAW_SCORE_NITROGEN_FACTOR = 1.1;
    private static final double RAW_SCORE_ORGANIC_MATTER_FACTOR = 2.2;
    private static final double RAW_SCORE_WATER_LOGGING_PENALTY = 5;
    private static final double STUCK_PROBABILITY_MULTIPLIER = 10.0;

    private final double waterLogging;

    /**
     * constructs a new SwampSoil entity
     * @param name the name of the soil instance
     * @param mass the mass of the soil
     * @param nitrogen the nitrogen content level
     * @param waterRetention the water retention capacity
     * @param soilPH the pH level of the soil
     * @param organicMatter the organic matter content
     * @param waterLogging the level of excess water saturation
     */
    public SwampSoil(final String name, final double mass, final double nitrogen,
                     final double waterRetention, final double soilPH,
                     final double organicMatter, final double waterLogging) {
        super("SwampSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.waterLogging = waterLogging;
    }

    /**
     * @return the water logging level
     */
    public double getWaterLogging() {
        return waterLogging;
    }

    /**
     * {@inheritDoc}
     * @return the raw soil quality score
     */
    @Override
    protected double rawScore() {
        return getNitrogen() * RAW_SCORE_NITROGEN_FACTOR
                + getOrganicMatter() * RAW_SCORE_ORGANIC_MATTER_FACTOR
                - waterLogging * RAW_SCORE_WATER_LOGGING_PENALTY;
    }

    /**
     * @return the stuck probability
     */
    @Override
    public double getStuckProbability() {
        return getWaterLogging() * STUCK_PROBABILITY_MULTIPLIER;
    }

    /**
     * {@inheritDoc}
     * adds the water logging level as a specific condition
     * @param mapper used for node creation.
     * @return containing the "waterLogging".
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("waterLogging", this.getWaterLogging());
        return specific;
    }
}
