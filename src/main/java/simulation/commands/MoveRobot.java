package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.core.TerraBot;
import simulation.map.Coordinates;
import simulation.map.MapCell;
import simulation.map.SimulationMap;

public class MoveRobot implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_NO_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String SUCCESS_MSG =
            "The robot has successfully moved to position (%d, %d).";

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        Simulation sim = ctx.getSimulation();
        TerraBot robot = sim.getBot();
        SimulationMap map = sim.getMap();
        Coordinates currentPos = robot.getPosition();

        int[][] directions = {
                {0, 1},  // Jos
                {1, 0},  // Dreapta
                {0, -1}, // Sus
                {-1, 0}  // Stânga
        };

        int bestX = -1;
        int bestY = -1;

        int bestScore = Integer.MAX_VALUE;

        boolean foundMove = false;

        for (int[] dir : directions) {
            int nextX = currentPos.getX() + dir[0];
            int nextY = currentPos.getY() + dir[1];

            if (isValid(nextX, nextY, map)) {
                MapCell cell = map.getCell(nextX, nextY);

                int currentScore = calculateSafetyScore(cell);

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
            result.put("message", "ERROR: Cannot move.");
        }
    }

    private boolean isValid(int x, int y, SimulationMap map) {
        return x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight();
    }

    private int calculateSafetyScore(MapCell cell) {
        double soilRisk = cell.getSoil().getStuckProbability();
        double airRisk = cell.getAir().getDamageProbability();

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