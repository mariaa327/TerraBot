// simulation/commands/WeatherStrategy.java

package simulation.commands;

import simulation.map.MapCell;
import fileio.CommandInput;

/**
 * Interface defining the contract for applying a specific weather event.
 */
public interface WeatherStrategy {

    /**
     * @return the required air type string.
     */
    String getTargetAirType();

    /**
     * Attempts to apply the defined weather modification logic to the air entity
     * within the given map cell
     * @param cell the map cell where the action is performed
     * @param input the command input object containing weather details
     * @param intensity the calculated intensity value of the weather event
     * @return {@code true} if the weather event was successfully applied to the cell
     * false otherwise
     */
    boolean apply(MapCell cell, CommandInput input, double intensity);
}
