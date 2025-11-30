package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.TerraBot;
import simulation.entities.Entity;
import simulation.entities.air.Air;
import simulation.entities.soil.Soil;
import simulation.map.MapCell;

public class ImproveEnvironment implements Command {

    private static final String ERR_NOT_STARTED = "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING = "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_BATTERY = "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_SUBJECT_NOT_SAVED = "ERROR: Subject not yet saved. Cannot perform action";
    private static final String ERR_FACT_NOT_SAVED = "ERROR: Fact not yet saved. Cannot perform action";
    private static final String ERR_UNKNOWN_TYPE = "ERROR: Unknown improvement type.";

    private abstract class Improvement {
        abstract boolean apply(MapCell cell);
        abstract String getFact(String name);
        abstract String getMsg(String name);
    }

    private class Planting extends Improvement {
        @Override
        String getFact(String name) {
            return "Method to plant " + name;
        }

        @Override
        String getMsg(String name) {
            return "The " + name + " was planted successfully.";
        }

        @Override
        boolean apply(MapCell cell) {
            if (cell.getAir() != null) {
                double val = cell.getAir().getOxygenLevel() + 0.3;
                cell.getAir().setOxygenLevel(Math.round(val * 100.0) / 100.0);
                return true;
            }
            return false;
        }
    }

    private class Fertilizing extends Improvement {
        @Override
        String getFact(String name) {
            return "Method to fertilize soil with " + name;
        }

        @Override
        String getMsg(String name) {
            return "The soil was successfully fertilized using " + name + ".";
        }

        @Override
        boolean apply(MapCell cell) {
            if (cell.getSoil() != null) {
                double val = cell.getSoil().getOrganicMatter() + 0.3;
                cell.getSoil().setOrganicMatter(Math.round(val * 100.0) / 100.0);
                return true;
            }
            return false;
        }
    }

    private class Humidifying extends Improvement {
        @Override
        String getFact(String name) {
            return "Method to increase humidity with " + name;
        }

        @Override
        String getMsg(String name) {
            return "The humidity was successfully increased using " + name + ".";
        }

        @Override
        boolean apply(MapCell cell) {
            if (cell.getAir() != null) {
                double val = cell.getAir().getHumidity() + 0.2;
                cell.getAir().setHumidity(Math.round(val * 100.0) / 100.0);
                return true;
            }
            return false;
        }
    }

    private class Moistening extends Improvement {
        @Override
        String getFact(String name) {
            return "Method to increase moisture with " + name;
        }

        @Override
        String getMsg(String name) {
            return "The moisture was successfully increased using " + name + ".";
        }

        @Override
        boolean apply(MapCell cell) {
            if (cell.getSoil() != null) {
                double val = cell.getSoil().getWaterRetention() + 0.2;
                cell.getSoil().setWaterRetention(Math.round(val * 100.0) / 100.0);
                return true;
            }
            return false;
        }
    }

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        TerraBot robot = ctx.getSimulation().getBot();
        CommandInput input = ctx.getCommandInput();

        if (robot.isCharging(input.getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        if (robot.getEnergy() < 10) {
            result.put("message", ERR_NO_BATTERY);
            return;
        }

        String type = input.getImprovementType();
        Improvement act = null;

        switch (type) {
            case "plantVegetation":
                act = new Planting();
                break;
            case "fertilizeSoil":
                act = new Fertilizing();
                break;
            case "increaseHumidity":
                act = new Humidifying();
                break;
            case "increaseMoisture":
                act = new Moistening();
                break;
            default:
                result.put("message", ERR_UNKNOWN_TYPE);
                return;
        }

        String entityName = input.getName();

        if (!robot.hasScannedEntity(entityName)) {
            result.put("message", ERR_SUBJECT_NOT_SAVED);
            return;
        }

        String reqFact = act.getFact(entityName);
        if (!robot.hasFact(entityName, reqFact)) {
            result.put("message", ERR_FACT_NOT_SAVED);
            return;
        }

        Entity ingredient = robot.getEntityFromInventory(entityName);
        if (ingredient == null) {
            result.put("message", ERR_SUBJECT_NOT_SAVED);
            return;
        }

        MapCell currentCell = ctx.getSimulation().getMap().getCell(robot.getPosition());

        boolean success = act.apply(currentCell);

        if (success) {
            robot.consumeEnergy(10);
            result.put("message", act.getMsg(entityName));
        } else {
            result.put("message", "ERROR: Cannot improve environment here (missing soil/air).");
        }
    }
}