package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.entities.air.Air;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public class ChangeWeatherConditions implements Command {

    private static final String ERR_NOT_STARTED = "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_ROBOT_CHARGING = "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_EFFECT = "ERROR: The weather change does not affect the environment. Cannot perform action";

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        TerraBot robot = ctx.getSimulation().getBot();
        if (robot.isCharging(ctx.getCommandInput().getTimestamp())) {
            result.put("message", ERR_ROBOT_CHARGING);
            return;
        }

        Simulation sim = ctx.getSimulation();
        SimulationMap map = sim.getMap();
        CommandInput input = ctx.getCommandInput();
        String weatherType = input.getType();

        boolean affected = false;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                Air air = cell.getAir();

                if (air != null && isAffected(air, weatherType)) {

                    Double val = 0.0;
                    if (input.getRainfall() != 0) {
                        val = input.getRainfall();
                    } else if (input.getWindSpeed() != 0) {
                        val = input.getWindSpeed();
                    } else if (input.getNumberOfHikers() != 0) {
                        val = (double) input.getNumberOfHikers();
                    }

                    air.setWeatherEvent(weatherType, input.getTimestamp(), val, input.getSeason(), input.isDesertStorm());

                    affected = true;
                }
            }
        }

        if (affected) {
            result.put("message", "The weather has changed.");
        } else {
            result.put("message", ERR_NO_EFFECT);
        }
    }

    private boolean isAffected(Air air, String weatherType) {
        String airType = air.getType();

        switch (weatherType) {
            case "desertStorm":
                return "DesertAir".equals(airType);
            case "peopleHiking":
                return "MountainAir".equals(airType);
            case "newSeason":
                return "TemperateAir".equals(airType);
            case "polarStorm":
                return "PolarAir".equals(airType);
            case "rainfall":
                return "TropicalAir".equals(airType);
            default:
                return false;
        }
    }
}