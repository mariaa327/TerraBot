package simulation.entities.soil;

public final class ForestSoil extends Soil {
    private final double leafLitter;

    public ForestSoil(String name,
                      double mass,
                      double nitrogen,
                      double waterRetention,
                      double soilPH,
                      double organicMatter,
                      double leafLitter) {
        super("ForestSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.leafLitter = leafLitter;
    }

    public double getLeafLitter() {
        return leafLitter;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * 1.2 + getOrganicMatter() * 2 + getWaterRetention() * 1.5 + leafLitter * 0.3;
    }

    @Override
    public double getStuckProbability() {
        return (getWaterRetention() * 0.6 + getLeafLitter() * 0.4) / 80.0 * 100.0;
    }
}
