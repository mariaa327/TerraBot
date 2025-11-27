package simulation.entities;

import fileio.SoilInput;

public class Soil extends BaseEntity {

    public enum SoilType {
        FOREST_SOIL,
        SWAMP_SOIL,
        DESERT_SOIL,
        GRASSLAND_SOIL,
        TUNDRA_SOIL
    }

    private final SoilType type;
    private double nitrogen;
    private double waterRetention;
    private double soilpH;
    private double organicMatter;
    private double leafLitter;
    private double waterLogging;
    private double permafrostDepth;
    private double rootDensity;
    private double salinity;

    public Soil(String name, double mass, SoilType type) {
        super(name, mass);
        this.type = type;
    }

    public static Soil fromInput(SoilInput in) {
        SoilType t;
        String s = in.getType();
        if ("ForestSoil".equalsIgnoreCase(s)) t = SoilType.FOREST_SOIL;
        else if ("SwampSoil".equalsIgnoreCase(s)) t = SoilType.SWAMP_SOIL;
        else if ("DesertSoil".equalsIgnoreCase(s)) t = SoilType.DESERT_SOIL;
        else if ("GrasslandSoil".equalsIgnoreCase(s)) t = SoilType.GRASSLAND_SOIL;
        else t = SoilType.TUNDRA_SOIL;
        Soil soil = new Soil(in.getName(), in.getMass(), t);
        soil.setNitrogen(in.getNitrogen());
        soil.setWaterRetention(in.getWaterRetention());
        soil.setSoilpH(in.getSoilpH());
        soil.setOrganicMatter(in.getOrganicMatter());
        soil.setLeafLitter(in.getLeafLitter());
        soil.setWaterLogging(in.getWaterLogging());
        soil.setPermafrostDepth(in.getPermafrostDepth());
        soil.setRootDensity(in.getRootDensity());
        soil.setSalinity(in.getSalinity());
        return soil;
    }

    public SoilType getType() {
        return type;
    }

    public double getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(double nitrogen) {
        this.nitrogen = nitrogen;
    }

    public double getWaterRetention() {
        return waterRetention;
    }

    public void setWaterRetention(double waterRetention) {
        this.waterRetention = waterRetention;
    }

    public double getSoilpH() {
        return soilpH;
    }

    public void setSoilpH(double soilpH) {
        this.soilpH = soilpH;
    }

    public double getOrganicMatter() {
        return organicMatter;
    }

    public void setOrganicMatter(double organicMatter) {
        this.organicMatter = organicMatter;
    }

    public double getLeafLitter() {
        return leafLitter;
    }

    public void setLeafLitter(double leafLitter) {
        this.leafLitter = leafLitter;
    }

    public double getWaterLogging() {
        return waterLogging;
    }

    public void setWaterLogging(double waterLogging) {
        this.waterLogging = waterLogging;
    }

    public double getPermafrostDepth() {
        return permafrostDepth;
    }

    public void setPermafrostDepth(double permafrostDepth) {
        this.permafrostDepth = permafrostDepth;
    }

    public double getRootDensity() {
        return rootDensity;
    }

    public void setRootDensity(double rootDensity) {
        this.rootDensity = rootDensity;
    }

    public double getSalinity() {
        return salinity;
    }

    public void setSalinity(double salinity) {
        this.salinity = salinity;
    }

    public double qualityScore() {
        double v = 0;
        switch (type) {
            case FOREST_SOIL:
                v = (nitrogen * 1.2) + (organicMatter * 2) + (waterRetention * 1.5) + (leafLitter * 0.3);
                break;
            case SWAMP_SOIL:
                v = (nitrogen * 1.1) + (organicMatter * 2.2) - (waterLogging * 5);
                break;
            case DESERT_SOIL:
                v = (nitrogen * 0.5) + (waterRetention * 0.3) - (salinity * 2);
                break;
            case GRASSLAND_SOIL:
                v = (nitrogen * 1.3) + (organicMatter * 1.5) + (rootDensity * 0.8);
                break;
            case TUNDRA_SOIL:
                v = (nitrogen * 0.7) + (organicMatter * 0.5) - (permafrostDepth * 1.5);
                break;
            default:
                break;
        }
        return v;
    }

    public double blockProbability() {
        double p = 0;
        switch (type) {
            case FOREST_SOIL:
                p = (waterRetention * 0.6 + leafLitter * 0.4) / 80.0 * 100.0;
                break;
            case SWAMP_SOIL:
                p = waterLogging * 10.0;
                break;
            case DESERT_SOIL:
                p = (100 - waterRetention + salinity) / 100.0 * 100.0;
                break;
            case GRASSLAND_SOIL:
                p = ((50 - rootDensity) + waterRetention * 0.5) / 75.0 * 100.0;
                break;
            case TUNDRA_SOIL:
                p = (50 - permafrostDepth) / 50.0 * 100.0;
                break;
            default:
                break;
        }
        return p;
    }

}
