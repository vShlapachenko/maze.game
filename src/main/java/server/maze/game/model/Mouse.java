package server.maze.game.model;

/**
 * Represents a Mouse in a maze
 */

public class Mouse extends MazeCell {

    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;

    public Mouse() {
        super(true, true, false);
    }

    private Coordinate coordinate= new Coordinate(1,1);

    public int getCoordinateX() {
        return coordinate.getX();
    }

    public int getCoordinateY(){
        return coordinate.getY();
    }

    public void move(int direction){
        switch (direction){
            case LEFT:
                coordinate.decrementX();
                break;
            case UP:
                coordinate.decrementY();
                break;
            case RIGHT:
                coordinate.incrementX();
                break;
            case DOWN:
                coordinate.incrementY();
                break;
            default:
                System.exit(-1);
        }
    }


}
