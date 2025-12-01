package simulation.core;

import simulation.entities.Entity;
import simulation.map.Coordinates;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * the {@code TerraBot} class represents the main simulation agent
 * it manages the robot's physical state (position, energy) inventory of scanned entities,
 * track of visited coordinates and its knowledge base of learned facts
 */
public final class TerraBot {

    /**
     * the abstract base class for knowledge topics stored in the robot's database
     * it defines the core methods for identifying the topic and managing associated information
     */
    public abstract static class AbstractKnowledge {
        protected String name;

        /**
         * @param name the name of the knowledge topic
         */
        public AbstractKnowledge(final String name) {
            this.name = name;
        }

        /**
         * @return the name of the knowledge topic
         */
        public String getName() {
            return name;
        }

        /**
         * adds an informational string to the topic's knowledge
         * @param info the fact to add
         */
        public abstract void addInfo(String info);

        /**
         * @return the list of facts associated with the topic
         */
        public abstract List<String> getInfo();

        /**
         * @return true if the topic has any stored information
         */
        public abstract boolean hasInfo();
    }

    /**
     * a concrete implementation of {@code AbstractKnowledge} used to store a list of facts
     * related to a specific topic
     */
    private static final class TopicKnowledge extends AbstractKnowledge {
        private List<String> facts;

        /**
         * @param name the name of the topic
         */
        TopicKnowledge(final String name) {
            super(name);
            this.facts = new ArrayList<>();
        }

        @Override
        public void addInfo(final String info) {
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
    private final List<Entity> inventory;
    private final Set<Coordinates> visited;
    private int busyUntil = -1;
    private final List<AbstractKnowledge> knowledgeBase;

    /**
     * constructs a new instance
     * @param startPosition the initial coordinates of the robot
     * @param startEnergy   the initial energy level
     */
    public TerraBot(final Coordinates startPosition, final int startEnergy) {
        this.position = startPosition;
        this.energy = startEnergy;
        this.inventory = new ArrayList<>();
        this.visited = new HashSet<>();
        this.knowledgeBase = new ArrayList<>();
        this.visited.add(startPosition);
    }

    /**
     * @return the current position of the robot
     */
    public Coordinates getPosition() {
        return position;
    }

    /**
     * sets the new position of the robot and adds the coordinates to the visited set
     * @param position the new coordinates
     */
    public void setPosition(final Coordinates position) {
        this.position = position;
        this.visited.add(position);
    }

    /**
     * @return the robot's current energy level
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * sets the robot's energy level
     * @param energy the new energy level
     */
    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    /**
     * reduces the robot's energy by the specified amount
     * @param amount the energy amount to consume
     */
    public void consumeEnergy(final int amount) {
        this.energy -= amount;
    }

    /**
     * recharges the robot's battery over a period of time setting the timestamp until it is busy
     * @param amount the energy amount to add
     * @param currentTimestamp the current time used to calculate the busy end time
     */
    public void recharge(final int amount, final int currentTimestamp) {
        this.energy += amount;
        this.busyUntil = currentTimestamp + amount;
    }

    /**
     * checks if the robot is currently busy charging
     * @param currentTimestamp the current time
     * @return true if the robot is busy charging
     */
    public boolean isCharging(final int currentTimestamp) {
        if (busyUntil != -1) {
            if (currentTimestamp < busyUntil) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the robot's inventory of scanned entities
     */
    public List<Entity> getInventory() {
        return inventory;
    }

    /**
     * retrieves an entity from the inventory by its name
     * @param name the name of the entity to retrieve
     * @return the entity object if found, or null
     */
    public Entity getEntityFromInventory(final String name) {
        for (Entity e : inventory) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * adds a newly scanned entity to the robot's inventory and updates the knowledge base
     * by creating a new topic if the entity's name is not already present
     * @param entity the entity object to add
     */
    public void addScannedEntity(final Entity entity) {
        this.inventory.add(entity);

        boolean exists = false;

        for (int i = 0; i < this.knowledgeBase.size(); i++) {
            AbstractKnowledge k = this.knowledgeBase.get(i);
            if (k.getName().equals(entity.getName())) {
                exists = true;
            }
        }

        if (!exists) {
            TopicKnowledge newTopic = new TopicKnowledge(entity.getName());
            this.knowledgeBase.add(newTopic);
        }
    }

    /**
     * checks if a topic corresponding to the entity name has been added to the knowledge base
     * @param name the name of the entity
     * @return true if the entity has been scanned
     */
    public boolean hasScannedEntity(final String name) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * adds a new fact to the knowledge topic corresponding to the given entity name
     * @param entityName the name of the entity/topic
     * @param fact the fact string to add
     */
    public void addFact(final String entityName, final String fact) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(entityName)) {
                k.addInfo(fact);
                return;
            }
        }
    }

    /**
     * checks if a specific fact is already stored under the given entity topic
     * @param entityName the name of the entity/topic
     * @param requiredFact the fact string to check for
     * @return true if the fact is present
     */
    public boolean hasFact(final String entityName, final String requiredFact) {
        for (AbstractKnowledge k : knowledgeBase) {
            if (k.getName().equals(entityName)) {
                return k.getInfo().contains(requiredFact);
            }
        }
        return false;
    }

    /**
     * @return the list representing the robot's complete knowledge base
     */
    public List<AbstractKnowledge> getKnowledgeBase() {
        return knowledgeBase;
    }
}
