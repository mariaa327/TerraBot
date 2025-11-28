package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.entities.air.*;
import simulation.entities.animal.*;
import simulation.entities.plant.*;
import simulation.entities.soil.*;
import simulation.entities.water.Water;
import simulation.map.Coordinates;
import simulation.map.SimulationMap;

public class StartSimulation implements Command {

    @Override
    public void execute(CommanContext ctx) {

        ObjectNode result = ctx.getResultNode();

        if (ctx.isSimulationStarted()) {
            result.put("message", "ERROR: Simulation already started. Cannot perform action");
            return;
        }

        if (ctx.getCurrentSimulationIndex() >= ctx.getSimulations().size()) {
            result.put("message", "ERROR: No simulation available. Cannot perform action");
            return;
        }

        SimulationInput simInput = ctx.getSimulations().get(ctx.getCurrentSimulationIndex());

        Simulation simulation = buildSimulation(simInput);

        ctx.setSimulation(simulation);
        ctx.setSimulationStarted(true);
        ctx.incrementSimulationIndex();

        result.put("message", "Simulation has started.");
    }

    private Simulation buildSimulation(SimulationInput simInput) {
        String[] dims = simInput.getTerritoryDim().split("x");
        int width = Integer.parseInt(dims[0]);
        int height = Integer.parseInt(dims[1]);

        SimulationMap map = new SimulationMap(width, height);
        TerritorySectionParamsInput params = simInput.getTerritorySectionParams();

        for (SoilInput soilIn : params.getSoil()) {
            for (PairInput p : soilIn.getSections()) {
                map.getCell(p.getX(), p.getY()).setSoil(createSoil(soilIn));
            }
        }

        for (PlantInput plantIn : params.getPlants()) {
            for (PairInput p : plantIn.getSections()) {
                map.getCell(p.getX(), p.getY()).setPlant(createPlant(plantIn));
            }
        }

        for (AnimalInput animalIn : params.getAnimals()) {
            for (PairInput p : animalIn.getSections()) {
                map.getCell(p.getX(), p.getY()).setAnimal(createAnimal(animalIn));
            }
        }

        for (WaterInput waterIn : params.getWater()) {
            for (PairInput p : waterIn.getSections()) {
                Water water = new Water(
                        waterIn.getType(),
                        waterIn.getName(),
                        waterIn.getMass(),
                        waterIn.getPurity(),
                        waterIn.getSalinity(),
                        waterIn.getTurbidity(),
                        waterIn.getContaminantIndex(),
                        waterIn.getPH(),
                        waterIn.isFrozen()
                );
                map.getCell(p.getX(), p.getY()).setWater(water);
            }
        }

        for (AirInput airIn : params.getAir()) {
            for (PairInput p : airIn.getSections()) {
                map.getCell(p.getX(), p.getY()).setAir(createAir(airIn));
            }
        }

        // robot starts at (0,0)
        int initialEnergy = simInput.getEnergyPoints();

        TerraBot bot = new TerraBot(new Coordinates(0, 0), initialEnergy);

        Simulation sim = new Simulation(map, bot);
        sim.setRunning(true);

        return sim;
    }

    private Soil createSoil(SoilInput in) {
        switch (in.getType()) {
            case "ForestSoil":
                return new ForestSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getLeafLitter());
            case "SwampSoil":
                return new SwampSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getWaterLogging());
            case "DesertSoil":
                return new DesertSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getSalinity());
            case "GrasslandSoil":
                return new GrasslandSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getRootDensity());
            case "TundraSoil":
                return new TundraSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getPermafrostDepth());
            default:
                return new ForestSoil(in.getName(), in.getMass(),
                        in.getNitrogen(), in.getWaterRetention(),
                        in.getSoilpH(), in.getOrganicMatter(),
                        in.getLeafLitter());
        }
    }

    private Plant createPlant(PlantInput in) {
        switch (in.getType()) {
            case "FloweringPlants": return new FloweringPlant(in.getName(), in.getMass());
            case "GymnospermsPlants": return new GymnospermPlant(in.getName(), in.getMass());
            case "Ferns": return new FernPlant(in.getName(), in.getMass());
            case "Mosses": return new MossPlant(in.getName(), in.getMass());
            case "Algae": return new AlgaePlant(in.getName(), in.getMass());
            default: return new FloweringPlant(in.getName(), in.getMass());
        }
    }

    private Animal createAnimal(AnimalInput in) {
        switch (in.getType()) {
            case "Herbivores": return new Herbivore(in.getName(), in.getMass());
            case "Carnivores": return new Carnivore(in.getName(), in.getMass());
            case "Omnivores": return new Omnivore(in.getName(), in.getMass());
            case "Detritivores": return new Detritivore(in.getName(), in.getMass());
            case "Parasites": return new Parasite(in.getName(), in.getMass());
            default: return new Herbivore(in.getName(), in.getMass());
        }
    }

    private Air createAir(AirInput in) {
        switch (in.getType()) {
            case "TropicalAir":
                return new TropicalAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getCo2Level());
            case "TemperateAir":
                return new TemperateAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getPollenLevel());
            case "DesertAir":
                return new DesertAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getDustParticles());
            case "PolarAir":
                return new PolarAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getIceCrystalConcentration());
            case "MountainAir":
                return new MountainAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getAltitude());
            default:
                return new TemperateAir(in.getName(), in.getMass(),
                        in.getHumidity(), in.getTemperature(),
                        in.getOxygenLevel(), in.getPollenLevel());
        }
    }
}
