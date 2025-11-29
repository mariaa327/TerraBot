package simulation.entities.soil;

public final class GrasslandSoil extends Soil {
    private final double rootDensity;

    public GrasslandSoil(String name,
                         double mass,
                         double nitrogen,
                         double waterRetention,
                         double soilPH,
                         double organicMatter,
                         double rootDensity) {
        super("GrasslandSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.rootDensity = rootDensity;
    }

    public double getRootDensity() {
        return rootDensity;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * 1.3 + getOrganicMatter() * 1.5 + rootDensity * 0.8;
    }

    @Override
    public double getStuckProbability() {
        return ((50.0 - getRootDensity()) + getWaterRetention() * 0.5) / 75.0 * 100.0;
    }
}
