package server.maze.game.wrappers;

import server.maze.game.model.MazeExecutor;

import java.util.List;

public class ApiBoardWrapper {

    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiBoardWrapper makeFromGame(MazeExecutor game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.boardHeight = game.getMazeHeight();
        wrapper.boardWidth = game.getMazeWidth();
        wrapper.hasWalls = game.getHasWalls();
        wrapper.isVisible = game.getIsVisible();
        wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.getMouse());
        wrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheese());
        wrapper.catLocations = ApiLocationWrapper.makeFromCellLocations(game.getCats());
        return wrapper;
    }
}
