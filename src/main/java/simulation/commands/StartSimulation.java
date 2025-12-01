package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SimulationInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.entities.air.Air;
import simulation.entities.air.DesertAir;
import simulation.entities.air.MountainAir;
import simulation.entities.air.PolarAir;
import simulation.entities.air.TemperateAir;
import simulation.entities.air.TropicalAir;
import simulation.entities.animal.Animal;
import simulation.entities.animal.Carnivore;
import simulation.entities.animal.Detritivore;
import simulation.entities.animal.Herbivore;
import simulation.entities.animal.Omnivore;
import simulation.entities.animal.Parasite;
import simulation.entities.plant.AlgaePlant;
import simulation.entities.plant.FernPlant;
import simulation.entities.plant.FloweringPlant;
import simulation.entities.plant.GymnospermPlant;
import simulation.entities.plant.MossPlant;
import simulation.entities.plant.Plant;
import simulation.entities.soil.DesertSoil;
import simulation.entities.soil.ForestSoil;
import simulation.entities.soil.GrasslandSoil;
import simulation.entities.soil.Soil;
import simulation.entities.soil.SwampSoil;
import simulation.entities.soil.TundraSoil;
import simulation.entities.water.Water;
import simulation.map.Coordinates;
import simulation.map.SimulationMap;

import java.util.List;

/**
 * the command initializes and begins a new simulation instance
 * it parses the territory dimensions and entities lists from the input
 * creates the map
 * instantiates the entities based on their type and sets up the
 */
public final class StartSimulation implements Command {

    private static final String ERR_ALREADY_STARTED =
            "ERROR: Simulation already started. Cannot perform action";
    private static final String ERR_NO_SIMULATION_AVAILABLE =
            "ERROR: No simulation available. Cannot perform action";

    /**
     * executes the start simulation command
     * it validates the current state
     * initializes the map and all entities sets up the robot and updates the context
     * @param ctx the context object containing the simulation state
     * @return void for errors
     */
    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted()) {

            if (ctx.getCurrentSimulationIndex() < ctx.getSimulations().size()) {

                SimulationInput simInput =
                        ctx.getSimulations().get(ctx.getCurrentSimulationIndex());

                String dim = simInput.getTerritoryDim();
                String[] parts = dim.split("x");
                int mapWidth = Integer.parseInt(parts[0]);
                int mapHeight = Integer.parseInt(parts[1]);

                SimulationMap map = new SimulationMap(mapWidth, mapHeight);
                TerritorySectionParamsInput params = simInput.getTerritorySectionParams();

                processSoil(params.getSoil(), map);
                processPlants(params.getPlants(), map);
                processAnimals(params.getAnimals(), map);
                processWater(params.getWater(), map);
                processAir(params.getAir(), map);

                int energy = simInput.getEnergyPoints();
                TerraBot bot = new TerraBot(new Coordinates(0, 0), energy);
                Simulation sim = new Simulation(map, bot);
                sim.setRunning(true);

                ctx.setSimulation(sim);
                ctx.setSimulationStarted(true);
                ctx.incrementSimulationIndex();

                result.put("message", "Simulation has started.");

            } else {
                result.put("message", ERR_NO_SIMULATION_AVAILABLE);
            }
        } else {
            result.put("message", ERR_ALREADY_STARTED);
        }
    }

    private void processSoil(final List<SoilInput> soils, final SimulationMap map) {
        for (SoilInput s : soils) {
            for (PairInput p : s.getSections()) {
                Soil soil;
                if (s.getType().equals("ForestSoil")) {
                    soil = new ForestSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getLeafLitter());
                } else if (s.getType().equals("SwampSoil")) {
                    soil = new SwampSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getWaterLogging());
                } else if (s.getType().equals("DesertSoil")) {
                    soil = new DesertSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getSalinity());
                } else if (s.getType().equals("GrasslandSoil")) {
                    soil = new GrasslandSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getRootDensity());
                } else if (s.getType().equals("TundraSoil")) {
                    soil = new TundraSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getPermafrostDepth());
                } else {
                    soil = new ForestSoil(s.getName(), s.getMass(),
                            s.getNitrogen(), s.getWaterRetention(),
                            s.getSoilpH(), s.getOrganicMatter(), s.getLeafLitter());
                }
                map.getCell(p.getX(), p.getY()).setSoil(soil);
            }
        }
    }

    private void processPlants(final List<PlantInput> plants, final SimulationMap map) {
        for (PlantInput pl : plants) {
            for (PairInput p : pl.getSections()) {
                Plant plant;
                if (pl.getType().equals("FloweringPlants")) {
                    plant = new FloweringPlant(pl.getName(), pl.getMass());
                } else if (pl.getType().equals("GymnospermsPlants")) {
                    plant = new GymnospermPlant(pl.getName(), pl.getMass());
                } else if (pl.getType().equals("Ferns")) {
                    plant = new FernPlant(pl.getName(), pl.getMass());
                } else if (pl.getType().equals("Mosses")) {
                    plant = new MossPlant(pl.getName(), pl.getMass());
                } else if (pl.getType().equals("Algae")) {
                    plant = new AlgaePlant(pl.getName(), pl.getMass());
                } else {
                    plant = new FloweringPlant(pl.getName(), pl.getMass());
                }
                map.getCell(p.getX(), p.getY()).setPlant(plant);
            }
        }
    }

    private void processAnimals(final List<AnimalInput> animals, final SimulationMap map) {
        for (AnimalInput anim : animals) {
            for (PairInput p : anim.getSections()) {
                Animal animal;
                if (anim.getType().equals("Herbivores")) {
                    animal = new Herbivore(anim.getName(), anim.getMass());
                } else if (anim.getType().equals("Carnivores")) {
                    animal = new Carnivore(anim.getName(), anim.getMass());
                } else if (anim.getType().equals("Omnivores")) {
                    animal = new Omnivore(anim.getName(), anim.getMass());
                } else if (anim.getType().equals("Detritivores")) {
                    animal = new Detritivore(anim.getName(), anim.getMass());
                } else if (anim.getType().equals("Parasites")) {
                    animal = new Parasite(anim.getName(), anim.getMass());
                } else {
                    animal = new Herbivore(anim.getName(), anim.getMass());
                }
                map.getCell(p.getX(), p.getY()).setAnimal(animal);
            }
        }
    }

    private void processWater(final List<WaterInput> waterList, final SimulationMap map) {
        for (WaterInput waterIn : waterList) {
            for (PairInput p : waterIn.getSections()) {
                Water water = new Water(
                        waterIn.getType(), waterIn.getName(), waterIn.getMass(),
                        waterIn.getPurity(), waterIn.getSalinity(),
                        waterIn.getTurbidity(), waterIn.getContaminantIndex(),
                        waterIn.getPH(), waterIn.isFrozen()
                );
                map.getCell(p.getX(), p.getY()).setWater(water);
            }
        }
    }

    private void processAir(final List<AirInput> airList, final SimulationMap map) {
        for (AirInput a : airList) {
            for (PairInput p : a.getSections()) {
                Air air;
                if (a.getType().equals("TropicalAir")) {
                    air = new TropicalAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getCo2Level());
                } else if (a.getType().equals("TemperateAir")) {
                    air = new TemperateAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getPollenLevel());
                } else if (a.getType().equals("DesertAir")) {
                    air = new DesertAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getDustParticles());
                } else if (a.getType().equals("PolarAir")) {
                    air = new PolarAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getIceCrystalConcentration());
                } else if (a.getType().equals("MountainAir")) {
                    air = new MountainAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getAltitude());
                } else {
                    air = new TemperateAir(a.getName(), a.getMass(),
                            a.getHumidity(), a.getTemperature(),
                            a.getOxygenLevel(), a.getPollenLevel());
                }
                map.getCell(p.getX(), p.getY()).setAir(air);
            }
        }
    }
}
