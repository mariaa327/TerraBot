package simulation;

import fileio.AirInput;
import fileio.AnimalInput;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;
import simulation.core.EnvMap;
import simulation.entities.Air;
import simulation.entities.Animal;
import simulation.entities.Plant;
import simulation.entities.Soil;
import simulation.entities.Water;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Initializer {

    private Initializer() {
    }

    public static void fillMap(EnvMap map, TerritorySectionParamsInput t,
                               Map<String, List<Soil>> soilByName,
                               Map<String, List<Air>> airByName,
                               Map<String, List<Plant>> plantByName,
                               Map<String, List<Animal>> animalByName,
                               Map<String, List<Water>> waterByName) {

        if (t.getSoil() != null) {
            for (SoilInput s : t.getSoil()) {
                for (PairInput p : s.getSections()) {
                    Soil buffer = Soil.fromInput(s);
                    map.getCell(p.getX(), p.getY()).setSoil(buffer);
                    soilByName.computeIfAbsent(buffer.getName(), k -> new ArrayList<>()).add(buffer);
                }
            }
        }

        if (t.getAir() != null) {
            for (AirInput s : t.getAir()) {
                for (PairInput p : s.getSections()) {
                    Air buffer = Air.fromInput(s);
                    map.getCell(p.getX(), p.getY()).setAir(buffer);
                    airByName.computeIfAbsent(buffer.getName(), k -> new ArrayList<>()).add(buffer);
                }
            }
        }

        if (t.getPlants() != null) {
            for (PlantInput s : t.getPlants()) {
                for (PairInput p : s.getSections()) {
                    Plant buffer = Plant.fromInput(s);
                    map.getCell(p.getX(), p.getY()).setPlant(buffer);
                    plantByName.computeIfAbsent(buffer.getName(), k -> new ArrayList<>()).add(buffer);
                }
            }
        }

        if (t.getAnimals() != null) {
            for (AnimalInput s : t.getAnimals()) {
                for (PairInput p : s.getSections()) {
                    Animal buffer = Animal.fromInput(s);
                    map.getCell(p.getX(), p.getY()).setAnimal(buffer);
                    animalByName.computeIfAbsent(buffer.getName(), k -> new ArrayList<>()).add(buffer);
                }
            }
        }

        if (t.getWater() != null) {
            for (WaterInput s : t.getWater()) {
                for (PairInput p : s.getSections()) {
                    Water buffer = Water.fromInput(s);
                    map.getCell(p.getX(), p.getY()).setWater(buffer);
                    waterByName.computeIfAbsent(buffer.getName(), k -> new ArrayList<>()).add(buffer);
                }
            }
        }
    }
}
