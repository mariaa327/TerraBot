package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * the command terminates the currently running simulation
 * it resets the simulation state within the context to null and updates the
 * {@code simulationStarted} flag to false
 */
public final class EndSimulation implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";

    /**
     * executes the end simulation command
     * it checks if a simulation is active
     * resets the simulation instance in the context and sets the status to ended
     * @param ctx the context object containing the simulation state
     */
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
