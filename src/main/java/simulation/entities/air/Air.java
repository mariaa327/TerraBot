package simulation.entities.air;

import simulation.entities.Entity;

public abstract class Air extends Entity {
    private double humidity;
    private final double temperature;
    private double oxygenLevel;

    protected String activeWeatherType = null;
    protected int weatherEndTimestamp = -1;

    // Stocăm parametrii evenimentului meteo pentru a-i folosi în formule
    protected Double weatherVal = 0.0;     // rainfall, windSpeed, hikers
    protected String weatherSeason = null; // season
    protected Boolean weatherIsStorm = false; // desertStorm

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

    // Getters standard
    public double getHumidity() { return humidity; }
    public double getTemperature() { return temperature; }
    public double getOxygenLevel() { return oxygenLevel; }

    public void setOxygenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    protected abstract double rawScore();

    protected abstract double getMaxQualityScore();

    protected abstract double applyWeatherModifier(double baseScore);

    public void setWeatherEvent(String type, int startTimestamp, Double val, String season, Boolean isStorm) {
        this.activeWeatherType = type;
        this.weatherEndTimestamp = startTimestamp + 2;

        this.weatherVal = val;
        this.weatherSeason = season;
        this.weatherIsStorm = isStorm;
    }

    public double getAirQuality(int currentTimestamp) {
        double score = rawScore();
        if (activeWeatherType != null && currentTimestamp < weatherEndTimestamp) {
            score = applyWeatherModifier(score);
        }

        double normalized = Math.max(0, Math.min(100, score));
        return Math.round(normalized * 100.0) / 100.0;
    }

    public double getDamageProbability(int currentTimestamp) {
        double maxScore = getMaxQualityScore();
        double currentQuality = getAirQuality(currentTimestamp);

        double toxicity = 100.0 * (1.0 - (currentQuality / maxScore));

        return Math.max(0, Math.round(toxicity * 100.0) / 100.0);
    }

    public String getQualityCategory(int currentTimestamp) {
        double q = getAirQuality(currentTimestamp);
        if (q >= 70.0) {
            return "good";
        } else if (q >= 40.0) {
            return "moderate";
        } else {
            return "poor";
        }
    }
}