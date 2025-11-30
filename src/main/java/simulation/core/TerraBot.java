package simulation.core;

import simulation.entities.Entity;
import simulation.map.Coordinates;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TerraBot {

    public abstract static class AbstractKnowledge {
        protected String name;

        public AbstractKnowledge(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void addInfo(String info);
        public abstract List<String> getInfo();
        public abstract boolean hasInfo();
    }

    private static class TopicKnowledge extends AbstractKnowledge {
        private List<String> facts;

        public TopicKnowledge(String name) {
            super(name);
            this.facts = new ArrayList<>();
        }

        @Override
        public void addInfo(String info) {
            this.facts.add(info);
        }

        @Override
        public List<String> getInfo() {
            return this.facts;
        }

        @Override
        public boolean hasInfo() {
            return !this.facts.isEmpty();
        }
    }

    private Coordinates position;
    private int energy;
    private List<Entity> inventory;
    private Set<Coordinates> visited;
    private int busyUntil = -1;
    private List<AbstractKnowledge> knowledgeBase;

    public TerraBot(Coordinates startPosition, int startEnergy) {
        this.position = startPosition;
        this.energy = startEnergy;
        this.inventory = new ArrayList<>();
        this.visited = new HashSet<>();
        this.knowledgeBase = new ArrayList<>();
        this.visited.add(startPosition);
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
        this.visited.add(position);
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void consumeEnergy(int amount) {
        this.energy -= amount;
    }

    public void recharge(int amount, int currentTimestamp) {
        this.energy += amount;
        this.busyUntil = currentTimestamp + amount;
    }

    public boolean isCharging(int currentTimestamp) {
        if (busyUntil != -1 && currentTimestamp < busyUntil) {
            return true;
        } else {
            return false;
        }
    }

    public List<Entity> getInventory() {
        return inventory;
    }

    public Entity getEntityFromInventory(String name) {
        for (Entity e : inventory) {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }

    public void addScannedEntity(Entity entity) {
        this.inventory.add(entity);

        boolean exists = false;

        for (int i = 0; i < this.knowledgeBase.size(); i++) {
            AbstractKnowledge k = this.knowledgeBase.get(i);
            if (k.getName().equals(entity.getName())) {
                exists = true;
            }
        }

        if (exists == false) {
            TopicKnowledge newTopic = new TopicKnowledge(entity.getName());
            this.knowledgeBase.add(newTopic);
        }
    }

    public boolean hasScannedEntity(String name) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addFact(String entityName, String fact) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(entityName)) {
                k.addInfo(fact);
                return;
            }
        }
    }

    public boolean hasFact(String entityName, String requiredFact) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(entityName)) {
                return k.getInfo().contains(requiredFact);
            }
        }
        return false;
    }

    public List<AbstractKnowledge> getKnowledgeBase() {
        return knowledgeBase;
    }
}