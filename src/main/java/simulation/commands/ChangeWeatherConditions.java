package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.TerraBot;
import simulation.map.SimulationMap;
import simulation.map.MapCell;

import java.util.HashMap;
import java.util.Map;

/**
 * handles the modification of weather parameters within the simulation map
 * based on a specific input event
 * it is responsible for checking simulation state and robot status before applying
 * the relevant weather strategy across the map
 */
public final class ChangeWeatherConditions implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_ROBOT_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_EFFECT =
            "ERROR: The weather change does not affect the environment. Cannot perform action";
    private static final String MSG_AFFECTED = "The weather has changed.";

    /**
     * concrete strategy for Desert Storm events
     */
    private static final class DesertStormStrategy extends AbstractWeatherStrategy {
        DesertStormStrategy() {
            super("DesertAir");
        }
    }

    /**
     * concrete strategy for People Hiking events
     */
    private static final class PeopleHikingStrategy extends AbstractWeatherStrategy {
        PeopleHikingStrategy() {
            super("MountainAir");
        }
    }

    /**
     * concrete strategy for New Season events
     */
    private static final class NewSeasonStrategy extends AbstractWeatherStrategy {
        NewSeasonStrategy() {
            super("TemperateAir");
        }
    }

    /**
     * concrete strategy for Polar Storm events
     */
    private static final class PolarStormStrategy extends AbstractWeatherStrategy {
        PolarStormStrategy() {
            super("PolarAir");
        }
    }

    /**
     * concrete strategy for Rainfall events
     */
    private static final class RainfallStrategy extends AbstractWeatherStrategy {
        RainfallStrategy() {
            super("TropicalAir");
        }
    }

    private final Map<String, WeatherStrategy> strategyMap;

    /**
     * constructs a new ChangeWeatherConditions command initializing the map
     * that links command types to their corresponding concrete strategies
     */
    public ChangeWeatherConditions() {
        strategyMap = new HashMap<>();
        strategyMap.put("desertStorm", new DesertStormStrategy());
        strategyMap.put("peopleHiking", new PeopleHikingStrategy());
        strategyMap.put("newSeason", new NewSeasonStrategy());
        strategyMap.put("polarStorm", new PolarStormStrategy());
        strategyMap.put("rainfall", new RainfallStrategy());
    }

    /**
     * calculates the intensity value of the weather event from the input fields
     * @param input the command input object containing weather parameters
     * @return the intensity value or 0.0 if none specified
     */
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

    /**
     * executes the weather change command
     * it validates the simulation state and robot status
     * selects the appropriate weather strategy
     * calculates intensity and applies the effect across the simulation map
     * @param ctx the context object containing the simulation state and command input
     */
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

        WeatherStrategy strategy = strategyMap.get(weatherType);

        if (strategy == null) {
            result.put("message", ERR_NO_EFFECT);
            return;
        }

        double intensity = getWeatherIntensity(input);
        boolean affected = false;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                if (strategy.apply(cell, input, intensity)) {
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
}
