package simulation.core;

import java.util.ArrayList;
import java.util.List;

public class ScientificTopic {
    private String name;
    private List<String> facts;

    public ScientificTopic(String name) {
        this.name = name;
        this.facts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getFacts() {
        return facts;
    }

    public void addFact(String fact) {
        this.facts.add(fact);
    }

    public boolean hasFacts() {
        return !facts.isEmpty();
    }
}