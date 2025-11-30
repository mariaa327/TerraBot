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

    public ObjectMapper getMapper() {
        return mapper;
    }

    public ObjectNode getResultNode() {
        return resultNode;
    }

    public CommandInput getCommandInput() {
        return commandInput;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(final Simulation simulation) {
        this.simulation = simulation;
    }

    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    public void setSimulationStarted(final boolean simulationStarted) {
        this.simulationStarted = simulationStarted;
    }

    public List<SimulationInput> getSimulations() {
        return simulations;
    }

    public int getCurrentSimulationIndex() {
        return currentSimulationIndex;
    }

    public void incrementSimulationIndex() {
        this.currentSimulationIndex++;
    }
}
