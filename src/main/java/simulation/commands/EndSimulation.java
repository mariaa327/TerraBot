package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class EndSimulation implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";

    @Override
    public void execute(final CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        ctx.setSimulation(null);
        ctx.setSimulationStarted(false);

        result.put("message", "Simulation has ended.");
    }
}
