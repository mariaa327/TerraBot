package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * air typically found in desert regions
 */
public final class DesertAir extends Air {

    private static final double MAX_QUALITY_SCORE = 65.0;
    private static final double RAW_SCORE_DUST_PENALTY = 0.2;
    private static final double RAW_SCORE_TEMP_PENALTY = 0.3;
    private static final double STORM_APPLY_PENALTY = 30;

    private double dustParticles;

    /**
     * constructs a new DesertAir entity
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     * @param dustParticles the concentration of dust particles
     */
    public DesertAir(final String name, final double mass, final double humidity,
                     final double temperature, final double oxygenLevel,
                     final double dustParticles) {
        super("DesertAir", name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }

    /**
     * @return the dust particles level
     */
    public double getDustParticles() {
        return this.dustParticles;
    }

    /**
     * {@inheritDoc} returns the maximum quality score for Desert Air (65.0)
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
        return getOxygenLevel() * 2 - dustParticles * RAW_SCORE_DUST_PENALTY
                - getTemperature() * RAW_SCORE_TEMP_PENALTY;
    }

    /**
     * checks if a desertStorm weather event is currently active and within its duration
     * @param currentTimestamp the current simulation time
     * @return true if a desert storm is active false otherwise
     */
    public boolean isDesertStormActive(final int currentTimestamp) {
        if ("desertStorm".equals(activeWeatherType)) {
            if (weatherIsStorm) {
                if (currentTimestamp < weatherEndTimestamp) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * applies a penalty
     * @param baseScore the air quality score before weather modification
     * @return the modified air quality score
     */
    @Override
    protected double applyWeatherModifier(final double baseScore) {
        if (activeWeatherType.equals("desertStorm")) {
            if (weatherIsStorm) {
                double newScore = baseScore - STORM_APPLY_PENALTY;
                return newScore;
            }
        }
        return baseScore;
    }

    /**
     * {@inheritDoc}
     * adds the specific condition of a dessertStorm
     * @param mapper used for node creation
     * @param currentTimestamp the current simulation time
     * @return containing the "desertStorm" status
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper, final int currentTimestamp) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("desertStorm", this.isDesertStormActive(currentTimestamp));
        return specific;
    }
}
