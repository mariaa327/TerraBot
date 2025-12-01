package simulation.map;

/**
 * a final immutable pair of (x, y) coordinates for locations on the simulation map
 */
public final class Coordinates {
    private int x;
    private int y;

    /**
     * constructs a new Coordinates object
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * gets the x-coordinate
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * gets the y-coordinate
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
