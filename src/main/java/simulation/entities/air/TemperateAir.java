package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * air typically found in temperate regions
 */
public final class TemperateAir extends Air {

    private static final double MAX_QUALITY_SCORE = 84.0;
    private static final double RAW_SCORE_OXYGEN_FACTOR = 2;
    private static final double RAW_SCORE_HUMIDITY_FACTOR = 0.7;
    private static final double RAW_SCORE_POLLEN_PENALTY = 0.1;
    private static final int SPRING_PENALTY = 15;

    private final double pollenLevel;

    /**
     * constructs a new TemperateAir entity
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     * @param pollenLevel the pollen concentration level
     */
    public TemperateAir(final String name, final double mass, final double humidity,
                        final double temperature, final double oxygenLevel,
                        final double pollenLevel) {
        super("TemperateAir", name, mass, humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }

    /**
     * @return the pollen level
     */
    public double getPollenLevel() {
        return pollenLevel;
    }

    /**
     * {@inheritDoc} returns the maximum quality score for Temperate Air (84.0)
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
                - pollenLevel * RAW_SCORE_POLLEN_PENALTY;
    }

    /**
     * {@inheritDoc}
     * applies a penalty to the base score if the active weather type is newSeason
     * and the season is spring
     * @param baseScore the air quality score before weather modification.
     * @return the modified air quality score.
     */
    @Override
    protected double applyWeatherModifier(final double baseScore) {
        if (activeWeatherType.equals("newSeason")) {
            int penalty = 0;
            if (weatherSeason != null) {
                if (weatherSeason.equalsIgnoreCase("Spring")) {
                    penalty = SPRING_PENALTY;
                }
            }
            return baseScore - penalty;
        }
        return baseScore;
    }

    /**
     * {@inheritDoc}
     * @param mapper used for node creation.
     * @param currentTimestamp the current simulation time isn't used in this implementation
     * @return containing the "pollenLevel".
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper, final int currentTimestamp) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("pollenLevel", this.getPollenLevel());
        return specific;
    }
}
