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
        return 142.0;
    }

    @Override
    protected double rawScore() {
        double absTemp = Math.abs(getTemperature());
        return getOxygenLevel() * 2 + (100 - absTemp) - iceCrystalConcentration * 0.05;
    }

    @Override
    protected double applyWeatherModifier(double baseScore) {
        if (activeWeatherType != null) {
            if (activeWeatherType.equals("polarStorm")) {
                double penalty = weatherVal * 0.2;
                return baseScore - penalty;
            }
        }
        return baseScore;
    }
}
