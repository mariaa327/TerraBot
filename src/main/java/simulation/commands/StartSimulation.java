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

import java.util.List; // Folosim List, nu ArrayList, pentru a evita eroarea de tip

public class StartSimulation implements Command {

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (ctx.isSimulationStarted() == false) {

            if (ctx.getCurrentSimulationIndex() < ctx.getSimulations().size()) {

                SimulationInput simInput = ctx.getSimulations().get(ctx.getCurrentSimulationIndex());

                String dim = simInput.getTerritoryDim();
                String[] parts = dim.split("x");
                int mapWidth = Integer.parseInt(parts[0]);
                int mapHeight = Integer.parseInt(parts[1]);

                SimulationMap map = new SimulationMap(mapWidth, mapHeight);
                TerritorySectionParamsInput params = simInput.getTerritorySectionParams();

                List<SoilInput> soils = params.getSoil();
                for (int i = 0; i < soils.size(); i++) {
                    SoilInput s = soils.get(i);
                    for (int j = 0; j < s.getSections().size(); j++) {
                        PairInput p = s.getSections().get(j);

                        if (s.getType().equals("ForestSoil")) {
                            map.getCell(p.getX(), p.getY()).setSoil(new ForestSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getLeafLitter()));
                        } else if (s.getType().equals("SwampSoil")) {
                            map.getCell(p.getX(), p.getY()).setSoil(new SwampSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getWaterLogging()));
                        } else if (s.getType().equals("DesertSoil")) {
                            map.getCell(p.getX(), p.getY()).setSoil(new DesertSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getSalinity()));
                        } else if (s.getType().equals("GrasslandSoil")) {
                            map.getCell(p.getX(), p.getY()).setSoil(new GrasslandSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getRootDensity()));
                        } else if (s.getType().equals("TundraSoil")) {
                            map.getCell(p.getX(), p.getY()).setSoil(new TundraSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getPermafrostDepth()));
                        } else {
                            map.getCell(p.getX(), p.getY()).setSoil(new ForestSoil(s.getName(), s.getMass(), s.getNitrogen(), s.getWaterRetention(), s.getSoilpH(), s.getOrganicMatter(), s.getLeafLitter()));
                        }
                    }
                }

                List<PlantInput> plants = params.getPlants();
                for (int i = 0; i < plants.size(); i++) {
                    PlantInput pl = plants.get(i);
                    for (int j = 0; j < pl.getSections().size(); j++) {
                        PairInput p = pl.getSections().get(j);

                        if (pl.getType().equals("FloweringPlants")) {
                            map.getCell(p.getX(), p.getY()).setPlant(new FloweringPlant(pl.getName(), pl.getMass()));
                        } else if (pl.getType().equals("GymnospermsPlants")) {
                            map.getCell(p.getX(), p.getY()).setPlant(new GymnospermPlant(pl.getName(), pl.getMass()));
                        } else if (pl.getType().equals("Ferns")) {
                            map.getCell(p.getX(), p.getY()).setPlant(new FernPlant(pl.getName(), pl.getMass()));
                        } else if (pl.getType().equals("Mosses")) {
                            map.getCell(p.getX(), p.getY()).setPlant(new MossPlant(pl.getName(), pl.getMass()));
                        } else if (pl.getType().equals("Algae")) {
                            map.getCell(p.getX(), p.getY()).setPlant(new AlgaePlant(pl.getName(), pl.getMass()));
                        } else {
                            map.getCell(p.getX(), p.getY()).setPlant(new FloweringPlant(pl.getName(), pl.getMass()));
                        }
                    }
                }

                List<AnimalInput> animals = params.getAnimals();
                for (int i = 0; i < animals.size(); i++) {
                    AnimalInput anim = animals.get(i);
                    for (int j = 0; j < anim.getSections().size(); j++) {
                        PairInput p = anim.getSections().get(j);

                        if (anim.getType().equals("Herbivores")) {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Herbivore(anim.getName(), anim.getMass()));
                        } else if (anim.getType().equals("Carnivores")) {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Carnivore(anim.getName(), anim.getMass()));
                        } else if (anim.getType().equals("Omnivores")) {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Omnivore(anim.getName(), anim.getMass()));
                        } else if (anim.getType().equals("Detritivores")) {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Detritivore(anim.getName(), anim.getMass()));
                        } else if (anim.getType().equals("Parasites")) {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Parasite(anim.getName(), anim.getMass()));
                        } else {
                            map.getCell(p.getX(), p.getY()).setAnimal(new Herbivore(anim.getName(), anim.getMass()));
                        }
                    }
                }

                List<WaterInput> waterList = params.getWater();
                for (WaterInput waterIn : waterList) {
                    for (PairInput p : waterIn.getSections()) {
                        map.getCell(p.getX(), p.getY()).setWater(new Water(
                                waterIn.getType(), waterIn.getName(), waterIn.getMass(), waterIn.getPurity(),
                                waterIn.getSalinity(), waterIn.getTurbidity(), waterIn.getContaminantIndex(),
                                waterIn.getPH(), waterIn.isFrozen()
                        ));
                    }
                }

                List<AirInput> airList = params.getAir();
                for (int i = 0; i < airList.size(); i++) {
                    AirInput a = airList.get(i);
                    for (int j = 0; j < a.getSections().size(); j++) {
                        PairInput p = a.getSections().get(j);

                        if (a.getType().equals("TropicalAir")) {
                            map.getCell(p.getX(), p.getY()).setAir(new TropicalAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getCo2Level()));
                        } else if (a.getType().equals("TemperateAir")) {
                            map.getCell(p.getX(), p.getY()).setAir(new TemperateAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getPollenLevel()));
                        } else if (a.getType().equals("DesertAir")) {
                            map.getCell(p.getX(), p.getY()).setAir(new DesertAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getDustParticles()));
                        } else if (a.getType().equals("PolarAir")) {
                            map.getCell(p.getX(), p.getY()).setAir(new PolarAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getIceCrystalConcentration()));
                        } else if (a.getType().equals("MountainAir")) {
                            map.getCell(p.getX(), p.getY()).setAir(new MountainAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getAltitude()));
                        } else {
                            map.getCell(p.getX(), p.getY()).setAir(new TemperateAir(a.getName(), a.getMass(), a.getHumidity(), a.getTemperature(), a.getOxygenLevel(), a.getPollenLevel()));
                        }
                    }
                }
                int energy = simInput.getEnergyPoints();
                TerraBot bot = new TerraBot(new Coordinates(0, 0), energy);
                Simulation sim = new Simulation(map, bot);
                sim.setRunning(true);

                ctx.setSimulation(sim);
                ctx.setSimulationStarted(true);
                ctx.incrementSimulationIndex();

                result.put("message", "Simulation has started.");

            } else {
                result.put("message", "ERROR: No simulation available. Cannot perform action");
            }
        } else {
            result.put("message", "ERROR: Simulation already started. Cannot perform action");
        }
    }
}