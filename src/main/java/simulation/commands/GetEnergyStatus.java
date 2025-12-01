package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.TerraBot;

/**
 * the command queries the current energy level of the TerraBot
 * it reports the robot's remaining energy points if the simulation is active
 * and the robot is not charging
 */
public final class GetEnergyStatus implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";

    /**
     * executes the energy status command
     * checks simulation status and robot charging status
     * if clear it reports the robot's current energy level
     * @param ctx the context object containing the simulation state
     */
    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        TerraBot robot = ctx.getSimulation().getBot();

        if (robot.isCharging(ctx.getCommandInput().getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        result.put("message", "TerraBot has " + robot.getEnergy() + " energy points left.");
    }
}
