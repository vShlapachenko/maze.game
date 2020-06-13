package server.maze.game.model;

/**
 * Parent class for all elements of maze (matrix consists of this class)
 * acts like a path-cell in a maze
 */

public class MazeCell {

    private final boolean canStepOn;
    private boolean cellDiscovered;
    private boolean visited;

    public MazeCell(boolean canStepOn, boolean cellDiscovered, boolean visited) {
        this.canStepOn = canStepOn;
        this.cellDiscovered = cellDiscovered;
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isCanStepOn() {
        return canStepOn;
    }

    public boolean isCellDiscovered() {
        return cellDiscovered;
    }

    public void setCellDiscovered(boolean cellDiscovered) {
        this.cellDiscovered = cellDiscovered;
    }
}
