package simulation.entities.air;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.entities.Entity;

/**
 * the base class for all air types in the simulation
 * it extends {@link simulation.entities.Entity} and provides common properties
 * and methods for calculating air quality, damage probability, and quality category
 */
public abstract class Air extends Entity {

    private static final double MAX_QUALITY_SCORE = 100.0;
    private static final double CATEGORY_GOOD_THRESHOLD = 70.0;
    private static final double CATEGORY_MODERATE_THRESHOLD = 40.0;
    private static final double TOXICITY_BASE = 100.0;
    private static final double ROUND_PRECISION = 100.0;

    private double humidity;
    private final double temperature;
    private double oxygenLevel;

    protected String activeWeatherType = null;
    protected int weatherEndTimestamp = -1;

    protected Double weatherVal = 0.0;
    protected String weatherSeason = null;
    protected Boolean weatherIsStorm = false;

    /**
     * constructs a new Air entity
     * @param type the type of the air
     * @param name the name of the air instance
     * @param mass the mass of the air
     * @param humidity the humidity level of the air
     * @param temperature the temperature of the air
     * @param oxygenLevel the oxygen level of the air
     */
    public Air(final String type, final String name, final double mass, final double humidity,
               final double temperature, final double oxygenLevel) {
        super(type, name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    /**
     * @return the humidity level
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * @return the temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @return the oxygen level
     */
    public double getOxygenLevel() {
        return oxygenLevel;
    }

    /**
     * @param oxygenLevel the new oxygen level
     */
    public void setOxygenLevel(final double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    /**
     * @param humidity the new humidity level
     */
    public void setHumidity(final double humidity) {
        this.humidity = humidity;
    }

    /**
     * calculates the raw score of the air quality based on its properties
     * this method is specific to each concrete air type
     * @return the raw air quality score
     */
    protected abstract double rawScore();
    /**
     * gets the maximum possible quality score for this specific air type
     * @return the maximum quality score
     */
    protected abstract double getMaxQualityScore();
    /**
     * applies a weather-related modifier to the base air quality score
     * the effect depends on the active weather type
     * @param baseScore the air quality score before weather modification
     * @return the modified air quality score
     */
    protected abstract double applyWeatherModifier(double baseScore);
    /**
     * retrieves the specific conditions of the air type
     * @param mapper used for node creation
     * @param currentTimestamp the current simulation timestamp
     *                         potentially needed for condition checks
     * @return specific air conditions
     */
    public abstract ObjectNode getSpecificConditions(ObjectMapper mapper, int currentTimestamp);

    /**
     * sets a weather event, which will affect the air quality for a duration
     * the event is active for +2 timestamps
     * @param type the type of the weather event
     * @param startTimestamp the time the weather event starts
     * @param val a numerical value associated with the weather
     * @param season the current season if relevant for the weather effect
     * @param isStorm true if the weather is a storm false otherwise
     */
    public void setWeatherEvent(final String type, final int startTimestamp, final Double val,
                                final String season, final Boolean isStorm) {
        this.activeWeatherType = type;
        this.weatherEndTimestamp = startTimestamp + 2;

        this.weatherVal = val;
        this.weatherSeason = season;
        this.weatherIsStorm = isStorm;
    }

    /**
     * calculates the final air quality score, applying weather modifiers if the event is active
     * the score is normalized between 0 and MAX_QUALITY_SCORE
     * and rounded to two decimal places
     * @param currentTimestamp the current simulation time
     * @return the calculated and normalized air quality score
     */
    public double getAirQuality(final int currentTimestamp) {
        double score = rawScore();
        if (activeWeatherType != null && currentTimestamp < weatherEndTimestamp) {
            score = applyWeatherModifier(score);
        }

        double normalized = Math.max(0, Math.min(MAX_QUALITY_SCORE, score));
        return Math.round(normalized * ROUND_PRECISION) / ROUND_PRECISION;
    }

    /**
     * calculates the probability of damage (toxicity) based on the air quality
     * @param currentTimestamp the current simulation time needed for quality calculation
     * @return the damage probability as a percentage
     */
    public double getDamageProbability(final int currentTimestamp) {
        double maxScore = getMaxQualityScore();
        double currentQuality = getAirQuality(currentTimestamp);

        double toxicity = TOXICITY_BASE * (1.0 - (currentQuality / maxScore));

        return Math.max(0, Math.round(toxicity * ROUND_PRECISION) / ROUND_PRECISION);
    }

    /**
     * determines the air quality category based on the current air quality score
     * @param currentTimestamp the current simulation time needed for quality calculation
     * @return the air quality category string
     */
    public String getQualityCategory(final int currentTimestamp) {
        double q = getAirQuality(currentTimestamp);
        if (q >= CATEGORY_GOOD_THRESHOLD) {
            return "good";
        } else if (q >= CATEGORY_MODERATE_THRESHOLD) {
            return "moderate";
        } else {
            return "poor";
        }
    }

    /**
     * delegates the responsibility of setting the air quality category onto the given JSON node
     * @param node the ObjectNode where the "airQuality" key will be added
     * @param currentTimestamp the current simulation time needed for quality calculation
     */
    public void putQualityStatus(final ObjectNode node, final int currentTimestamp) {
        node.put("airQuality", getQualityCategory(currentTimestamp));
    }
}
