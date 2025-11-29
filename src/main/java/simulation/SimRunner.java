package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.commands.*;
import simulation.core.Simulation;

import java.util.List;

public class SimRunner {

    private final ObjectMapper mapper;
    private final ArrayNode output;
    private final List<SimulationInput> simulations;
    private final List<CommandInput> commands;

    private int currentSimulationIndex = 0;
    private Simulation currentSimulation = null;
    private boolean simulationStarted = false;

    public SimRunner(ObjectMapper mapper, ArrayNode output, InputLoader loader) {
        this.mapper = mapper;
        this.output = output;
        this.simulations = loader.getSimulations();
        this.commands = loader.getCommands();
    }

    public void run() {

        for (CommandInput cmd : commands) {
            ObjectNode result = mapper.createObjectNode();
            result.put("command", cmd.getCommand());
            result.put("timestamp", cmd.getTimestamp());

            CommanContext ctx = new CommanContext(
                    mapper,
                    result,
                    cmd,
                    currentSimulation,
                    simulationStarted,
                    simulations,
                    currentSimulationIndex
            );

            Command command = null;

            switch (cmd.getCommand()) {
                case "startSimulation":
                    command = new StartSimulation();
                    break;
                case "endSimulation":
                    command = new EndSimulation();
                    break;
                case "printEnvConditions":
                    command = new PrintEnvConditions();
                    break;
                case "printMap":
                    command = new PrintMap();
                    break;
                case "moveRobot":
                    command = new MoveRobot();
                    break;
                case "getEnergyStatus":
                    command = new GetEnergyStatus();
                    break;
                case "rechargeBattery":
                    command = new RechargeBattery();
                    break;
                case "changeWeatherConditions":
                    command = new ChangeWeatherConditions();
                    break;
                default:
                    result.put("message",
                            "ERROR: Simulation not started. Cannot perform action");
            }

            if (command != null) {
                command.execute(ctx);
            }

            currentSimulation = ctx.getSimulation();
            simulationStarted = ctx.isSimulationStarted();
            currentSimulationIndex = ctx.getCurrentSimulationIndex();

            output.add(result);
        }
    }
}