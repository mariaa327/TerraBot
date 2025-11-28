package simulation.entities.air;

public final class PolarAir extends Air {
    private final double iceCrystalConcentration;

    public PolarAir(String name,
                    double mass,
                    double humidity,
                    double temperature,
                    double oxygenLevel,
                    double iceCrystalConcentration) {
        super("PolarAir", name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    @Override
    protected double getMaxQualityScore() {
        return 142.0; // [cite: 345]
    }

    @Override
    protected double rawScore() {
        return getOxygenLevel() * 2 + (100 - Math.abs(getTemperature())) - iceCrystalConcentration * 0.05;
    }
}
