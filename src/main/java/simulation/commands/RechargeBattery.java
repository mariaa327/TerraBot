package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.Simulation;
import simulation.core.TerraBot;

public final class RechargeBattery implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";

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
