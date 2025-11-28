package simulation.entities.air;

public final class DesertAir extends Air {
    private final double dustParticles;

    public DesertAir(String name,
                     double mass,
                     double humidity,
                     double temperature,
                     double oxygenLevel,
                     double dustParticles) {
        super("DesertAir", name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }

    public double getDustParticles() {
        return dustParticles;
    }

    @Override
    protected double getMaxQualityScore() {
        return 65.0; // [cite: 347]
    }

    @Override
    protected double rawScore() {
        return getOxygenLevel() * 2 - dustParticles * 0.2 - getTemperature() * 0.3;
    }
}
