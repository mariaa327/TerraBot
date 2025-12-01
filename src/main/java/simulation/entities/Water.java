package simulation.entities.water;

import simulation.entities.Entity;

/**
 * represents a static Water entity in the simulation
 * it is a final class extending {@link simulation.entities.Entity}
 */
public final class Water extends Entity {

    private final double purity;
    private final double salinity;
    private final double turbidity;
    private final double contaminantIndex;
    private final double pH;
    private final boolean frozen;

    private int tickCount = 0;

    /**
     * constructs a new Water entity
     * @param type the type of the water
     * @param name the name of the water instance
     * @param mass the mass of the water
     * @param purity the purity level
     * @param salinity the salinity (salt) level
     * @param turbidity the turbidity (cloudiness) level
     * @param contaminantIndex the contaminant index level
     * @param pH the pH level (acidity/alkalinity)
     * @param frozen true if the water is frozen (ice) false otherwise
     */
    public Water(final String type, final String name, final double mass, final double purity,
                 final double salinity, final double turbidity, final double contaminantIndex,
                 final double pH, final boolean frozen) {
        super(type, name, mass);
        this.purity = purity;
        this.salinity = salinity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.pH = pH;
        this.frozen = frozen;
    }

    /**
     * @return the purity level
     */
    public double getPurity() {
        return purity;
    }

    /**
     * @return the salinity level
     */
    public double getSalinity() {
        return salinity;
    }

    /**
     * @return the turbidity level
     */
    public double getTurbidity() {
        return turbidity;
    }

    /**
     * @return the contaminant index
     */
    public double getContaminantIndex() {
        return contaminantIndex;
    }

    /**
     * @return the pH level
     */
    public double getPH() {
        return pH;
    }

    /**
     * @return true if the water is frozen false otherwise
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * increments the internal tick counter
     */
    public void tick() {
        this.tickCount++;
    }

    /**
     * environment should be updated based on the tick count
     * the environment updates only when the tick count is a positive, even number
     * @return true if the environment update condition is met false otherwise
     */
    public boolean shouldUpdateEnvironment() {
        return this.tickCount > 0 && this.tickCount % 2 == 0;
    }
}
