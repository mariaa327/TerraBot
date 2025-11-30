package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.TerraBot;
import simulation.entities.air.Air;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

import java.util.HashMap;
import java.util.Map;

public final class ChangeWeatherConditions implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_ROBOT_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_EFFECT =
            "ERROR: The weather change does not affect the environment. Cannot perform action";
    private static final String MSG_AFFECTED = "The weather has changed.";

    private final Map<String, String> weatherRules;

    public ChangeWeatherConditions() {
        weatherRules = new HashMap<>();
        weatherRules.put("desertStorm", "DesertAir");
        weatherRules.put("peopleHiking", "MountainAir");
        weatherRules.put("newSeason", "TemperateAir");
        weatherRules.put("polarStorm", "PolarAir");
        weatherRules.put("rainfall", "TropicalAir");
    }

    @Override
    public void execute(final CommanContext ctx) {
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

        final CommandInput input = ctx.getCommandInput();
        SimulationMap map = ctx.getSimulation().getMap();
        String weatherType = input.getType();

        if (!weatherRules.containsKey(weatherType)) {
            result.put("message", ERR_NO_EFFECT);
            return;
        }

        String targetAirType = weatherRules.get(weatherType);

        double intensity = getWeatherIntensity(input);

        boolean affected = false;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                Air air = cell.getAir();

                if (applyWeatherToCell(cell, targetAirType, input, intensity)) {
                    affected = true;
                }
            }
        }

        if (affected) {
            result.put("message", MSG_AFFECTED);
        } else {
            result.put("message", ERR_NO_EFFECT);
        }
    }

    private double getWeatherIntensity(final CommandInput input) {
        if (input.getRainfall() != 0) {
            return input.getRainfall();
        }
        if (input.getWindSpeed() != 0) {
            return input.getWindSpeed();
        }
        if (input.getNumberOfHikers() != 0) {
            return (double) input.getNumberOfHikers();
        }
        return 0.0;
    }

    private boolean applyWeatherToCell(final MapCell cell, final String targetType,
                                       final CommandInput input, final double val) {
        Air air = cell.getAir();

        if (air == null || !targetType.equals(air.getType())) {
            return false;
        }

        air.setWeatherEvent(
                input.getType(),
                input.getTimestamp(),
                val,
                input.getSeason(),
                input.isDesertStorm()
        );

        return true;
    }
}
