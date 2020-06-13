package server.maze.game.wrappers;

import server.maze.game.model.Cat;
import server.maze.game.model.Cheese;
import server.maze.game.model.MazeCell;
import server.maze.game.model.Mouse;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(MazeCell cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();

        if (cell instanceof Cat){
            location.x = ((Cat) cell).getCoordinateX();
            location.y = ((Cat) cell).getCoordinateY();
        } else if (cell instanceof Mouse) {
            location.x = ((Mouse) cell).getCoordinateX();
            location.y = ((Mouse) cell).getCoordinateY();
        } else {
            location.x = ((Cheese) cell).getCoordinateX();
            location.y = ((Cheese) cell).getCoordinateY();

        }
        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<Cat> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();
        for (MazeCell cell : cells){
            locations.add(ApiLocationWrapper.makeFromCellLocation(cell));
        }
        return locations;
    }
}
