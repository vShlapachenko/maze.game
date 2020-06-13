package server.maze.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Moves cats and mouses (implements action received from user and implements cat pseudo-AI)
 * handles collisions (decides if game is won or not)
 * implements all user-given commands
 * sets Players onto the maze
 */

public class MazeExecutor {


    private final static int GAME_LOST = -3;
    private final static int INCORRECT_INPUT = -2;
    private final static int MOVE_INTO_WALL = -1;
    private final static int RESULT_SUCCESSFUL = 0;
    private final static int LEFT = 1;
    private final static int UP = 2;
    private final static int RIGHT = 3;
    private final static int DOWN = 4;
    private final static int SET_CHEESE_TO_COLLECT_TO_ONE = 5;
    private final static int REVEAL_MAP = 6;
    private final static int GET_HELP = 7;
    private final static int GAME_WON = 8;
    private boolean catCheeseCollisionPreviousRound = false;
    private int numberOfCheesesCollected = 0;
    private int numberOfCheesesToBeCollected = 5;

    boolean mazeWasRevealedWithCheat = false;
    boolean gameWon = false;
    boolean gameLost = false;

    MazeCreator createdMaze = new MazeCreator();
    MazeCell[][] maze;
    Mouse mouse = new Mouse();
    Cheese cheese;
    List<Cat> cats = new ArrayList<>();

    public MazeCell[][] getMaze() {
        return maze;
    }

    public int getMazeHeight() {
        return createdMaze.getMazeHeight();
    }

    public int getMazeWidth() {
        return createdMaze.getMazeWidth();
    }

    public MazeCell getMouse() {
        return mouse;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public Cheese getCheese() {
        return cheese;
    }

    public boolean[][] getHasWalls() {
        boolean[][] hasWallsMatrix = new boolean[createdMaze.getMazeHeight()][createdMaze.getMazeWidth()];
        for (int i = 0; i < maze.length; i++) {
            for (int k = 0; k < maze[i].length; k++) {
                hasWallsMatrix[i][k] = !(maze[i][k].isCanStepOn());
            }
        }
        return hasWallsMatrix;
    }

    public boolean[][] getIsVisible() {
        boolean[][] isVisibleMatrix = new boolean[createdMaze.getMazeHeight()][createdMaze.getMazeWidth()];
        for (int i = 0; i < maze.length; i++) {
            for (int k = 0; k < maze[i].length; k++) {
                isVisibleMatrix[i][k] = maze[i][k].isCellDiscovered();
            }
        }
        return isVisibleMatrix;
    }

    public int getNumberOfCheesesCollected() {
        return numberOfCheesesCollected;
    }

    public int getNumberOfCheesesToBeCollected() {
        return numberOfCheesesToBeCollected;
    }

    public MazeCell[][] createMazeWithPlayers() {
        askForMaze();
        spawnMouse();
        spawnCheese();
        spawnCats();
        updateSight();
        handleCatCheeseCollision();
        return maze;
    }

    public void setCheeseToOne() {
        numberOfCheesesToBeCollected = 1;
    }

    private void prepareMatrixForEndGame() {
        for (MazeCell[] mazeCells : maze) {
            for (MazeCell mazeCell : mazeCells) {
                mazeCell.setCellDiscovered(true);
            }
        }
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public void revealMap() {
        for (MazeCell[] mazeCells : maze) {
            for (MazeCell mazeCell : mazeCells) {
                mazeCell.setCellDiscovered(true);
            }
        }
        mazeWasRevealedWithCheat = true;
    }

    private void spawnMouse() {
        maze[1][1] = mouse;
    }

    private void spawnCheese() {
        for (int i = 1; i > 0; ) {
            int randomXPosition = getRandomNum(maze[0].length - 2);
            int randomYPosition = getRandomNum(maze.length - 2);
            if (maze[randomYPosition][randomXPosition].isCanStepOn() && !(maze[randomYPosition][randomXPosition] instanceof Mouse)) {
                cheese = new Cheese(randomYPosition, randomXPosition);
                maze[randomYPosition][randomXPosition] = cheese;
                i--;
            }
        }
    }

    private int getRandomNum(int rangeEnd) {
        double x = (int) (Math.random() * ((rangeEnd - 1) + 1)) + 1;
        return (int) x;
    }

    private void spawnCats() {
        cats.add(new Cat(18, 1));
        maze[1][18] = cats.get(0);
        cats.add(new Cat(1, 13));
        maze[13][1] = cats.get(1);
        cats.add(new Cat(18, 13));
        maze[13][18] = cats.get(2);
    }

    public boolean checkGameWon() {
        return numberOfCheesesCollected >= numberOfCheesesToBeCollected;
    }

    public boolean checkMouseCheeseCollision() {
        if (mouse.getCoordinateY() == cheese.getCoordinateY()
                && mouse.getCoordinateX() == cheese.getCoordinateX()) {
            numberOfCheesesCollected++;
            spawnCheese();
            return true;
        } else {
            return false;
        }

    }

    public void moveAllCats() {
        for (Cat cat : cats) {
            int move = getRandomMove(cat);
            moveCat(move, cat);
        }
    }

    private void handleCatCheeseCollision() {
        if (catCheeseCollisionPreviousRound && !checkCatCheeseCollision()) {
            maze[cheese.getCoordinateY()][cheese.getCoordinateX()] = cheese;
            catCheeseCollisionPreviousRound = false;
        }
        if (checkCatCheeseCollision()) {
            catCheeseCollisionPreviousRound = true;
        }
    }

    private boolean checkCatCheeseCollision() {
        boolean collisionHappened = false;
        for (Cat cat : cats) {
            if (cat.getCoordinateX() == cheese.getCoordinateX() && cat.getCoordinateY() == cheese.getCoordinateY()) {
                collisionHappened = true;
            }
        }
        return collisionHappened;
    }

    public boolean checkGameLost() {
        for (Cat cat : cats) {
            if (cat.getCoordinateX() == mouse.getCoordinateX() && cat.getCoordinateY() == mouse.getCoordinateY()) {
                return true;
            }
        }
        return false;
    }

    private void moveCat(int direction, Cat cat) {
        if (cat.isPreviousCellDiscovered() || mazeWasRevealedWithCheat) {
            maze[cat.getCoordinateY()][cat.getCoordinateX()] = new MazeCell(true, true, true);
        } else {
            maze[cat.getCoordinateY()][cat.getCoordinateX()] = new MazeCell(true, false, true);
        }
        cat.move(direction);
        if (maze[cat.getCoordinateY()][cat.getCoordinateX()].isCellDiscovered()) {
            cat.setPreviousCellDiscovered(true);
        } else {
            cat.setPreviousCellDiscovered(false);
        }
        maze[cat.getCoordinateY()][cat.getCoordinateX()] = cat;
    }

    private int getRandomMove(Cat cat) {
        List<Integer> possibleMoves = IntStream.range(LEFT, DOWN + 1)
                .filter(side -> checkSide(side, cat.coordinate))
                .filter(side -> isNotDoubleBack(side, cat))
                .boxed()
                .collect(Collectors.toList());
        if (possibleMoves.isEmpty()) {
            possibleMoves = IntStream.range(LEFT, DOWN + 1)
                    .filter(side -> checkSide(side, cat.coordinate))
                    .boxed()
                    .collect(Collectors.toList());
        }
        Collections.shuffle(possibleMoves);
        cat.setPrevMove(possibleMoves.get(0));
        return possibleMoves.get(0);
    }

    private boolean isNotDoubleBack(int direction, Cat cat) {
        int backMove = cat.getPrevMove();
        switch (backMove) {
            case LEFT:
                backMove = RIGHT;
                break;
            case RIGHT:
                backMove = LEFT;
                break;
            case UP:
                backMove = DOWN;
                break;
            case DOWN:
                backMove = UP;
                break;
            default:
                backMove = 0;
                break;
        }
        return backMove != direction;
    }

    private boolean checkSide(int side, Coordinate coordinate) {
        if (side == 1) {
            return maze[coordinate.getY()][coordinate.getX() - 1].isCanStepOn();
        }
        if (side == 2) {
            return maze[coordinate.getY() - 1][coordinate.getX()].isCanStepOn();
        }
        if (side == 3) {
            return maze[coordinate.getY()][coordinate.getX() + 1].isCanStepOn();
        }
        if (side == 4) {
            return maze[coordinate.getY() + 1][coordinate.getX()].isCanStepOn();
        }
        return false;
    }

    public void mouseMove(int direction) {
        maze[mouse.getCoordinateY()][mouse.getCoordinateX()] = new MazeCell(true, true, true);
        mouse.move(direction);
        maze[mouse.getCoordinateY()][mouse.getCoordinateX()] = mouse;
        updateSight();
    }

    private void updateSight() {
        int coordinateX = mouse.getCoordinateX();
        int coordinateY = mouse.getCoordinateY();
        maze[coordinateY - 1][coordinateX - 1].setCellDiscovered(true);
        maze[coordinateY - 1][coordinateX].setCellDiscovered(true);
        maze[coordinateY - 1][coordinateX + 1].setCellDiscovered(true);
        maze[coordinateY][coordinateX + 1].setCellDiscovered(true);
        maze[coordinateY + 1][coordinateX + 1].setCellDiscovered(true);
        maze[coordinateY + 1][coordinateX].setCellDiscovered(true);
        maze[coordinateY + 1][coordinateX - 1].setCellDiscovered(true);
        maze[coordinateY][coordinateX - 1].setCellDiscovered(true);
    }

    public boolean incorrectDirection(int direction) {
        if (direction == LEFT) {
            if (!maze[mouse.getCoordinateY()][mouse.getCoordinateX() - 1].isCanStepOn()) {
                return true;
            }
        }
        if (direction == UP) {
            if (!maze[mouse.getCoordinateY() - 1][mouse.getCoordinateX()].isCanStepOn()) {
                return true;
            }
        }
        if (direction == RIGHT) {
            if (!maze[mouse.getCoordinateY()][mouse.getCoordinateX() + 1].isCanStepOn()) {
                return true;
            }
        }
        if (direction == DOWN) {
            if (!maze[mouse.getCoordinateY() + 1][mouse.getCoordinateX()].isCanStepOn()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void askForMaze() {
        createdMaze.createMaze();
        maze = createdMaze.getMaze();
    }

}
