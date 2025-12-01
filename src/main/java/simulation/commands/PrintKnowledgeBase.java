package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.TerraBot;

import java.util.List;

/**
 * the command retrieves and serializes all knowledge stored in the TerraBot's database
 * it outputs the knowledge base as a list of topics, each containing an array of associated facts
 */
public final class PrintKnowledgeBase implements Command {

    private static final String ERR_NOT_STARTED =
            "ERROR: Simulation not started. Cannot perform action";

    /**
     * executes the print knowledge base command
     * it checks if the simulation is started retrieves the TerraBot's knowledge base
     * and converts the stored information into a JSON array structure for output
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
        List<TerraBot.AbstractKnowledge> list = robot.getKnowledgeBase();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode finalOutput = mapper.createArrayNode();

        for (int i = 0; i < list.size(); i++) {
            TerraBot.AbstractKnowledge elem = list.get(i);

            if (elem.hasInfo()) {
                ObjectNode obj = mapper.createObjectNode();
                obj.put("topic", elem.getName());
                ArrayNode arr = mapper.createArrayNode();
                List<String> infoList = elem.getInfo();

                for (int j = 0; j < infoList.size(); j++) {
                    arr.add(infoList.get(j));
                }

                obj.set("facts", arr);
                finalOutput.add(obj);
            }
        }
        result.set("output", finalOutput);
    }
}
