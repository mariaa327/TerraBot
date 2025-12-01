package simulation.commands;

public interface Command {
    /**
     * @param ctx command context - simulation state and necessary objects
     */
    void execute(CommanContext ctx);
}
