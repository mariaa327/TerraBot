package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.map.Coordinates;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

/**
 * the command calculates the safest adjacent cell for the TerraBot
 * to move to based on an aggregated safety score and executes
 * the movement if the robot has sufficient energy
 * the safety score is derived from risks associated with soil,
 * air and entities like animals and plants
 */
public final class MoveRobot implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_NO_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String SUCCESS_MSG =
            "The robot has successfully moved to position (%d, %d).";
    private static final String ERR_CANNOT_MOVE = "ERROR: Cannot move.";

    /**
     * executes the move robot command
     * it checks simulation and robot status
     * calculates the optimal neighboring position with the lowest safety score
     * and performs the move if the robot has enough energy
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
        SimulationMap map = sim.getMap();
        Coordinates currentPos = robot.getPosition();

        int currentTimestamp = ctx.getCommandInput().getTimestamp();

        if (robot.isCharging(ctx.getCommandInput().getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        int[][] directions = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, 0}
        };

        int bestX = -1;
        int bestY = -1;
        int bestScore = Integer.MAX_VALUE;
        boolean foundMove = false;

        for (int[] dir : directions) {
            int nextX = currentPos.getX() + dir[0];
            int nextY = currentPos.getY() + dir[1];

            if (nextX >= 0 && nextX < map.getWidth() && nextY >= 0 && nextY < map.getHeight()) {
                MapCell cell = map.getCell(nextX, nextY);
                int currentScore = calculateSafetyScore(cell, currentTimestamp);
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestX = nextX;
                    bestY = nextY;
                    foundMove = true;
                }
            }
        }

        if (foundMove) {
            if (robot.getEnergy() >= bestScore) {
                robot.setPosition(new Coordinates(bestX, bestY));
                robot.consumeEnergy(bestScore);
                result.put("message", String.format(SUCCESS_MSG, bestX, bestY));
            } else {
                result.put("message", ERR_NO_BATTERY);
            }
        } else {
            result.put("message", ERR_CANNOT_MOVE);
        }
    }

    /**
     * calculates the safety score for a given map cell
     * the score is the rounded mean of all applicable risk probabilities
     * @param cell the map cell whose safety score is calculated
     * @param currentTimestamp the current simulation time needed for time-dependent
     *                         air calculations
     * @return the calculated safety score rounded to the nearest integer
     */
    private int calculateSafetyScore(final MapCell cell, final int currentTimestamp) {
        double soilRisk = cell.getSoil().getStuckProbability();
        double airRisk = cell.getAir().getDamageProbability(currentTimestamp);

        double sum = soilRisk + airRisk;
        double count = 2.0;

        if (cell.getAnimal() != null) {
            sum += cell.getAnimal().getAttackProbability();
            count++;
        }

        if (cell.getPlant() != null) {
            sum += cell.getPlant().getStuckProbability();
            count++;
        }

        double mean = Math.abs(sum / count);
        return (int) Math.round(mean);
    }
}
