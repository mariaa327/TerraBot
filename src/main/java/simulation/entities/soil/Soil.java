package simulation.entities.soil;

import simulation.entities.Entity;

public abstract class Soil extends Entity {
    private final double nitrogen;
    private double waterRetention;
    private final double soilPH;
    private double organicMatter;

    private Double quality;

    protected Soil(String type,
                   String name,
                   double mass,
                   double nitrogen,
                   double waterRetention,
                   double soilPH,
                   double organicMatter) {
        super(type, name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilPH = soilPH;
        this.organicMatter = organicMatter;
    }

    public double getNitrogen() {
        return nitrogen;
    }

    public double getWaterRetention() {
        return waterRetention;
    }

    public double getSoilPH() {
        return soilPH;
    }

    public double getOrganicMatter() {
        return organicMatter;
    }

    public void setOrganicMatter(double organicMatter) {
        this.organicMatter = organicMatter;
    }

    public void setWaterRetention(double waterRetention) {
        this.waterRetention = waterRetention;
    }

    protected abstract double rawScore();

    public double getSoilQuality() {
        if (quality != null) {
            return quality;
        }

        double score = rawScore();
        double normalized = Math.max(0, Math.min(100, score));
        quality = Math.round(normalized * 100.0) / 100.0;
        return quality;
    }

    public String getQualityCategory() {
        double q = getSoilQuality();
        if (q >= 70.0) {
            return "good";
        } else if (q >= 40.0) {
            return "moderate";
        } else {
            return "poor";
        }
    }

    public abstract double getStuckProbability();
}
