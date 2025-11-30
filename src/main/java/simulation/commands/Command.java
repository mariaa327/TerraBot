package simulation.commands;

public interface Command {
    /**
     * @param ctx contextul comenzii - starea simularii si obiectele necesare
     */
    void execute(CommanContext ctx);
}
