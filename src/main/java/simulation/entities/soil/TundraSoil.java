package simulation.entities.soil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class TundraSoil extends Soil {

    private static final double RAW_SCORE_NITROGEN_FACTOR = 0.7;
    private static final double RAW_SCORE_ORGANIC_MATTER_FACTOR = 0.5;
    private static final double RAW_SCORE_PERMAFROST_PENALTY = 1.5;
    private static final double STUCK_PROBABILITY_BASE_VALUE = 50.0;
    private static final double STUCK_PROBABILITY_DIVISOR = 50.0;
    private static final double STUCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double permafrostDepth;

    public TundraSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilPH,
                      final double organicMatter, final double permafrostDepth) {
        super("TundraSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }

    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * RAW_SCORE_NITROGEN_FACTOR
                + getOrganicMatter() * RAW_SCORE_ORGANIC_MATTER_FACTOR
                - permafrostDepth * RAW_SCORE_PERMAFROST_PENALTY;
    }

    @Override
    public double getStuckProbability() {
        return (STUCK_PROBABILITY_BASE_VALUE - getPermafrostDepth())
                / STUCK_PROBABILITY_DIVISOR
                * STUCK_PROBABILITY_MULTIPLIER;
    }

    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("permafrostDepth", this.getPermafrostDepth());
        return specific;
    }
}
