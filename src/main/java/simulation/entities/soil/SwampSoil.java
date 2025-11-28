package simulation.entities.soil;

public final class SwampSoil extends Soil {
    private final double waterLogging;

    public SwampSoil(String name,
                     double mass,
                     double nitrogen,
                     double waterRetention,
                     double soilPH,
                     double organicMatter,
                     double waterLogging) {
        super("SwampSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.waterLogging = waterLogging;
    }

    public double getWaterLogging() {
        return waterLogging;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * 1.1
                + getOrganicMatter() * 2.2
                - waterLogging * 5;
    }

    @Override
    public double getStuckProbability() {
        return getWaterLogging() * 10.0;
    }
}
