package simulation.entities.soil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents a Desert Soil entity
 */
public final class DesertSoil extends Soil {

    private static final double RAW_SCORE_NITROGEN_FACTOR = 0.5;
    private static final double RAW_SCORE_WATER_RETENTION_FACTOR = 0.3;
    private static final double RAW_SCORE_SALINITY_PENALTY = 2;
    private static final double STUCK_PROBABILITY_BASE = 100.0;

    private final double salinity;

    /**
     * constructs a new DesertSoil entity
     * @param name the name of the soil instance
     * @param mass the mass of the soil
     * @param nitrogen the nitrogen content level
     * @param waterRetention the water retention capacity
     * @param soilPH the pH level of the soil
     * @param organicMatter the organic matter content
     * @param salinity the level of salt content
     */
    public DesertSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilPH,
                      final double organicMatter, final double salinity) {
        super("DesertSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.salinity = salinity;
    }

    /**
     * @return the salinity
     */
    public double getSalinity() {
        return salinity;
    }

    /**
     * {@inheritDoc}
     * @return the raw soil quality score
     */
    @Override
    protected double rawScore() {
        return getNitrogen() * RAW_SCORE_NITROGEN_FACTOR
                + getWaterRetention() * RAW_SCORE_WATER_RETENTION_FACTOR
                - salinity * RAW_SCORE_SALINITY_PENALTY;
    }

    /**
     * {@inheritDoc}
     * @return the stuck probability
     */
    @Override
    public double getStuckProbability() {
        return (STUCK_PROBABILITY_BASE - getWaterRetention() + getSalinity());
    }

    /**
     * {@inheritDoc}
     * adds the salinity level as a specific condition
     * @param mapper used for node creation
     * @return containing the "salinity"
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("salinity", this.getSalinity());
        return specific;
    }
}
