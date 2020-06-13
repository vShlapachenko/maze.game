package server.maze.game.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.maze.game.wrappers.ApiBoardWrapper;
import server.maze.game.wrappers.ApiGameWrapper;
import server.maze.game.model.MazeExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api")
public class GameController {

    private List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
    private List<MazeExecutor> mazeExecutors = new ArrayList<>();
    private List<ApiBoardWrapper> apiBoardWrappers = new ArrayList<>();
    private AtomicInteger nextID = new AtomicInteger();

    @GetMapping("about")
    public String getAbout() {
        return "Valetnyn Shlapachenko";
    }

    @GetMapping("games")
    public List<ApiGameWrapper> getApiGameWrappers() {
        return apiGameWrappers;
    }


    @PostMapping("games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createNewGame() {
        MazeExecutor mazeExecutor = new MazeExecutor();
        mazeExecutor.createMazeWithPlayers();
        mazeExecutors.add(nextID.get(), mazeExecutor);
        ApiGameWrapper apiGameWrapper = ApiGameWrapper.makeFromGame(mazeExecutor, nextID.get());
        nextID.incrementAndGet();
        apiGameWrappers.add(apiGameWrapper);
        return apiGameWrapper;
    }

    @GetMapping("games/{id}")
    public ApiGameWrapper getOneApiGameWrapper(@PathVariable("id") int id) {
        checkGameExist(id);
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                return apiGameWrapper;
            }
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("games/{id}/board")
    public ApiBoardWrapper getApiBoardWrapper(@PathVariable("id") int id) {
        checkGameExist(id);
        ApiBoardWrapper apiBoardWrapper = ApiBoardWrapper.makeFromGame(mazeExecutors.get(id));
        apiBoardWrappers.add(apiBoardWrapper);
        return apiBoardWrapper;
    }

    @PostMapping("games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMove(@RequestBody String move, @PathVariable("id") int id) {
        checkGameExist(id);
        int moveLeft = 1;
        int moveUp = 2;
        int moveRight = 3;
        int moveDown = 4;
        switch (move) {
            case "MOVE_LEFT":
                checkForCorrectMove(id, moveLeft);
                mazeExecutors.get(id).mouseMove(moveLeft);
                break;
            case "MOVE_UP":
                checkForCorrectMove(id, moveUp);
                mazeExecutors.get(id).mouseMove(moveUp);
                break;
            case "MOVE_RIGHT":
                checkForCorrectMove(id, moveRight);
                mazeExecutors.get(id).mouseMove(moveRight);
                break;
            case "MOVE_DOWN":
                checkForCorrectMove(id, moveDown);
                mazeExecutors.get(id).mouseMove(moveDown);
                break;
            case "MOVE_CATS":
                mazeExecutors.get(id).moveAllCats();
                break;
            default:
                throw new IllegalStateException();
        }
        checkIncrementCheeseCounter(id);
        checkGameLost(id);
        checkGameWon(id);
    }

    private void checkIncrementCheeseCounter(int id) {
        if (mazeExecutors.get(id).checkMouseCheeseCollision()){
            apiGameWrappers.get(id).numCheeseFound = apiGameWrappers.get(id).numCheeseFound + 1;
        }
    }

    private void checkGameLost(int id) {
        if (mazeExecutors.get(id).checkGameLost()) {
            apiGameWrappers.get(id).isGameLost = true;
        }
    }

    private void checkGameWon(@PathVariable("id") int id) {
        if (mazeExecutors.get(id).checkGameWon()) {
            apiGameWrappers.get(id).isGameWon = true;
        }
    }

    private void checkForCorrectMove(@PathVariable("id") int id, int move) {
        if (mazeExecutors.get(id).incorrectDirection(move)) {
            throw new IllegalStateException();
        }
    }

    private void checkGameExist(@PathVariable("id") int id) {
        if (mazeExecutors.size() < id) {
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void useCheat(@RequestBody String move, @PathVariable("id") int id) {
        checkGameExist(id);
        switch (move) {
            case "1_CHEESE":
                apiGameWrappers.get(id).numCheeseGoal = 1;
                mazeExecutors.get(id).setCheeseToOne();
                break;
            case "SHOW_ALL":
                mazeExecutors.get(id).revealMap();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handelIllegalArgumentException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalReceiveException() {
    }
}
