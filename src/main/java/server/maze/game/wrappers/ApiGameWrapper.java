package server.maze.game.wrappers;

import server.maze.game.model.MazeExecutor;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiGameWrapper makeFromGame(MazeExecutor game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.isGameWon = game.isGameWon();
        wrapper.isGameLost = game.isGameLost();
        wrapper.numCheeseFound = game.getNumberOfCheesesCollected();
        wrapper.numCheeseGoal = game.getNumberOfCheesesToBeCollected();
        return wrapper;
    }
}
