package simulation.map;

public class SimulationMap {
    private final int width;
    private final int height;
    private final MapCell[][] grid;

    public SimulationMap(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new MapCell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new MapCell();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MapCell getCell(int x, int y) {
        return grid[y][x];
    }

    public MapCell getCell(Coordinates c) {
        return getCell(c.getX(), c.getY());
    }
}
