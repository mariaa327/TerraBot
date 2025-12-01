package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.core.TerraBot;

/**
 * the command initiates a charging process for the TerraBot
 * it consumes simulation time equal to the amount specified for charging during which
 * the simulation updates occur without robot movement
 */
public final class RechargeBattery implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";

    /**
     * executes the recharge command
     * checks simulation status and robot charging status
     * initiates the charging sequence updating the simulation for the duration specified
     * @param ctx the context object containing the simulation state
     * @return void for errors
     */
    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();
        int currentTimestamp = ctx.getCommandInput().getTimestamp();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        Simulation sim = ctx.getSimulation();
        TerraBot robot = sim.getBot();

        if (robot.isCharging(currentTimestamp)) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        int amount = ctx.getCommandInput().getTimeToCharge();
        robot.recharge(amount, currentTimestamp);

        for (int i = 0; i < amount; i++) {
            sim.update();
        }

        result.put("message", "Robot battery is charging.");
    }
}
