package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.entities.air.Air;
import simulation.entities.soil.Soil;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public class PrintMap implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();
        ObjectMapper mapper = ctx.getMapper();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        Simulation sim = ctx.getSimulation();
        SimulationMap map = sim.getMap();

        ArrayNode outArray = mapper.createArrayNode();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                MapCell cell = map.getCell(x, y);

                ObjectNode cellNode = mapper.createObjectNode();

                ArrayNode section = mapper.createArrayNode();
                section.add(x);
                section.add(y);
                cellNode.set("section", section);

                cellNode.put("totalNrOfObjects", cell.getOptionalObjectsCount());

                Air air = cell.getAir();
                Soil soil = cell.getSoil();

                String airQuality = (air != null) ? air.getQualityCategory() : "poor";
                String soilQuality = (soil != null) ? soil.getQualityCategory() : "poor";

                cellNode.put("airQuality", airQuality);
                cellNode.put("soilQuality", soilQuality);

                outArray.add(cellNode);
            }
        }

        result.set("output", outArray);
    }
}
