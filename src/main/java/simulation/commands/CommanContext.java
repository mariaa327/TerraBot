package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.SimulationInput;
import simulation.core.Simulation;

import java.util.List;

public final class CommanContext {

    private final ObjectMapper mapper;
    private final ObjectNode resultNode;
    private final CommandInput commandInput;
    private Simulation simulation;
    private boolean simulationStarted;
    private final List<SimulationInput> simulations;
    private int currentSimulationIndex;

    /**
     * constructs a new command context object, initializing it with the current simulation state
     * @param mapper the Jackson object mapper utility
     * @param resultNode the JSON object node used to build the command's output result
     * @param commandInput the current command input received from the file
     * @param simulation the current running simulation instance (can be null)
     * @param simulationStarted flag indicating whether a simulation is actively running
     * @param simulations the full list of simulation inputs to be executed
     * @param currentSimulationIndex the index of the simulation currently being run
     */
    public CommanContext(final ObjectMapper mapper, final ObjectNode resultNode,
                         final CommandInput commandInput,
                         final Simulation simulation, final boolean simulationStarted,
                         final List<SimulationInput> simulations,
                         final int currentSimulationIndex) {
        this.mapper = mapper;
        this.resultNode = resultNode;
        this.commandInput = commandInput;
        this.simulation = simulation;
        this.simulationStarted = simulationStarted;
        this.simulations = simulations;
        this.currentSimulationIndex = currentSimulationIndex;
    }

    /**
     * @return the Jackson object mapper utility
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * @return the JSON object node for command output
     */
    public ObjectNode getResultNode() {
        return resultNode;
    }

    /**
     * @return the current command input
     */
    public CommandInput getCommandInput() {
        return commandInput;
    }

    /**
     * @return the current running simulation instance
     */
    public Simulation getSimulation() {
        return simulation;
    }

    /**
     * @param simulation the simulation instance to set
     */
    public void setSimulation(final Simulation simulation) {
        this.simulation = simulation;
    }

    /**
     * @return true if a simulation is actively running
     */
    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    /**
     * @param simulationStarted the simulation status to set
     */
    public void setSimulationStarted(final boolean simulationStarted) {
        this.simulationStarted = simulationStarted;
    }

    /**
     * @return the full list of simulation inputs
     */
    public List<SimulationInput> getSimulations() {
        return simulations;
    }

    /**
     * @return the index of the simulation currently being run
     */
    public int getCurrentSimulationIndex() {
        return currentSimulationIndex;
    }

    /**
     * increments the index to point to the next simulation in the list
     */
    public void incrementSimulationIndex() {
        this.currentSimulationIndex++;
    }
}
