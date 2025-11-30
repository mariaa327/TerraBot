package simulation.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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