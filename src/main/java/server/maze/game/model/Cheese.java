package server.maze.game.model;

/**
 * Represents cheese
 */

public class Cheese extends MazeCell {
    Coordinate coordinate;

    public Cheese(int y, int x) {
        super(true, true, true);
        coordinate = new Coordinate(x, y);
    }

    public int getCoordinateX() {
        return coordinate.getX();
    }

    public int getCoordinateY(){
        return coordinate.getY();
    }

}
