package server.maze.game.model;

public class Cat extends MazeCell {

    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;

    private int prevMove = -1;

    Coordinate coordinate;
    private boolean previousCellDiscovered = false;

    public void setPrevMove(int prevMove) {
        this.prevMove = prevMove;
    }

    public int getPrevMove() {
        return prevMove;
    }

    public Cat(int x, int y) {
        super(true, true, true);
        coordinate = new Coordinate(x, y);
    }

    public int getCoordinateX() {
        return coordinate.getX();
    }

    public int getCoordinateY(){
        return coordinate.getY();
    }

    public boolean isPreviousCellDiscovered() {
        return previousCellDiscovered;
    }

    public void setPreviousCellDiscovered(boolean previousCellDiscovered) {
        this.previousCellDiscovered = previousCellDiscovered;
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
                System.out.println("There was a mistake in a direction of CAT");
                System.exit(-1);
        }
    }
}
