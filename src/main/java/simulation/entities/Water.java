package simulation.entities;

import fileio.WaterInput;
import simulation.core.WorldUpdater;
import simulation.core.Cell;
import simulation.core.SimContext;

public class Water extends BaseEntity {

    public enum WaterType {
        GENERIC
    }

    private final WaterType type;
    private double purity;
    private double salinity;
    private double turbidity;
    private double contaminantIndex;
    private double pH;
    private boolean frozen;
    private boolean scanned;
    private int scanTime;

    public Water(String name, double mass, WaterType type) {
        super(name, mass);
        this.type = type;
    }

    public static Water fromInput(WaterInput in) {
        Water w = new Water(in.getName(), in.getMass(), WaterType.GENERIC);
        w.setPurity(in.getPurity());
        w.setSalinity(in.getSalinity());
        w.setTurbidity(in.getTurbidity());
        w.setContaminantIndex(in.getContaminantIndex());
        w.setPH(in.getPH());
        w.setFrozen(in.isFrozen());
        return w;
    }

    public WaterType getType() {
        return type;
    }

    public double getPurity() {
        return purity;
    }

    public void setPurity(double purity) {
        this.purity = purity;
    }

    public double getSalinity() {
        return salinity;
    }

    public void setSalinity(double salinity) {
        this.salinity = salinity;
    }

    public double getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(double turbidity) {
        this.turbidity = turbidity;
    }

    public double getContaminantIndex() {
        return contaminantIndex;
    }

    public void setContaminantIndex(double contaminantIndex) {
        this.contaminantIndex = contaminantIndex;
    }

    public double getPH() {
        return pH;
    }

    public void setPH(double pH) {
        this.pH = pH;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }

    public double qualityScore() {
        double purityScore = purity / 100.0;
        double pHScore = 1 - Math.abs(pH - 7.5) / 7.5;
        double salinityScore = 1 - (salinity / 350.0);
        double turbidityScore = 1 - (turbidity / 100.0);
        double contaminantScore = 1 - (contaminantIndex / 100.0);
        double frozenScore = frozen ? 0 : 1;
        double q = (0.3 * purityScore
                + 0.2 * pHScore
                + 0.15 * salinityScore
                + 0.1 * turbidityScore
                + 0.15 * contaminantScore
                + 0.2 * frozenScore) * 100.0;
        return WorldUpdater.normalizeRound100(q);
    }

}
