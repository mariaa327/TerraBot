package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.entities.air.Air;
import simulation.entities.soil.Soil;
import simulation.map.MapCell;
import simulation.map.SimulationMap;
import simulation.core.TerraBot;

public final class PrintMap implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String DEFAULT_QUALITY_POOR = "poor";

    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();
        ObjectMapper mapper = ctx.getMapper();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        int currentTimestamp = ctx.getCommandInput().getTimestamp();
        TerraBot robot = ctx.getSimulation().getBot();
        if (robot.isCharging(ctx.getCommandInput().getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        Simulation sim = ctx.getSimulation();
        SimulationMap map = sim.getMap();
        ArrayNode outArray = mapper.createArrayNode();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);
                ObjectNode cellNode = createCellNodeData(cell, x, y, mapper, currentTimestamp);
                outArray.add(cellNode);
            }
        }

        result.set("output", outArray);
    }

    /**
     * creates the JSON node for a single cell using polymorphism to retrieve
     * the quality categories
     * @param cell the map cell
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param mapper Jackson ObjectMapper
     * @param currentTimestamp the current timestamp
     * @return the JSON node containing the cell data
     */
    private ObjectNode createCellNodeData(final MapCell cell, final int x, final int y,
                                          final ObjectMapper mapper, final int currentTimestamp) {

        ObjectNode cellNode = mapper.createObjectNode();

        ArrayNode section = mapper.createArrayNode();
        section.add(x);
        section.add(y);
        cellNode.set("section", section);

        cellNode.put("totalNrOfObjects", cell.getOptionalObjectsCount());

        Air air = cell.getAir();
        Soil soil = cell.getSoil();

        if (air != null) {
            air.putQualityStatus(cellNode, currentTimestamp);
        } else {
            cellNode.put("airQuality", DEFAULT_QUALITY_POOR);
        }

        if (soil != null) {
            soil.putQualityStatus(cellNode);
        } else {
            cellNode.put("soilQuality", DEFAULT_QUALITY_POOR);
        }

        return cellNode;
    }
}
