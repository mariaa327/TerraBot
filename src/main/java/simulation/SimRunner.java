package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.commands.Command;
import simulation.commands.CommanContext;
import simulation.core.Simulation;
import simulation.commands.StartSimulation;
import simulation.commands.EndSimulation;
import simulation.commands.PrintEnvConditions;
import simulation.commands.PrintMap;
import simulation.commands.MoveRobot;
import simulation.commands.GetEnergyStatus;
import simulation.commands.RechargeBattery;
import simulation.commands.ChangeWeatherConditions;
import simulation.commands.ScanObject;
import simulation.commands.LearnFact;
import simulation.commands.PrintKnowledgeBase;
import simulation.commands.ImproveEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * the SimRunner class is responsible for orchestrating the execution of
 * a series of simulation commands over a list of defined simulations
 */
public final class SimRunner {

    private final ObjectMapper mapper;
    private final ArrayNode output;
    private final List<SimulationInput> simulations;
    private final List<CommandInput> commands;

    private int currentSimulationIndex = 0;
    private Simulation currentSimulation = null;
    private boolean simulationStarted = false;

    private static final String ERROR_COMMAND_NOT_FOUND =
            "ERROR: Command not recognized. Cannot perform action";

    /**
     * the command registry map instantiated for each SimRunner object
     * it maps command strings to their corresponding Command instances
     */
    private final Map<String, Command> commandRegistry;

    /**
     * constructs a new SimRunner.
     * @param mapper for creating JSON nodes
     * @param output to collect command results
     * @param loader containing the simulation and command definitions
     */
    public SimRunner(final ObjectMapper mapper, final ArrayNode output, final InputLoader loader) {
        this.mapper = mapper;
        this.output = output;
        this.simulations = loader.getSimulations();
        this.commands = loader.getCommands();
        this.commandRegistry = new HashMap<>();
        this.commandRegistry.put("startSimulation", new StartSimulation());
        this.commandRegistry.put("endSimulation", new EndSimulation());
        this.commandRegistry.put("printEnvConditions", new PrintEnvConditions());
        this.commandRegistry.put("printMap", new PrintMap());
        this.commandRegistry.put("moveRobot", new MoveRobot());
        this.commandRegistry.put("getEnergyStatus", new GetEnergyStatus());
        this.commandRegistry.put("rechargeBattery", new RechargeBattery());
        this.commandRegistry.put("changeWeatherConditions", new ChangeWeatherConditions());
        this.commandRegistry.put("scanObject", new ScanObject());
        this.commandRegistry.put("learnFact", new LearnFact());
        this.commandRegistry.put("printKnowledgeBase", new PrintKnowledgeBase());
        this.commandRegistry.put("improveEnvironment", new ImproveEnvironment());
    }

    /**
     * executes the simulation commands sequentially
     */
    public void run() {
        int previousTimestamp = 1;

        for (CommandInput cmd : commands) {
            ObjectNode result = mapper.createObjectNode();
            result.put("command", cmd.getCommand());
            result.put("timestamp", cmd.getTimestamp());

            if (currentSimulation != null && simulationStarted) {
                int currentTimestamp = cmd.getTimestamp();
                int steps = currentTimestamp - previousTimestamp;

                for (int i = 0; i < steps; i++) {
                    currentSimulation.update();
                }
            }
            previousTimestamp = cmd.getTimestamp();

            CommanContext ctx = new CommanContext(
                    mapper,
                    result,
                    cmd,
                    currentSimulation,
                    simulationStarted,
                    simulations,
                    currentSimulationIndex
            );

            Command command = this.commandRegistry.get(cmd.getCommand());

            if (command != null) {
                command.execute(ctx);
            } else {
                result.put("message", ERROR_COMMAND_NOT_FOUND);
            }

            currentSimulation = ctx.getSimulation();
            simulationStarted = ctx.isSimulationStarted();
            currentSimulationIndex = ctx.getCurrentSimulationIndex();

            output.add(result);
        }
    }
}
