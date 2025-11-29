package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.entities.air.*;
import simulation.entities.animal.Animal;
import simulation.entities.plant.Plant;
import simulation.entities.soil.*;
import simulation.entities.water.Water;
import simulation.map.Coordinates;
import simulation.map.MapCell;
import simulation.core.TerraBot;

public class PrintEnvConditions implements Command {

    private static final String ERR_NOT_STARTED = "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING = "ERROR: Robot still charging. Cannot perform action";

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();
        ObjectMapper mapper = ctx.getMapper();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        Simulation sim = ctx.getSimulation();
        TerraBot robot = sim.getBot();

        if (robot.isCharging(ctx.getCommandInput().getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        Coordinates pos = sim.getBot().getPosition();
        MapCell cell = sim.getMap().getCell(pos);

        ObjectNode out = mapper.createObjectNode();

        Soil soil = cell.getSoil();
        if (soil != null) {
            ObjectNode s = mapper.createObjectNode();
            s.put("type", soil.getType());
            s.put("name", soil.getName());
            s.put("mass", soil.getMass());
            s.put("nitrogen", soil.getNitrogen());
            s.put("waterRetention", soil.getWaterRetention());
            s.put("soilpH", soil.getSoilPH());
            s.put("organicMatter", soil.getOrganicMatter());
            s.put("soilQuality", soil.getSoilQuality());

            String soilType = soil.getType();
            if ("ForestSoil".equals(soilType)) {
                ForestSoil fs = (ForestSoil) soil;
                s.put("leafLitter", fs.getLeafLitter());
            } else if ("SwampSoil".equals(soilType)) {
                SwampSoil ss = (SwampSoil) soil;
                s.put("waterLogging", ss.getWaterLogging());
            } else if ("DesertSoil".equals(soilType)) {
                DesertSoil ds = (DesertSoil) soil;
                s.put("salinity", ds.getSalinity());
            } else if ("GrasslandSoil".equals(soilType)) {
                GrasslandSoil gs = (GrasslandSoil) soil;
                s.put("rootDensity", gs.getRootDensity());
            } else if ("TundraSoil".equals(soilType)) {
                TundraSoil ts = (TundraSoil) soil;
                s.put("permafrostDepth", ts.getPermafrostDepth());
            }

            out.set("soil", s);
        }

        Plant plant = cell.getPlant();
        if (plant != null) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", plant.getType());
            p.put("name", plant.getName());
            p.put("mass", plant.getMass());
            out.set("plants", p);
        }

        Animal animal = cell.getAnimal();
        if (animal != null) {
            ObjectNode a = mapper.createObjectNode();
            a.put("type", animal.getType());
            a.put("name", animal.getName());
            a.put("mass", animal.getMass());
            out.set("animals", a);
        }

        Water water = cell.getWater();
        if (water != null) {
            ObjectNode w = mapper.createObjectNode();
            w.put("type", water.getType());
            w.put("name", water.getName());
            w.put("mass", water.getMass());
            out.set("water", w);
        }

        int currentTimestamp = ctx.getCommandInput().getTimestamp();

        Air air = cell.getAir();
        if (air != null) {
            ObjectNode a = mapper.createObjectNode();
            a.put("type", air.getType());
            a.put("name", air.getName());
            a.put("mass", air.getMass());
            a.put("humidity", air.getHumidity());
            a.put("temperature", air.getTemperature());
            a.put("oxygenLevel", air.getOxygenLevel());
            //a.put("airQuality", air.getAirQuality());

            a.put("airQuality", air.getAirQuality(currentTimestamp));
            String airType = air.getType();
            if ("TropicalAir".equals(airType)) {
                TropicalAir ta = (TropicalAir) air;
                double roundedCo2 =
                        Math.round(ta.getCo2Level() * 100.0) / 100.0;
                a.put("co2Level", roundedCo2);
            } else if ("TemperateAir".equals(airType)) {
                TemperateAir tmp = (TemperateAir) air;
                a.put("pollenLevel", tmp.getPollenLevel());
            } else if ("DesertAir".equals(airType)) {
                DesertAir da = (DesertAir) air;
                //a.put("dustParticles", da.getDustParticles());
                a.put("desertStorm", da.isDesertStormActive(currentTimestamp));
            } else if ("PolarAir".equals(airType)) {
                PolarAir pa = (PolarAir) air;
                a.put("iceCrystalConcentration", pa.getIceCrystalConcentration());
            } else if ("MountainAir".equals(airType)) {
                MountainAir ma = (MountainAir) air;
                a.put("altitude", ma.getAltitude());
            }

            out.set("air", a);
        }

        result.set("output", out);
    }
}
