package simulation.entities.water;

import simulation.entities.Entity;

public class Water extends Entity {
    private final double purity;
    private final double salinity;
    private final double turbidity;
    private final double contaminantIndex;
    private final double pH;
    private final boolean frozen;

    public Water(String type,
                 String name,
                 double mass,
                 double purity,
                 double salinity,
                 double turbidity,
                 double contaminantIndex,
                 double pH,
                 boolean frozen) {
        super(type, name, mass);
        this.purity = purity;
        this.salinity = salinity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.pH = pH;
        this.frozen = frozen;
    }

    public double getPurity() {
        return purity;
    }

    public double getSalinity() {
        return salinity;
    }

    public double getTurbidity() {
        return turbidity;
    }

    public double getContaminantIndex() {
        return contaminantIndex;
    }

    public double getPH() {
        return pH;
    }

    public boolean isFrozen() {
        return frozen;
    }

}
