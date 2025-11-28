package simulation.entities.air;

public final class TemperateAir extends Air {
    private final double pollenLevel;

    public TemperateAir(String name,
                        double mass,
                        double humidity,
                        double temperature,
                        double oxygenLevel,
                        double pollenLevel) {
        super("TemperateAir", name, mass, humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }

    public double getPollenLevel() {
        return pollenLevel;
    }

    @Override
    protected double getMaxQualityScore() {
        return 84.0;
    }

    @Override
    protected double rawScore() {
        return getOxygenLevel() * 2 + getHumidity() * 0.7 - pollenLevel * 0.1;
    }
}
