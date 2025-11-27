package simulation.entities;

import fileio.AnimalInput;

public class Animal extends BaseEntity {

    public enum Kind {
        HERBIVORE,
        CARNIVORE,
        OMNIVORE,
        DETRITIVORE,
        PARASITE
    }

    public enum State {
        HUNGRY,
        WELL_FED,
        SICK
    }

    private final Kind type;
    private State state;
    private boolean scanned;
    private int lastMove;
    private int lastFeed;

    public Animal(String name, double mass, Kind type) {
        super(name, mass);
        this.type = type;
        this.state = State.HUNGRY;
        this.scanned = false;
        this.lastMove = 0;
        this.lastFeed = 0;
    }

    public static Animal fromInput(AnimalInput in) {
        Kind k;
        String t = in.getType();
        if ("Herbivores".equalsIgnoreCase(t) || "Herbivore".equalsIgnoreCase(t)) k = Kind.HERBIVORE;
        else if ("Carnivores".equalsIgnoreCase(t) || "Carnivore".equalsIgnoreCase(t)) k = Kind.CARNIVORE;
        else if ("Omnivores".equalsIgnoreCase(t) || "Omnivore".equalsIgnoreCase(t)) k = Kind.OMNIVORE;
        else if ("Detritivores".equalsIgnoreCase(t)) k = Kind.DETRITIVORE;
        else k = Kind.PARASITE;
        return new Animal(in.getName(), in.getMass(), k);
    }

    public Kind getType() {
        return type;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public int getLastMove() {
        return lastMove;
    }

    public void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    public int getLastFeed() {
        return lastFeed;
    }

    public void setLastFeed(int lastFeed) {
        this.lastFeed = lastFeed;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        this.state = s;
    }

    public double attackProbabilityScore() {
        double p;
        switch (type) {
            case HERBIVORE:
                p = 85;
                break;
            case CARNIVORE:
                p = 30;
                break;
            case OMNIVORE:
                p = 60;
                break;
            case DETRITIVORE:
                p = 90;
                break;
            case PARASITE:
                p = 10;
                break;
            default:
                p = 50;
        }
        return (100.0 - p) / 10.0;
    }

}
