package simulation.entities;

import fileio.AirInput;
import simulation.core.WorldUpdater;

import simulation.core.Cell;
import simulation.core.SimContext;

public class Air extends BaseEntity {

    public enum AirType {
        TROPICAL,
        POLAR,
        TEMPERATE,
        DESERT,
        MOUNTAIN
    }

    private final AirType type;
    private double humidity;
    private double temperature;
    private double oxygenLevel;
    private double altitude;
    private double pollenLevel;
    private double co2Level;
    private double iceCrystalConcentration;
    private double dustParticles;

    private String eventType;
    private int eventStart;
    private int eventDuration;
    private double rainfall;
    private double windSpeed;
    private boolean desertStorm;
    private String season;
    private int hikers;

    public Air(String name, double mass, AirType type) {
        super(name, mass);
        this.type = type;
        eventDuration = 0;
    }

    public static Air fromInput(AirInput in) {
        AirType t;
        String s = in.getType();
        if ("tropical".equalsIgnoreCase(s)) t = AirType.TROPICAL;
        else if ("polar".equalsIgnoreCase(s)) t = AirType.POLAR;
        else if ("temperate".equalsIgnoreCase(s)) t = AirType.TEMPERATE;
        else if ("dessert".equalsIgnoreCase(s) || "desert".equalsIgnoreCase(s)) t = AirType.DESERT;
        else t = AirType.MOUNTAIN;
        Air a_ = new Air(in.getName(), in.getMass(), t);
        a_.setHumidity(in.getHumidity());
        a_.setTemperature(in.getTemperature());
        a_.setOxygenLevel(in.getOxygenLevel());
        a_.setAltitude(in.getAltitude());
        a_.setPollenLevel(in.getPollenLevel());
        a_.setCo2Level(in.getCo2Level());
        a_.setIceCrystalConcentration(in.getIceCrystalConcentration());
        a_.setDustParticles(in.getDustParticles());
        return a_;
    }

    public AirType getType() {
        return type;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getPollenLevel() {
        return pollenLevel;
    }

    public void setPollenLevel(double pollenLevel) {
        this.pollenLevel = pollenLevel;
    }

    public double getCo2Level() {
        return co2Level;
    }

    public void setCo2Level(double co2Level) {
        this.co2Level = co2Level;
    }

    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }

    public void setIceCrystalConcentration(double iceCrystalConcentration) {
        this.iceCrystalConcentration = iceCrystalConcentration;
    }

    public double getDustParticles() {
        return dustParticles;
    }

    public void setDustParticles(double dustParticles) {
        this.dustParticles = dustParticles;
    }

    public boolean isDesertStorm() {
        return desertStorm;
    }

    public void startEvent(String eventType, int currentTime, double rainfall, double windSpeed,
                           boolean desertStorm, String season, int hikers) {
        this.eventType = eventType;
        this.eventStart = currentTime;
        this.eventDuration = 2;
        this.rainfall = rainfall;
        this.windSpeed = windSpeed;
        this.desertStorm = desertStorm;
        this.season = season;
        this.hikers = hikers;
    }

    public double baseQuality() {
        double score = 0;
        switch (type) {
            case TROPICAL:
                score = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
                break;
            case POLAR:
                score = (oxygenLevel * 2) + (100 - Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
                break;
            case TEMPERATE:
                score = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.1);
                break;
            case DESERT:
                score = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);
                break;
            case MOUNTAIN:
                double oxygenFactor = oxygenLevel - (altitude / 1000.0 * 0.5);
                score = (oxygenFactor * 2) + (humidity * 0.6);
                break;
            default:
                break;
        }
        return WorldUpdater.normalizeRound100(score);
    }



    public void decreaseEvent() {
        if (eventDuration > 0) {
            eventDuration--;
            if (eventDuration == 0) desertStorm = false;
        }
    }

    public double maxScore() {
        switch (type) {
            case TROPICAL:
                return 82;
            case POLAR:
                return 142;
            case TEMPERATE:
                return 84;
            case DESERT:
                return 65;
            case MOUNTAIN:
                return 78;
            default:
                return 100;
        }
    }

    public double toxicity() {
        double airQuality = baseQuality();
        double mq = maxScore();
        double toxicityAQ = 100 * (1 - airQuality / mq);
        return WorldUpdater.round2(toxicityAQ);
    }

}
