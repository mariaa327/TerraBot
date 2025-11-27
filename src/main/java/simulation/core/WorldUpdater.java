package simulation.core;

import simulation.entities.Air;
import simulation.entities.Animal;
import simulation.entities.Plant;
import simulation.entities.Soil;
import simulation.entities.Water;

public final class WorldUpdater {

    private WorldUpdater() {
    }

    public static void apply(SimContext ctx, int timestamp) {
        EnvMap map = ctx.getMap();
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getCols(); j++) {
                var cell = map.getCell(i, j);
                Air air = cell.getAir();
                Soil soil = cell.getSoil();
                Water water = cell.getWater();
                Plant plant = cell.getPlant();
                Animal animal = cell.getAnimal();

                if (air != null) {
                    air.decreaseEvent();
                    double tox = air.toxicity();
                    if (animal != null && animal.isScanned()) {
                        if (tox > air.maxScore() * 0.8) {
                            animal.setState(Animal.State.SICK);
                        }
                    }
                }

                if (soil != null && plant != null && plant.isScanned()) {
                    plant.addGrowth(0.2);
                }

                if (water != null && water.isScanned()) {
                    if ((timestamp - water.getScanTime()) % 2 == 0 && timestamp > water.getScanTime()) {
                        if (air != null) {
                            air.setHumidity(round2(air.getHumidity() + 0.1));
                        }
                        if (soil != null) {
                            soil.setWaterRetention(round2(soil.getWaterRetention() + 0.1));
                        }
                    }
                    if (plant != null && plant.isScanned()) {
                        plant.addGrowth(0.2);
                    }
                }

                if (plant != null && plant.isScanned() && air != null && !plant.isDead()) {
                    air.setOxygenLevel(round2(air.getOxygenLevel()
                            + plant.oxygenFromPlant() + plant.maturityBonus()));
                }
            }
        }
    }

    public static int cellRiskScore(Cell cell) {
        double soilScore = 0;
        double airScore = 0;
        double animalScore = 0;
        double plantScore = 0;
        Soil s = cell.getSoil();
        if (s != null)
            soilScore = s.blockProbability();
        Air a_ = cell.getAir();
        if (a_ != null)
            airScore = a_.toxicity();
        Animal an = cell.getAnimal();
        if (an != null)
            animalScore = an.attackProbabilityScore();
        Plant p = cell.getPlant();
        if (p != null)
            plantScore = p.stickProbability();
        double sum = soilScore + airScore + animalScore + plantScore;
        double mean = Math.abs(sum / 4.0);
        return (int) Math.round(mean);
    }

    public static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public static double normalizeRound100(double v) {
        double n = Math.max(0, Math.min(100, v));
        return round2(n);
    }
}
