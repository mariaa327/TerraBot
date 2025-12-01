package simulation.map;

/**
 * represents the entire grid map for the simulation
 * it manages the dimensions of the map and provides access to individual objects
 */
public final class SimulationMap {
    private final int width;
    private final int height;
    private final MapCell[][] grid;

    /**
     * constructs a new SimulationMap with the specified dimensions
     * the grid is initialized with empty mapcell objects
     * @param width the width of the map
     * @param height the height of the map
     */
    public SimulationMap(final int width, final int height) {
        this.width = width;
        this.height = height;
        grid = new MapCell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new MapCell();
            }
        }
    }

    /**
     * gets the width of the map
     * @return the map width
     */
    public int getWidth() {
        return width;
    }

    /**
     * gets the height of the map
     * @return the map height
     */
    public int getHeight() {
        return height;
    }

    /**
     * retrieves the {@link MapCell} at the specified (x, y) coordinates
     * the internal array access uses grid[y][x]
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the {@link MapCell} at the location
     */
    public MapCell getCell(final int x, final int y) {
        return grid[y][x];
    }

    /**
     * retrieves the {@link MapCell} using a {@link Coordinates} object
     * @param c the coordinates of the cell
     * @return the {@link MapCell} at the coordinates
     */
    public MapCell getCell(final Coordinates c) {
        return getCell(c.getX(), c.getY());
    }
}
