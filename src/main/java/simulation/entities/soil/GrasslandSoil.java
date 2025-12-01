package simulation.entities.soil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents a Grassland Soil entity
 */
public final class GrasslandSoil extends Soil {

    private static final double RAW_SCORE_NITROGEN_FACTOR = 1.3;
    private static final double RAW_SCORE_ORGANIC_MATTER_FACTOR = 1.5;
    private static final double RAW_SCORE_ROOT_DENSITY_FACTOR = 0.8;
    private static final double STUCK_PROBABILITY_BASE_VALUE = 50.0;
    private static final double STUCK_PROBABILITY_WATER_FACTOR = 0.5;
    private static final double STUCK_PROBABILITY_DIVISOR = 75.0;
    private static final double STUCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double rootDensity;

    /**
     * constructs a new GrasslandSoil entity
     * @param name the name of the soil instance
     * @param mass the mass of the soil
     * @param nitrogen the nitrogen content level
     * @param waterRetention the water retention capacity
     * @param soilPH the pH level of the soil
     * @param organicMatter the organic matter content
     * @param rootDensity the density of roots within the soil
     */
    public GrasslandSoil(final String name, final double mass, final double nitrogen,
                         final double waterRetention, final double soilPH,
                         final double organicMatter, final double rootDensity) {
        super("GrasslandSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.rootDensity = rootDensity;
    }

    /**
     * @return the root density
     */
    public double getRootDensity() {
        return rootDensity;
    }

    /**
     * {@inheritDoc}
     * @return the raw soil quality score
     */
    @Override
    protected double rawScore() {
        return getNitrogen() * RAW_SCORE_NITROGEN_FACTOR
                + getOrganicMatter() * RAW_SCORE_ORGANIC_MATTER_FACTOR
                + rootDensity * RAW_SCORE_ROOT_DENSITY_FACTOR;
    }

    /**
     * @return the stuck probability
     */
    @Override
    public double getStuckProbability() {
        return ((STUCK_PROBABILITY_BASE_VALUE - getRootDensity())
                + getWaterRetention() * STUCK_PROBABILITY_WATER_FACTOR)
                / STUCK_PROBABILITY_DIVISOR
                * STUCK_PROBABILITY_MULTIPLIER;
    }

    /**
     * {@inheritDoc}
     * adds the root density level as a specific condition
     * @param mapper used for node creation
     * @return containing the "rootDensity"
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("rootDensity", this.getRootDensity());
        return specific;
    }
}
