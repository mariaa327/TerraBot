package simulation.core;

import simulation.entities.Air;
import simulation.entities.Animal;
import simulation.entities.Plant;
import simulation.entities.Soil;
import simulation.entities.Water;

import java.util.List;
import java.util.Map;

public class SimContext {

    private final EnvMap map;
    private final Robot robot;
    private final Map<String, List<Soil>> soilByName;
    private final Map<String, List<Air>> airByName;
    private final Map<String, List<Plant>> plantByName;
    private final Map<String, List<Animal>> animalByName;
    private final Map<String, List<Water>> waterByName;

    public SimContext(EnvMap map, Robot robot,
                      Map<String, List<Soil>> soilByName,
                      Map<String, List<Air>> airByName,
                      Map<String, List<Plant>> plantByName,
                      Map<String, List<Animal>> animalByName,
                      Map<String, List<Water>> waterByName) {
        this.map = map;
        this.robot = robot;
        this.soilByName = soilByName;
        this.airByName = airByName;
        this.plantByName = plantByName;
        this.animalByName = animalByName;
        this.waterByName = waterByName;
    }

    public EnvMap getMap() {
        return map;
    }

    public Robot getRobot() {
        return robot;
    }


}
