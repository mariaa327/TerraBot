package simulation.core;

public class EnvMap {

    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    public EnvMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}
