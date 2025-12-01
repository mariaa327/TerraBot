package simulation.commands;

import simulation.entities.air.Air;
import simulation.map.MapCell;
import fileio.CommandInput;

/**
 * base abstract class for common weather strategies
 */
public abstract class AbstractWeatherStrategy implements WeatherStrategy {

    protected final String requiredAirType;

    /**
     * constructs the AbstractWeatherStrategy initializing the target air type
     * @param requiredAirType the type of air that must be present
     *                        for the strategy to apply its effect
     */
    protected AbstractWeatherStrategy(final String requiredAirType) {
        this.requiredAirType = requiredAirType;
    }

    /**
     * retrieves the specific type of air entity
     * that this strategy is intended to affect
     * @return the required air type string
     */
    @Override
    public String getTargetAirType() {
        return requiredAirType;
    }

    /**
     * implements the core logic to apply the weather modification
     * @param cell the map cell where the action is performed
     * @param input the command input object containing weather details
     * @param intensity the calculated intensity value of the weather event
     * @return {@code true} if the weather event was successfully applied to the cell
     * false otherwise.
     */
    @Override
    public boolean apply(final MapCell cell, final CommandInput input, final double intensity) {
        Air air = cell.getAir();

        if (air == null || !requiredAirType.equals(air.getType())) {
            return false;
        }

        air.setWeatherEvent(
                input.getType(),
                input.getTimestamp(),
                intensity,
                input.getSeason(),
                input.isDesertStorm()
        );
        return true;
    }
}
