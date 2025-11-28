package simulation.entities.air;

import simulation.entities.Entity;

/**
 * Common base pentru toate tipurile de aer.
 * Aici facem normalizarea + rotunjirea pentru airQuality (conform cerinței).
 */
public abstract class Air extends Entity {
    private final double humidity;
    private final double temperature;
    private final double oxygenLevel;

    private Double airQuality; // normalizat + rotunjit (cache)

    protected Air(String type,
                  String name,
                  double mass,
                  double humidity,
                  double temperature,
                  double oxygenLevel) {
        super(type, name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }


    protected abstract double getMaxQualityScore();
        public double getDamageProbability() {
            double maxScore = getMaxQualityScore(); //polimorfism
            double currentQuality = getAirQuality();

            double toxicity = 100.0 * (1.0 - (currentQuality / maxScore));

            return Math.max(0, Math.round(toxicity * 100.0) / 100.0);
        }

    protected abstract double rawScore();

    public double getAirQuality() {
        if (airQuality != null) {
            return airQuality;
        }

        double score = rawScore();
        double normalized = Math.max(0, Math.min(100, score));
        airQuality = Math.round(normalized * 100.0) / 100.0;
        return airQuality;
    }

    public String getQualityCategory() {
        double q = getAirQuality();
        if (q >= 70.0) {
            return "good";
        } else if (q >= 40.0) {
            return "moderate";
        } else {
            return "poor";
        }
    }

}
