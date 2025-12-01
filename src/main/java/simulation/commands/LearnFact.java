package simulation.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.core.TerraBot;

/**
 * the command enables the TerraBot to store a new fact
 * about an entity that has been previously scanned
 * it consumes a small amount of energy and records the fact in the robot's database
 * provided the subject entity has already been identified and saved
 */
public final class LearnFact implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";
    private static final String ERR_STILL_CHARGING =
            "ERROR: Robot still charging. Cannot perform action";
    private static final String ERR_NO_BATTERY =
            "ERROR: Not enough battery left. Cannot perform action";
    private static final String ERR_SUBJECT_NOT_SAVED =
            "ERROR: Subject not yet saved. Cannot perform action";
    private static final String SUCCESS_MSG =
            "The fact has been successfully saved in the database.";

    /**
     * executes the learn fact command
     * it verifies the simulation status, robot energy level
     * and confirms that the subject entity has been previously scanned before saving the fact
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

        TerraBot robot = ctx.getSimulation().getBot();
        CommandInput input = ctx.getCommandInput();

        if (robot.isCharging(input.getTimestamp())) {
            result.put("message", ERR_STILL_CHARGING);
            return;
        }

        if (robot.getEnergy() < 2) {
            result.put("message", ERR_NO_BATTERY);
            return;
        }

        String entityName = input.getComponents();
        String fact = input.getSubject();

        if (robot.hasScannedEntity(entityName)) {
            robot.addFact(entityName, fact);
            robot.consumeEnergy(2);
            result.put("message", SUCCESS_MSG);
        } else {
            result.put("message", ERR_SUBJECT_NOT_SAVED);
        }
    }
}
