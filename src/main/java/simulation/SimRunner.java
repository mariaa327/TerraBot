package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SimulationInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;

import java.util.ArrayList;
import java.util.List;

public class SimRunner {

    private final ObjectMapper mapper;
    private final ArrayNode buffer;
    private final List<SimulationInput> sims;
    private final List<CommandInput> cmds;

    private int simIndex = -1;
    private boolean started;

    private int width;
    private int height;
    private Tile[][] grid;
    private int posX;
    private int posY;

    private static class Tile {
        AirInput air;
        SoilInput soil;
        PlantInput plant;
        AnimalInput animal;
        WaterInput water;
    }

    public SimRunner(ObjectMapper mapper, ArrayNode buffer, InputLoader loader) {
        this.mapper = mapper;
        this.buffer = buffer;
        this.sims = new ArrayList<>(loader.getSimulations());
        this.cmds = new ArrayList<>(loader.getCommands());
    }

    public void run() {
        for (CommandInput in : cmds) {
            handle(in);
        }
    }

    private void handle(CommandInput in) {
        String c = in.getCommand();

        if ("startSimulation".equals(c)) {
            startSim(in);
            return;
        }

        if ("endSimulation".equals(c)) {
            endSim(in);
            return;
        }

        if (!started) {
            message(in, "ERROR: Simulation not started. Cannot perform action");
            return;
        }

        switch (c) {
            case "printEnvConditions" -> printEnv(in);
            case "printMap" -> printMap(in);
            default -> message(in, "ERROR: Unknown command");
        }
    }

    private void startSim(CommandInput in) {
        if (started) {
            message(in, "ERROR: Simulation already started. Cannot perform action");
            return;
        }

        simIndex++;
        if (simIndex < 0 || simIndex >= sims.size()) {
            message(in, "ERROR: Simulation not started. Cannot perform action");
            return;
        }

        SimulationInput s = sims.get(simIndex);

        parseDim(s.getTerritoryDim());

        buildGrid(s.getTerritorySectionParams());

        posX = 0;
        posY = 0;

        started = true;
        message(in, "Simulation has started.");
    }

    private void endSim(CommandInput in) {
        if (!started) {
            message(in, "ERROR: Simulation not started. Cannot perform action");
            return;
        }
        started = false;
        message(in, "Simulation has ended.");
    }

    private void parseDim(String dim) {
        String[] parts = dim.split("x");
        width = Integer.parseInt(parts[0]);
        height = Integer.parseInt(parts[1]);
    }

    private void buildGrid(TerritorySectionParamsInput p) {
        grid = new Tile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Tile();
            }
        }

        if (p.getSoil() != null) {
            for (SoilInput s : p.getSoil()) {
                if (s.getSections() == null) {
                    continue;
                }
                for (PairInput sec : s.getSections()) {
                    grid[sec.getY()][sec.getX()].soil = s;
                }
            }
        }

        if (p.getPlants() != null) {
            for (PlantInput pl : p.getPlants()) {
                if (pl.getSections() == null) {
                    continue;
                }
                for (PairInput sec : pl.getSections()) {
                    grid[sec.getY()][sec.getX()].plant = pl;
                }
            }
        }

        if (p.getAnimals() != null) {
            for (AnimalInput a : p.getAnimals()) {
                if (a.getSections() == null) {
                    continue;
                }
                for (PairInput sec : a.getSections()) {
                    grid[sec.getY()][sec.getX()].animal = a;
                }
            }
        }

        if (p.getWater() != null) {
            for (WaterInput w : p.getWater()) {
                if (w.getSections() == null) {
                    continue;
                }
                for (PairInput sec : w.getSections()) {
                    grid[sec.getY()][sec.getX()].water = w;
                }
            }
        }

        if (p.getAir() != null) {
            for (AirInput a : p.getAir()) {
                if (a.getSections() == null) {
                    continue;
                }
                for (PairInput sec : a.getSections()) {
                    grid[sec.getY()][sec.getX()].air = a;
                }
            }
        }
    }

    private Tile currentTile() {
        if (posY < 0 || posY >= height || posX < 0 || posX >= width) {
            return null;
        }
        return grid[posY][posX];
    }

    private double computeAirQuality(AirInput a) {
        if (a == null) {
            return 0;
        }
        String type = a.getType();
        double score;

        if ("TropicalAir".equals(type)) {
            score = (a.getOxygenLevel() * 2)
                    + (a.getHumidity() * 0.5)
                    - (a.getCo2Level() * 0.01);
        } else if ("PolarAir".equals(type)) {
            score = (a.getOxygenLevel() * 2)
                    + (100 - Math.abs(a.getTemperature()))
                    - (a.getIceCrystalConcentration() * 0.05);
        } else if ("TemperateAir".equals(type)) {
            score = (a.getOxygenLevel() * 2)
                    + (a.getHumidity() * 0.7)
                    - (a.getPollenLevel() * 0.1);
        } else if ("DesertAir".equals(type)) {
            score = (a.getOxygenLevel() * 2)
                    - (a.getDustParticles() * 0.2)
                    - (a.getTemperature() * 0.3);
        } else {
            double oxygenFactor = a.getOxygenLevel()
                    - (a.getAltitude() / 1000.0 * 0.5);
            score = (oxygenFactor * 2)
                    + (a.getHumidity() * 0.6);
        }

        double norm = Math.max(0, Math.min(100, score));
        return Math.round(norm * 100.0) / 100.0;
    }

    private double computeSoilQuality(SoilInput s) {
        if (s == null) {
            return 0;
        }
        String type = s.getType();
        double score;

        if ("ForestSoil".equals(type)) {
            score = (s.getNitrogen() * 1.2)
                    + (s.getOrganicMatter() * 2)
                    + (s.getWaterRetention() * 1.5)
                    + (s.getLeafLitter() * 0.3);
        } else if ("SwampSoil".equals(type)) {
            score = (s.getNitrogen() * 1.1)
                    + (s.getOrganicMatter() * 2.2)
                    - (s.getWaterLogging() * 5);
        } else if ("DesertSoil".equals(type)) {
            score = (s.getNitrogen() * 0.5)
                    + (s.getWaterRetention() * 0.3)
                    - (s.getSalinity() * 2);
        } else if ("GrasslandSoil".equals(type)) {
            score = (s.getNitrogen() * 1.3)
                    + (s.getOrganicMatter() * 1.5)
                    + (s.getRootDensity() * 0.8);
        } else {
            score = (s.getNitrogen() * 0.7)
                    + (s.getOrganicMatter() * 0.5)
                    - (s.getPermafrostDepth() * 1.5);
        }

        double norm = Math.max(0, Math.min(100, score));
        return Math.round(norm * 100.0) / 100.0;
    }

    private String qualityCategory(double q) {
        if (q >= 70) {
            return "good";
        }
        if (q >= 40) {
            return "moderate";
        }
        return "poor";
    }

    private void printMap(CommandInput in) {
        ObjectNode root = mapper.createObjectNode();
        root.put("command", in.getCommand());
        root.put("timestamp", in.getTimestamp());
        ArrayNode out = mapper.createArrayNode();
        root.set("output", out);
        buffer.add(root);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile t = grid[y][x];
                ObjectNode sec = mapper.createObjectNode();

                ArrayNode coord = mapper.createArrayNode();
                coord.add(x);
                coord.add(y);
                sec.set("section", coord);

                int objects = 0;
                if (t.plant != null) {
                    objects++;
                }
                if (t.animal != null) {
                    objects++;
                }
                if (t.water != null) {
                    objects++;
                }
                sec.put("totalNrOfObjects", objects);

                double aq = computeAirQuality(t.air);
                double sq = computeSoilQuality(t.soil);

                sec.put("airQuality", qualityCategory(aq));
                sec.put("soilQuality", qualityCategory(sq));

                out.add(sec);
            }
        }
    }

    private void printEnv(CommandInput in) {
        ObjectNode root = mapper.createObjectNode();
        root.put("command", in.getCommand());
        root.put("timestamp", in.getTimestamp());
        ObjectNode out = mapper.createObjectNode();
        root.set("output", out);
        buffer.add(root);

        Tile t = currentTile();
        if (t == null) {
            return;
        }

        ObjectNode airNode = mapper.createObjectNode();
        if (t.air != null) {
            AirInput a_ = t.air;
            airNode.put("type", a_.getType());
            airNode.put("name", a_.getName());
            airNode.put("mass", a_.getMass());
            airNode.put("humidity", a_.getHumidity());
            airNode.put("temperature", a_.getTemperature());
            airNode.put("oxygenLevel", a_.getOxygenLevel());

            String ty = a_.getType();
            if ("TropicalAir".equals(ty)) {
                double co2Rounded = Math.round(a_.getCo2Level() * 100.0) / 100.0;
                airNode.put("co2Level", co2Rounded);
            } else if ("PolarAir".equals(ty)) {
                airNode.put("iceCrystalConcentration", a_.getIceCrystalConcentration());
            } else if ("TemperateAir".equals(ty)) {
                airNode.put("pollenLevel", a_.getPollenLevel());
            } else if ("DesertAir".equals(ty)) {
                airNode.put("dustParticles", a_.getDustParticles());
            } else {
                airNode.put("altitude", a_.getAltitude());
            }

            double airQuality = computeAirQuality(a_);
            airNode.put("airQuality", airQuality);
        }
        out.set("air", airNode);

        ObjectNode soilNode = mapper.createObjectNode();
        if (t.soil != null) {
            SoilInput s_ = t.soil;
            soilNode.put("type", s_.getType());
            soilNode.put("name", s_.getName());
            soilNode.put("mass", s_.getMass());
            soilNode.put("nitrogen", s_.getNitrogen());
            soilNode.put("waterRetention", s_.getWaterRetention());
            soilNode.put("soilpH", s_.getSoilpH());
            soilNode.put("organicMatter", s_.getOrganicMatter());

            String st = s_.getType();
            if ("SwampSoil".equals(st)) {
                soilNode.put("waterLogging", s_.getWaterLogging());
            } else if ("ForestSoil".equals(st)) {
                soilNode.put("leafLitter", s_.getLeafLitter());
            } else if ("DesertSoil".equals(st)) {
                soilNode.put("salinity", s_.getSalinity());
            } else if ("GrasslandSoil".equals(st)) {
                soilNode.put("rootDensity", s_.getRootDensity());
            } else if ("TundraSoil".equals(st)) {
                soilNode.put("permafrostDepth", s_.getPermafrostDepth());
            }

            double soilQuality = computeSoilQuality(s_);
            soilNode.put("soilQuality", soilQuality);
        }
        out.set("soil", soilNode);

        if (t.plant != null) {
            ObjectNode plantsNode = mapper.createObjectNode();
            PlantInput p_ = t.plant;
            plantsNode.put("type", p_.getType());
            plantsNode.put("name", p_.getName());
            plantsNode.put("mass", p_.getMass());
            out.set("plants", plantsNode);
        }

        if (t.animal != null) {
            ObjectNode animalsNode = mapper.createObjectNode();
            AnimalInput a2 = t.animal;
            animalsNode.put("type", a2.getType());
            animalsNode.put("name", a2.getName());
            animalsNode.put("mass", a2.getMass());
            out.set("animals", animalsNode);
        }

        if (t.water != null) {
            ObjectNode waterNode = mapper.createObjectNode();
            WaterInput w_ = t.water;
            waterNode.put("type", w_.getType());
            waterNode.put("name", w_.getName());
            waterNode.put("mass", w_.getMass());
            out.set("water", waterNode);
        }
    }

    private void message(CommandInput in, String text) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", in.getCommand());
        node.put("message", text);
        node.put("timestamp", in.getTimestamp());
        buffer.add(node);
    }
}
