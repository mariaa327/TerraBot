package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.InputLoader;
import simulation.SimRunner;

import java.io.File;
import java.io.IOException;

public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER =
            MAPPER.writer().withDefaultPrettyPrinter();

    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        // 1. Load full input (all simulations + all commands)
        InputLoader inputLoader = new InputLoader(inputPath);

        // 2. Prepare output JSON array
        ArrayNode output = MAPPER.createArrayNode();

        // 3. Run your simulation logic ONCE for the whole file
        SimRunner runner = new SimRunner(MAPPER, output, inputLoader);
        runner.run();

        // 4. Write result
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
