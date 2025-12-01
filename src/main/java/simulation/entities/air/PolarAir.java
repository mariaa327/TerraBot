package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represents air typically found in polar regions,
 */
public final class PolarAir extends Air {

    private static final double MAX_QUALITY_SCORE = 142.0;
    private static final double RAW_SCORE_OXYGEN_FACTOR = 2;
    private static final double RAW_SCORE_BASE_TEMP = 100.0;
    private static final double RAW_SCORE_CRYSTAL_PENALTY = 0.05;
    private static final double STORM_PENALTY_FACTOR = 0.2;

    private final double iceCrystalConcentration;

    /**
     * constructs a new PolarAir entity
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     * @param iceCrystalConcentration the concentration of ice crystals
     */
    public PolarAir(final String name, final double mass, final double humidity,
                    final double temperature, final double oxygenLevel,
                    final double iceCrystalConcentration) {
        super("PolarAir", name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    /**
     * @return the ice crystal concentration
     */
    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    /**
     * {@inheritDoc} returns the maximum quality score for Polar Air (142.0)
     */
    @Override
    protected double getMaxQualityScore() {
        return MAX_QUALITY_SCORE;
    }

    /**
     * {@inheritDoc}
     * humidity is not a factor in the raw score calculation for PolarAir
     * @return the raw air quality score.
     */
    @Override
    protected double rawScore() {
        double absTemp = Math.abs(getTemperature());

        return getOxygenLevel() * RAW_SCORE_OXYGEN_FACTOR
                + (RAW_SCORE_BASE_TEMP - absTemp)
                - iceCrystalConcentration * RAW_SCORE_CRYSTAL_PENALTY;
    }

    /**
     * {@inheritDoc}
     * applies a penalty if the active weather type is polarStorm
     * @param baseScore the air quality score before weather modification
     * @return the modified air quality score
     */
    @Override
    protected double applyWeatherModifier(final double baseScore) {
        if (activeWeatherType.equals("polarStorm")) {
            double penalty = weatherVal * STORM_PENALTY_FACTOR;
            return baseScore - penalty;
        }
        return baseScore;
    }

    /**
     * {@inheritDoc}
     * adds the ice crystal concentration as a specific condition
     * @param mapper used for node creation
     * @param currentTimestamp the current simulation time isn't used in this implementation
     * @return containing the iceCrystalConcentration
     */
    @Override
    public ObjectNode getSpecificConditions(final ObjectMapper mapper, final int currentTimestamp) {
        ObjectNode specific = mapper.createObjectNode();
        specific.put("iceCrystalConcentration", this.getIceCrystalConcentration());
        return specific;
    }
}
