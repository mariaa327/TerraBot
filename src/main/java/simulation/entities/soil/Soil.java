package simulation.entities.soil;

import simulation.entities.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * the abstract base class for all soil types in the simulation
 * it extends {@link simulation.entities.Entity}
 */
public abstract class Soil extends Entity {

    private static final double MAX_SCORE_NORMALIZATION = 100.0;
    private static final double SCORE_ROUND_PRECISION = 100.0;
    private static final double CATEGORY_GOOD_THRESHOLD = 70.0;
    private static final double CATEGORY_MODERATE_THRESHOLD = 40.0;

    private final double nitrogen;
    private double waterRetention;
    private final double soilPH;
    private double organicMatter;

    private Double quality;

    /**
     * constructs a new Soil entity
     * @param type the type of the soil
     * @param name the name of the soil instance
     * @param mass the mass of the soil
     * @param nitrogen the nitrogen content level
     * @param waterRetention the water retention capacity
     * @param soilPH the pH level of the soil
     * @param organicMatter the organic matter content
     */
    protected Soil(final String type, final String name, final double mass, final double nitrogen,
                   final double waterRetention, final double soilPH, final double organicMatter) {
        super(type, name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilPH = soilPH;
        this.organicMatter = organicMatter;
    }

    /**
     * @return the nitrogen content
     */
    public double getNitrogen() {
        return nitrogen;
    }

    /**
     * @return the water retention
     */
    public double getWaterRetention() {
        return waterRetention;
    }

    /**
     * @return the soil pH
     */
    public double getSoilPH() {
        return soilPH;
    }

    /**
     * @return the organic matter content
     */
    public double getOrganicMatter() {
        return organicMatter;
    }

    /**
     * sets a new organic matter content level
     * @param organicMatter the new organic matter content
     */
    public void setOrganicMatter(final double organicMatter) {
        this.organicMatter = organicMatter;
    }

    /**
     * sets a new water retention capacity
     * @param waterRetention the new water retention
     */
    public void setWaterRetention(final double waterRetention) {
        this.waterRetention = waterRetention;
    }

    /**
     * calculates the raw score of the soil quality based on its properties
     * this method is specific to each concrete soil type
     * @return the raw soil quality score
     */
    protected abstract double rawScore();

    /**
     * retrieves the specific conditions of the soil type
     * @param mapper used for node creation
     * @return containing specific soil conditions
     */
    public abstract ObjectNode getSpecificConditions(ObjectMapper mapper);

    /**
     * calculates the final soil quality score normalizing the raw score between 0 and 100
     * and rounding to two decimal places
     * the result is cached after the first calculation
     * @return the calculated and normalized soil quality score
     */
    public double getSoilQuality() {
        if (quality != null) {
            return quality;
        }

        double score = rawScore();
        double normalized = Math.max(0, Math.min(MAX_SCORE_NORMALIZATION, score));
        quality = Math.round(normalized * SCORE_ROUND_PRECISION) / SCORE_ROUND_PRECISION;
        return quality;
    }

    /**
     * determines the soil quality category based on the current soil quality score
     * @return the soil quality category string
     */
    public String getQualityCategory() {
        double q = getSoilQuality();
        if (q >= CATEGORY_GOOD_THRESHOLD) {
            return "good";
        } else if (q >= CATEGORY_MODERATE_THRESHOLD) {
            return "moderate";
        } else {
            return "poor";
        }
    }

    /**
     * calculates the probability of the robot getting stuck on this soil type
     * @return the stuck probability
     */
    public abstract double getStuckProbability();

    /**
     * @param node the ObjectNode where the soilQuality key will be added
     */
    public void putQualityStatus(final ObjectNode node) {
        node.put("soilQuality", getQualityCategory());
    }
}
