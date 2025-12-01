package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class TropicalAir extends Air {

    private static final double MAX_QUALITY_SCORE = 82.0;
    private static final double RAW_SCORE_OXYGEN_FACTOR = 2;
    private static final double RAW_SCORE_HUMIDITY_FACTOR = 0.5;
    private static final double RAW_SCORE_CO2_PENALTY = 0.01;
    private static final double RAINFALL_SCORE_BONUS = 0.3;
    private static final double PRECISION_FACTOR = 100.0;

    private final double co2Level;

    /**
     * constructs a new TropicalAir entity
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     * @param co2Level the carbon dioxide concentration level
     */
    public TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel,
                       final double co2Level) {
        super("TropicalAir", name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }

    public double getCo2Level() {
        return co2Level;
    }

    /**
     * {@inheritDoc} returns the maximum quality score for Tropical Air (82.0)
     */
    @Override
    protected double getMaxQualityScore() {
        return MAX_QUALITY_SCORE;
    }

    /**
     * {@inheritDoc}
     * @return the raw air quality score
     */
    @Override
    protected double rawScore() {
        return getOxygenLevel() * RAW_SCORE_OXYGEN_FACTOR
                + getHumidity() * RAW_SCORE_HUMIDITY_FACTOR
                - co2Level * RAW_SCORE_CO2_PENALTY;
    }

    /**
     * {@inheritDoc}
     * @param baseScore the air quality score before weather modification
     * @return the modified air quality score
     */
    @Override
    protected double applyWeatherModifier(final double baseScore) {
        if (activeWeatherType.equals("rainfall")) {
            double newScore = baseScore + weatherVal * RAINFALL_SCORE_BONUS;
            return newScore;
        }
        return baseScore;
    }

    /**
     * {@inheritDoc}
     * adds the rounded CO2 level as a specific condition
     * @param mapper used for node creation.
     * @param currentTimestamp the current simulation time isn't used in this implementation
     * @return containing the "co2Level"
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper, final int currentTimestamp) {
        ObjectNode specific = mapper.createObjectNode();
        double roundedCo2 = Math.round(this.getCo2Level() * PRECISION_FACTOR) / PRECISION_FACTOR;
        specific.put("co2Level", roundedCo2);
        return specific;
    }
}
