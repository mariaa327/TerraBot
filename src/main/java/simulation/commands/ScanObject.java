package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.entities.Entity;
import simulation.map.Coordinates;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

import java.util.ArrayList;
import java.util.List;

/**
 * the command executes a scan operation at the TerraBot's current
 * location to identify and record an entity based on sensory input
 */
public final class ScanObject implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_NO_ENERGY =
            "ERROR: Not enough energy to perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_OBJ_NOT_FOUND =
            "ERROR: Object not found. Cannot perform action";

    private static final int SCAN_COST = 7;

    private interface ScanStrategy {
        boolean matches(String color, String smell, String sound);
        Entity getEntity(MapCell cell);
        String getMessageType();
    }

    private static final class PlantScanStrategy implements ScanStrategy {
        @Override
        public boolean matches(final String color, final String smell, final String sound) {
            if (!"none".equalsIgnoreCase(color) && !"none".equalsIgnoreCase(smell)
                    && "none".equalsIgnoreCase(sound)) {
                return true;
            }
            return false;
        }
        @Override
        public Entity getEntity(final MapCell cell) {
            return cell.getPlant();
        }
        @Override
        public String getMessageType() {
            return "a plant";
        }
    }

    private static final class AnimalScanStrategy implements ScanStrategy {
        @Override
        public boolean matches(final String color, final String smell, final String sound) {
            if (!"none".equalsIgnoreCase(color) && !"none".equalsIgnoreCase(smell)
                    && !"none".equalsIgnoreCase(sound)) {
                return true;
            }
            return false;
        }
        @Override
        public Entity getEntity(final MapCell cell) {
            return cell.getAnimal();
        }
        @Override
        public String getMessageType() {
            return "an animal";
        }
    }

    private static final class WaterScanStrategy implements ScanStrategy {
        @Override
        public boolean matches(final String color, final String smell, final String sound) {
            if ("none".equalsIgnoreCase(color) && "none".equalsIgnoreCase(smell)
                    && "none".equalsIgnoreCase(sound)) {
                return true;
            }
            return false;
        }
        @Override
        public Entity getEntity(final MapCell cell) {
            return cell.getWater();
        }
        @Override
        public String getMessageType() {
            return "water";
        }
    }

    private static final List<ScanStrategy> ALL_STRATEGIES;

    static {
        ALL_STRATEGIES = new ArrayList<>();
        ALL_STRATEGIES.add(new PlantScanStrategy());
        ALL_STRATEGIES.add(new AnimalScanStrategy());
        ALL_STRATEGIES.add(new WaterScanStrategy());
    }

    /**
     * executes the scan object command
     * it verifies preconditions (energy, charging status)
     * finds the matching scan strategy based on input and attempts to scan the
     * corresponding entity in the current cell
     * @param ctx the context object containing the simulation state
     * @return void for errors
     */
    @Override
    public void execute(final CommanContext ctx) {
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
        SimulationMap map = sim.getMap();
        Coordinates pos = robot.getPosition();
        MapCell cell = map.getCell(pos.getX(), pos.getY());

        String color = input.getColor();
        String smell = input.getSmell();
        String sound = input.getSound();

        Entity entityToScan = null;
        String messageType = null;
        ScanStrategy foundStrategy = null;

        for (ScanStrategy strategy : ALL_STRATEGIES) {
            if (strategy.matches(color, smell, sound)) {
                foundStrategy = strategy;
                break;
            }
        }

        if (foundStrategy == null) {
            result.put("message", ERR_OBJ_NOT_FOUND);
            return;
        }

        entityToScan = foundStrategy.getEntity(cell);
        messageType = foundStrategy.getMessageType();

        if (entityToScan != null) {
            entityToScan.scan();
            robot.addScannedEntity(entityToScan);
            robot.consumeEnergy(SCAN_COST);
            result.put("message", "The scanned object is " + messageType + ".");
        } else {
            result.put("message", ERR_OBJ_NOT_FOUND);
        }
    }
}
