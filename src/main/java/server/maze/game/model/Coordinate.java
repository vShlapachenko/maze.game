package server.maze.game.model;

/**
 * coordinate class used to keep track where players should be on the field (to double-check)
 */

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void incrementX(){
        this.x++;
    }

    public void incrementY(){
        this.y++;
    }

    public void decrementX(){
        this.x--;
    }

    public void decrementY(){
        this.y--;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
