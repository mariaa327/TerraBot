package simulation.entities;

import fileio.PlantInput;
import simulation.core.Cell;
import simulation.core.SimContext;
import simulation.core.WorldUpdater;

public class Plant extends BaseEntity {

    public enum Category {
        FLOWERING_PLANTS,
        GYMNOSPERMS_PLANTS,
        FERNS,
        MOSSES,
        ALGAE
    }

    public enum Maturity {
        YOUNG,
        MATURE,
        OLD,
        DEAD
    }

    private final Category type;
    private double growth;
    private Maturity maturity;
    private boolean scanned;

    public Plant(String name, double mass, Category type) {
        super(name, mass);
        this.type = type;
        this.growth = 0;
        this.maturity = Maturity.YOUNG;
        this.scanned = false;
    }

    public static Plant fromInput(PlantInput in) {
        Category c;
        String t = in.getType();
        if ("FloweringPlants".equalsIgnoreCase(t)) c = Category.FLOWERING_PLANTS;
        else if ("GymnospermsPlants".equalsIgnoreCase(t)) c = Category.GYMNOSPERMS_PLANTS;
        else if ("Ferns".equalsIgnoreCase(t)) c = Category.FERNS;
        else if ("Mosses".equalsIgnoreCase(t)) c = Category.MOSSES;
        else c = Category.ALGAE;
        return new Plant(in.getName(), in.getMass(), c);
    }

    public Category getType() {
        return type;
    }

    public double getGrowth() {
        return growth;
    }

    public void addGrowth(double delta) {
        if (!scanned) return;
        growth += delta;
        while (growth >= 1.0 && maturity != Maturity.DEAD) {
            growth -= 1.0;
            switch (maturity) {
                case YOUNG:
                    maturity = Maturity.MATURE;
                    break;
                case MATURE:
                    maturity = Maturity.OLD;
                    break;
                case OLD:
                    maturity = Maturity.DEAD;
                    break;
                default:
                    break;
            }
        }
    }

    public Maturity getMaturity() {
        return maturity;
    }

    public boolean isDead() {
        return maturity == Maturity.DEAD;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public double oxygenFromPlant() {
        switch (type) {
            case FLOWERING_PLANTS:
                return 6.0;
            case GYMNOSPERMS_PLANTS:
                return 0.0;
            case FERNS:
                return 0.0;
            case MOSSES:
                return 0.8;
            case ALGAE:
                return 0.5;
            default:
                return 0;
        }
    }

    public double maturityBonus() {
        if (maturity == Maturity.YOUNG) {
            switch (type) {
                case FLOWERING_PLANTS:
                    return 0.2;
                case GYMNOSPERMS_PLANTS:
                case FERNS:
                    return 0.2;
                case MOSSES:
                    return 0.2;
                case ALGAE:
                    return 0.2;
                default:
                    return 0;
            }
        }
        if (maturity == Maturity.MATURE) {
            switch (type) {
                case FLOWERING_PLANTS:
                    return 0.7;
                case GYMNOSPERMS_PLANTS:
                case FERNS:
                    return 0.7;
                case MOSSES:
                    return 0.7;
                case ALGAE:
                    return 0.7;
                default:
                    return 0;
            }
        }
        if (maturity == Maturity.OLD) {
            switch (type) {
                case FLOWERING_PLANTS:
                    return 0.4;
                case GYMNOSPERMS_PLANTS:
                case FERNS:
                    return 0.4;
                case MOSSES:
                    return 0.4;
                case ALGAE:
                    return 0.4;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public double stickProbability() {
        double p;
        switch (type) {
            case FLOWERING_PLANTS:
                p = 90;
                break;
            case GYMNOSPERMS_PLANTS:
                p = 60;
                break;
            case FERNS:
                p = 30;
                break;
            case MOSSES:
                p = 40;
                break;
            case ALGAE:
                p = 20;
                break;
            default:
                p = 0;
        }
        return p / 100.0;
    }

}
