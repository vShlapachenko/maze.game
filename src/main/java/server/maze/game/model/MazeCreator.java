package server.maze.game.model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Creates maze using modified recursive backtracker
 */

public class MazeCreator {

    private int mazeWidth = 20;
    private int mazeHeight = 15;
    private int wallsLeftToRemove = 15;
    private Stack<Coordinate> stack = new Stack<>();
    private Coordinate startCoordinate = new Coordinate(10, 10);

    private MazeCell[][] maze = new MazeCell[mazeHeight][mazeWidth];

    public MazeCell[][] getMaze() {
        return maze;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public int getMazeHeight() {
        return mazeHeight;
    }

    public void createMaze() {
        createOuterWalls();
        while (needReCreateMaze()) {
            createOuterWalls();
            stack.push(startCoordinate);
            createInnerMaze();
        }
        makeLoops();
        clearSpaceForPlayers();
    }

    private void clearSpaceForPlayers() {
        maze[1][1] = new MazeCell(true, true, true);
        if (CreatingEmptySquare(1, 1)) {
            int whichCell = getRandomNum(3);
            switch (whichCell) {
                case 1:
                    maze[1][2] = new Wall();
                    break;
                case 2:
                    maze[2][1] = new Wall();
                    break;
                case 3:
                    maze[2][2] = new Wall();
                    break;
                default:
            }
        }
        maze[13][18] = new MazeCell(true, true, true);
        if (CreatingEmptySquare(13, 18)) {
            int whichCell = getRandomNum(3);
            switch (whichCell) {
                case 1:
                    maze[12][18] = new Wall();
                    break;
                case 2:
                    maze[13][17] = new Wall();
                    break;
                case 3:
                    maze[12][17] = new Wall();
                    break;
                default:
            }
        }
        maze[1][18] = new MazeCell(true, true, true);
        if (CreatingEmptySquare(1, 18)) {
            int whichCell = getRandomNum(3);
            switch (whichCell) {
                case 1:
                    maze[1][17] = new Wall();
                    break;
                case 2:
                    maze[2][17] = new Wall();
                    break;
                case 3:
                    maze[2][18] = new Wall();
                    break;
                default:
            }
        }
        maze[13][1] = new MazeCell(true, true, true);
        if (CreatingEmptySquare(13, 1)) {
            int whichCell = getRandomNum(3);
            switch (whichCell) {
                case 1:
                    maze[12][1] = new Wall();
                    break;
                case 2:
                    maze[12][2] = new Wall();
                    break;
                case 3:
                    maze[13][2] = new Wall();
                    break;
                default:
            }
        }
    }

    private boolean CreatingEmptySquare(int i, int j) {
        if (maze[i + 1][j].isCanStepOn() && maze[i][j + 1].isCanStepOn() && maze[i + 1][j + 1].isCanStepOn() && maze[i][j].isCanStepOn()) {
            return true;
        }
        if (maze[i + 1][j].isCanStepOn() && maze[i][j - 1].isCanStepOn() && maze[i + 1][j - 1].isCanStepOn() && maze[i][j].isCanStepOn()) {
            return true;
        }
        if (maze[i - 1][j].isCanStepOn() && maze[i - 1][j - 1].isCanStepOn() && maze[i][j - 1].isCanStepOn() && maze[i][j].isCanStepOn()) {
            return true;
        }
        if (maze[i][j + 1].isCanStepOn() && maze[i - 1][j + 1].isCanStepOn() && maze[i - 1][j].isCanStepOn() && maze[i][j].isCanStepOn()) {
            return true;
        }
        return false;
    }

    private void makeLoops() {
        int i = 0;
        while (wallsLeftToRemove > 0) {
            i++;
            if (i > 10000) {
                createMaze();
                return;
            }
            int randomXPosition = getRandomNum(mazeWidth - 2);
            int randomYPosition = getRandomNum(mazeHeight - 2);
            if (!maze[randomYPosition][randomXPosition].isCanStepOn() && isCreatingEmptySquareAround(randomYPosition, randomXPosition)) {
                maze[randomYPosition][randomXPosition] = new MazeCell(true, false, true);
                wallsLeftToRemove--;
            }
        }
    }

    private boolean isCreatingEmptySquareAround(int i, int j) {
        if (maze[i + 1][j].isCanStepOn() && maze[i][j + 1].isCanStepOn() && maze[i + 1][j + 1].isCanStepOn()) {
            return false;
        }
        if (maze[i + 1][j].isCanStepOn() && maze[i][j - 1].isCanStepOn() && maze[i + 1][j - 1].isCanStepOn()) {
            return false;
        }
        if (maze[i - 1][j].isCanStepOn() && maze[i - 1][j - 1].isCanStepOn() && maze[i][j - 1].isCanStepOn()) {
            return false;
        }
        if (maze[i][j + 1].isCanStepOn() && maze[i - 1][j + 1].isCanStepOn() && maze[i - 1][j].isCanStepOn()) {
            return false;
        }
        return true;
    }

    private int getRandomNum(int rangeEnd) {
        double x = (int) (Math.random() * ((rangeEnd - 1) + 1)) + 1;
        return (int) x;
    }

    private boolean needReCreateMaze() {
        return cellsNotVisited() || containsSquareWalls() || containsSquareEmptySpace();
    }

    private boolean containsSquareEmptySpace() {
        for (int i = 1; i < maze.length - 1; i++) {
            MazeCell[] mazeCells = maze[i];
            for (int j = 1; j < mazeCells.length - 1; j++) {
                if (maze[i][j].isCanStepOn() && maze[i + 1][j].isCanStepOn() && maze[i][j + 1].isCanStepOn() && maze[i + 1][j + 1].isCanStepOn()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsSquareWalls() {
        for (int i = 1; i < maze.length - 1; i++) {
            MazeCell[] mazeCells = maze[i];
            for (int j = 1; j < mazeCells.length - 1; j++) {
                if (!maze[i][j].isCanStepOn() && !maze[i + 1][j].isCanStepOn() && !maze[i][j + 1].isCanStepOn() && !maze[i + 1][j + 1].isCanStepOn()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean cellsNotVisited() {
        for (MazeCell[] mazeCells : maze) {
            for (MazeCell mazeCell : mazeCells) {
                if (!mazeCell.isVisited()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createInnerMaze() {
        Coordinate currentPosition = stack.peek();
        int side = getRandomSide(currentPosition);
        if (side == 0) {
            maze[currentPosition.getX()][currentPosition.getY()].setVisited(true);
            stack.pop();
            if (stack.empty()) {
                return;
            }
        }
        moveToNextCell(side);
        createInnerMaze();
    }

    private int getRandomSide(Coordinate coordinate) {
        List<Integer> sideList = IntStream.range(1, 5)
                .filter(side -> !isVisited(side, coordinate))
                .filter(side -> canStepOn(side, coordinate))
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(sideList);
        return sideList.stream().findFirst().orElse(0);
    }

    private boolean isVisited(int side, Coordinate coordinate) {
        return checkSide(side, coordinate, MazeCell::isVisited);
    }

    private boolean canStepOn(int side, Coordinate coordinate) {
        return checkSide(side, coordinate, MazeCell::isCanStepOn);
    }

    private boolean checkSide(int side, Coordinate coordinate, Function<MazeCell, Boolean> checkFunction) {
        if (side == 1) {
            return checkFunction.apply(maze[coordinate.getX() - 1][coordinate.getY()]);
        }
        if (side == 2) {
            return checkFunction.apply(maze[coordinate.getX()][coordinate.getY() + 1]);
        }
        if (side == 3) {
            return checkFunction.apply(maze[coordinate.getX() + 1][coordinate.getY()]);
        }
        if (side == 4) {
            return checkFunction.apply(maze[coordinate.getX()][coordinate.getY() - 1]);
        }
        return false;
    }

    private void moveToNextCell(int randomSide) {
        setVisitedTrue();
        if (randomSide == 1) {
            createWallLeftRight();
            Coordinate nextCoordinate = new Coordinate(stack.peek().getX() - 1, stack.peek().getY());
            stack.push(nextCoordinate);
        } else if (randomSide == 2) {
            createWallTopBottom();
            Coordinate nextCoordinate = new Coordinate(stack.peek().getX(), stack.peek().getY() + 1);
            stack.push(nextCoordinate);
        } else if (randomSide == 3) {
            createWallLeftRight();
            Coordinate nextCoordinate = new Coordinate(stack.peek().getX() + 1, stack.peek().getY());
            stack.push(nextCoordinate);
        } else if (randomSide == 4) {
            createWallTopBottom();
            Coordinate nextCoordinate = new Coordinate(stack.peek().getX(), stack.peek().getY() - 1);
            stack.push(nextCoordinate);
        }
    }

    private void setVisitedTrue() {
        maze[stack.peek().getX()][stack.peek().getY()].setVisited(true);
    }

    private void createWallLeftRight() {
        if (!maze[stack.peek().getX()][stack.peek().getY() + 1].isVisited()) {
            maze[stack.peek().getX()][stack.peek().getY() + 1] = new Wall();
        }
        if (!maze[stack.peek().getX()][stack.peek().getY() - 1].isVisited()) {
            maze[stack.peek().getX()][stack.peek().getY() - 1] = new Wall();
        }
    }

    private void createWallTopBottom() {
        if (!maze[stack.peek().getX() + 1][stack.peek().getY()].isVisited()) {
            maze[stack.peek().getX() + 1][stack.peek().getY()] = new Wall();
        }
        if (!maze[stack.peek().getX() - 1][stack.peek().getY()].isVisited()) {
            maze[stack.peek().getX() - 1][stack.peek().getY()] = new Wall();
        }
    }

    private void createOuterWalls() {
        createCeilingWall();
        FillInnerSpace();
        createSideWalls();
        crateFloorWall();
    }

    private void createCeilingWall() {
        for (int j = 0; j < maze[0].length; j++) {
            maze[0][j] = new Wall();
            maze[0][j].setCellDiscovered(true);
        }
    }

    private void FillInnerSpace() {
        for (int i = 1; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new MazeCell(true, false, false);
            }
        }
    }

    private void createSideWalls() {
        for (int i = 1; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][0] = new Wall();
                maze[i][0].setCellDiscovered(true);
                maze[i][maze[i].length - 1] = new Wall();
                maze[i][maze[i].length - 1].setCellDiscovered(true);
            }
        }
    }

    private void crateFloorWall() {
        for (int i = maze.length - 1; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new Wall();
                maze[i][j].setCellDiscovered(true);
            }
        }
    }
}
