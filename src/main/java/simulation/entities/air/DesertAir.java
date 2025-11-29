package simulation.entities.air;

public class DesertAir extends Air {
    private double dustParticles;

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
        return this.dustParticles;
    }

    @Override
    protected double getMaxQualityScore() {
        return 65.0; //
    }

    @Override
    protected double rawScore() {
        return getOxygenLevel() * 2 - dustParticles * 0.2 - getTemperature() * 0.3;
    }

    public boolean isDesertStormActive(int currentTimestamp) {
        if (activeWeatherType != null && activeWeatherType.equals("desertStorm")) {
            if (weatherIsStorm == true) {
                if (currentTimestamp < weatherEndTimestamp) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected double applyWeatherModifier(double baseScore) {
        if (activeWeatherType != null) {
            if (activeWeatherType.equals("desertStorm")) {
                if (weatherIsStorm == true) {
                    double newScore = baseScore - 30;
                    return newScore;
                }
            }
        }
        return baseScore;
    }
}