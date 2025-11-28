package simulation.entities.air;

public final class MountainAir extends Air {
    private final double altitude;

    public MountainAir(String name,
                       double mass,
                       double humidity,
                       double temperature,
                       double oxygenLevel,
                       double altitude) {
        super("MountainAir", name, mass, humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    protected double getMaxQualityScore() {
        return 78.0; // [cite: 348]
    }

    @Override
    protected double rawScore() {
        double oxygenFactor = getOxygenLevel() - (altitude / 1000.0 * 0.5);
        return oxygenFactor * 2 + getHumidity() * 0.6;
    }
}
