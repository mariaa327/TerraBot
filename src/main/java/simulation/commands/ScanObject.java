package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.entities.Entity;
import simulation.map.Coordinates;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public class ScanObject implements Command {

    private static final String ERR_NOT_STARTED = "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_NO_ENERGY = "ERROR: Not enough energy to perform action";
    private static final String ERR_STILL_CHARGING = "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_OBJ_NOT_FOUND = "ERROR: Object not found. Cannot perform action";

    private static final int SCAN_COST = 7;

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

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

        if (robot.getEnergy() < SCAN_COST) {
            result.put("message", ERR_NO_ENERGY);
            return;
        }

        CommandInput input = ctx.getCommandInput();
        String detectedType = identifyType(
                input.getColor(),
                input.getSmell(),
                input.getSound()
        );

        if (detectedType == null) {
            result.put("message", ERR_OBJ_NOT_FOUND);
            return;
        }

        SimulationMap map = sim.getMap();
        Coordinates pos = robot.getPosition();
        MapCell cell = map.getCell(pos.getX(), pos.getY());

        Entity entityToScan = null;
        String messageType = "";

        switch (detectedType) {
            case "plant":
                if (cell.getPlant() != null) {
                    entityToScan = cell.getPlant();
                    messageType = "a plant";
                }
                break;
            case "animal":
                if (cell.getAnimal() != null) {
                    entityToScan = cell.getAnimal();
                    messageType = "an animal";
                }
                break;
            case "water":
                if (cell.getWater() != null) {
                    entityToScan = cell.getWater();
                    messageType = "water";
                }
                break;
            default:
                break;
        }

        if (entityToScan != null) {
            entityToScan.scan();
            robot.consumeEnergy(SCAN_COST);
            result.put("message", "The scanned object is " + messageType + ".");
        } else {
            result.put("message", ERR_OBJ_NOT_FOUND);
        }
    }

    private String identifyType(String color, String smell, String sound) {
        if ("none".equalsIgnoreCase(color) && "none".equalsIgnoreCase(smell) && "none".equalsIgnoreCase(sound)) {
            return "water";
        }

        if (!"none".equalsIgnoreCase(color) && !"none".equalsIgnoreCase(smell) && "none".equalsIgnoreCase(sound)) {
            return "plant";
        }

        if (!"none".equalsIgnoreCase(color) && !"none".equalsIgnoreCase(smell) && !"none".equalsIgnoreCase(sound)) {
            return "animal";
        }
        return null;
    }
}
