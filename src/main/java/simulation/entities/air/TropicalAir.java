package simulation.entities.air;

public final class TropicalAir extends Air {
    private final double co2Level;

    public TropicalAir(String name,
                       double mass,
                       double humidity,
                       double temperature,
                       double oxygenLevel,
                       double co2Level) {
        super("TropicalAir", name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }

    public double getCo2Level() {
        return co2Level;
    }

    @Override
    protected double getMaxQualityScore() {
        return 82.0;
    }

    @Override
    protected double rawScore() {
        return getOxygenLevel() * 2 + getHumidity() * 0.5 - co2Level * 0.01;
    }

    @Override
    protected double applyWeatherModifier(double baseScore) {
        if (activeWeatherType != null) {
            if (activeWeatherType.equals("rainfall")) {
                return baseScore + weatherVal * 0.3;
            }
        }
        return baseScore;
    }
}
