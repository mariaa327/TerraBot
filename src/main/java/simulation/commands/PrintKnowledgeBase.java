package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.core.ScientificTopic;
import simulation.core.TerraBot;
import java.util.List;

public class PrintKnowledgeBase implements Command {

    private static final String ERR_NOT_STARTED = "ERROR: Simulation not started. Cannot perform action";

    @Override
    public void execute(CommanContext ctx) {
        ObjectNode result = ctx.getResultNode();

        if (!ctx.isSimulationStarted() || ctx.getSimulation() == null) {
            result.put("message", ERR_NOT_STARTED);
            return;
        }

        TerraBot robot = ctx.getSimulation().getBot();
        List<ScientificTopic> topics = robot.getKnowledgeBase();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode outputArray = mapper.createArrayNode();

        for (ScientificTopic topic : topics) {

            if (topic.hasFacts()) {
                ObjectNode topicNode = mapper.createObjectNode();

                topicNode.put("topic", topic.getName());

                ArrayNode factsArray = mapper.createArrayNode();
                for (String f : topic.getFacts()) {
                    factsArray.add(f);
                }
                topicNode.set("facts", factsArray);

                outputArray.add(topicNode);
            }
        }

        result.set("output", outputArray);
    }
}