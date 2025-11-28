package simulation.entities.soil;

public final class TundraSoil extends Soil {
    private final double permafrostDepth;

    public TundraSoil(String name,
                      double mass,
                      double nitrogen,
                      double waterRetention,
                      double soilPH,
                      double organicMatter,
                      double permafrostDepth) {
        super("TundraSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }

    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * 0.7
                + getOrganicMatter() * 0.5
                - permafrostDepth * 1.5;
    }

    @Override
    public double getStuckProbability() {
        return (50.0 - getPermafrostDepth()) / 50.0 * 100.0;
    }
}
