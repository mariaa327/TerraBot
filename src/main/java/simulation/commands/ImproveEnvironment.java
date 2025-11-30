package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.TerraBot;
import simulation.entities.Entity;
import simulation.map.MapCell;

public final class ImproveEnvironment implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_SUBJECT_NOT_SAVED =
            "ERROR: Subject not yet saved. Cannot perform action";
    private static final String ERR_FACT_NOT_SAVED =
            "ERROR: Fact not yet saved. Cannot perform action";
    private static final String ERR_UNKNOWN_TYPE =
            "ERROR: Unknown improvement type.";
    private static final String ERR_MISSING_ENTITY =
            "ERROR: Cannot improve environment here (missing soil/air).";

    private static final int ENERGY_COST = 10;
    private static final double PLANT_OXYGEN_INCREASE = 0.3;
    private static final double FERTILIZER_ORGANIC_MATTER_INCREASE = 0.3;
    private static final double HUMIDITY_INCREASE = 0.2;
    private static final double MOISTURE_INCREASE = 0.2;
    private static final double PRECISION_FACTOR = 100.0;
    private static final double MIN_ENERGY_REQUIRED = 10;

    private abstract static class Improvement {
        abstract String getFact(String name);
        abstract String getMsg(String name);
        abstract boolean apply(MapCell cell);
    }

    private static final class Planting extends Improvement {
        @Override
        String getFact(final String name) {
            return "Method to plant " + name;
        }

        @Override
        String getMsg(final String name) {
            return "The " + name + " was planted successfully.";
        }

        @Override
        boolean apply(final MapCell cell) {
            if (cell.getAir() != null) {
                double val = cell.getAir().getOxygenLevel() + PLANT_OXYGEN_INCREASE;
                val = Math.round(val * PRECISION_FACTOR) / PRECISION_FACTOR;
                cell.getAir().setOxygenLevel(val);
                return true;
            }
            return false;
        }
    }

    private static final class Fertilizing extends Improvement {
        @Override
        String getFact(final String name) {
            return "Method to fertilize soil with " + name;
        }

        @Override
        String getMsg(final String name) {
            return "The soil was successfully fertilized using " + name + ".";
        }

        @Override
        boolean apply(final MapCell cell) {
            if (cell.getSoil() != null) {
                double val = cell.getSoil().getOrganicMatter() + FERTILIZER_ORGANIC_MATTER_INCREASE;
                val = Math.round(val * PRECISION_FACTOR) / PRECISION_FACTOR;
                cell.getSoil().setOrganicMatter(val);
                return true;
            }
            return false;
        }
    }

    private static final class Humidifying extends Improvement {
        @Override
        String getFact(final String name) {
            return "Method to increase humidity with " + name;
        }

        @Override
        String getMsg(final String name) { // FIX: Parameter final
            return "The humidity was successfully increased using " + name + ".";
        }

        @Override
        boolean apply(final MapCell cell) {
            if (cell.getAir() != null) {
                double val = cell.getAir().getHumidity() + HUMIDITY_INCREASE;
                val = Math.round(val * PRECISION_FACTOR) / PRECISION_FACTOR;
                cell.getAir().setHumidity(val);
                return true;
            }
            return false;
        }
    }

    private static final class Moistening extends Improvement {
        @Override
        String getFact(final String name) {
            return "Method to increase moisture with " + name;
        }

        @Override
        String getMsg(final String name) { // FIX: Parameter final
            return "The moisture was successfully increased using " + name + ".";
        }

        @Override
        boolean apply(final MapCell cell) {
            if (cell.getSoil() != null) {
                double val = cell.getSoil().getWaterRetention() + MOISTURE_INCREASE;
                val = Math.round(val * PRECISION_FACTOR) / PRECISION_FACTOR;
                cell.getSoil().setWaterRetention(val);
                return true;
            }
            return false;
        }
    }

    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        TerraBot robot = ctx.getSimulation().getBot();
        final CommandInput input = ctx.getCommandInput();

        if (robot.isCharging(input.getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        if (robot.getEnergy() < MIN_ENERGY_REQUIRED) {
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
            robot.consumeEnergy(ENERGY_COST);
            result.put("message", act.getMsg(entityName));
        } else {
            result.put("message", ERR_MISSING_ENTITY);
        }
    }
}
