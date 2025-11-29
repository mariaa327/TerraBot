package simulation.entities.plant;

import simulation.entities.Entity;

public abstract class Plant extends Entity {
    protected String growthStage = "young";
    protected double growthLevel = 0.0;

    protected Plant(String type, String name, double mass) {
        super(type, name, mass);
    }

    protected abstract double getBaseStuckPossibility();

    protected abstract double getOxygenFromPlant();

    public double getStuckProbability() {
        return getBaseStuckPossibility() / 100.0;
    }

    public void grow() {
        if ("dead".equals(growthStage)) return;

        this.growthLevel += 0.2;

        if (this.growthLevel >= 1.0) {
            evolve();
            this.growthLevel = 0.0;
        }
    }

    private void evolve() {
        switch (growthStage) {
            case "young":
                growthStage = "mature";
                break;
            case "mature":
                growthStage = "old";
                break;
            case "old":
                growthStage = "dead";
                break;
        }
    }

    public double produceOxygen() {
        if ("dead".equals(growthStage))
            return 0.0;

        double maturityRate = 0.0;
        switch (growthStage) {
            case "young":
                maturityRate = 0.2;
                break;
            case "mature":
                maturityRate = 0.7;
                break;
            case "old":
                maturityRate = 0.4;
                break;
        }

        return getOxygenFromPlant() + maturityRate;
    }
}