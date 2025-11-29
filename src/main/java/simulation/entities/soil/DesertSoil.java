package simulation.entities.soil;

public final class DesertSoil extends Soil {
    private final double salinity;

    public DesertSoil(String name,
                      double mass,
                      double nitrogen,
                      double waterRetention,
                      double soilPH,
                      double organicMatter,
                      double salinity) {
        super("DesertSoil", name, mass, nitrogen, waterRetention, soilPH, organicMatter);
        this.salinity = salinity;
    }

    public double getSalinity() {
        return salinity;
    }

    @Override
    protected double rawScore() {
        return getNitrogen() * 0.5 + getWaterRetention() * 0.3 - salinity * 2;
    }

    @Override
    public double getStuckProbability() {
        return (100.0 - getWaterRetention() + getSalinity());
    }
}
