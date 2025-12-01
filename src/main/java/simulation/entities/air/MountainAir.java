package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents air typically found in mountain regions
 */
public final class MountainAir extends Air {

    private static final double MAX_QUALITY_SCORE = 78.0;
    private static final double ALTITUDE_DIVISOR = 1000.0;
    private static final double OXYGEN_ALTITUDE_PENALTY_FACTOR = 0.5;
    private static final double HUMIDITY_SCORE_FACTOR = 0.6;
    private static final double HIKING_PENALTY_FACTOR = 0.1;
    private static final double OXYGEN_RAW_SCORE_FACTOR = 2;

    private final double altitude;

    /**
     * constructs a new MountainAir entity
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     * @param altitude the altitude of the air location in meters
     */
    public MountainAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel,
                       final double altitude) {
        super("MountainAir", name, mass, humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }

    /**
     * @return the altitude.
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * {@inheritDoc} returns the maximum quality score for Mountain Air (78.0)
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
        double altitudePenalty = altitude / ALTITUDE_DIVISOR
                * OXYGEN_ALTITUDE_PENALTY_FACTOR;
        double oxygenFactor = getOxygenLevel() - altitudePenalty;

        return oxygenFactor * OXYGEN_RAW_SCORE_FACTOR
                + getHumidity() * HUMIDITY_SCORE_FACTOR;
    }

    /**
     * {@inheritDoc}
     * applies a penalty if the active weather type is peopleHiking
     * @param baseScore the air quality score before weather modification
     * @return the modified air quality score
     */
    @Override
    protected double applyWeatherModifier(final double baseScore) {
        if (activeWeatherType.equals("peopleHiking")) {
            double penalty = weatherVal * HIKING_PENALTY_FACTOR;
            double newScore = baseScore - penalty;
            return newScore;
        }
        return baseScore;
    }

    /**
     * {@inheritDoc}
     * @param mapper used for node creation.
     * @param currentTimestamp the current simulation time isn't used in this implementation
     * @return containing the altitude
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper, final int currentTimestamp) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("altitude", this.getAltitude());
        return specific;
    }
}
