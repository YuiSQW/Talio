package server.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.BoardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    /**
     * the JpaRepository that is used to retrieve items of type Board
     */
    private final BoardRepository repo;

    // set the timeout for the long polling request to 5 seconds
    //private final Long LONG_POLLING_TIMEOUT = 5000L;

    // service that facilitates the busy waiting of polling client requests to be outsourced to other threads,
    // so that the request thread gets freed up
    //private ExecutorService waitingUsersService;

    // Thread-safe HashMap that keeps the
    //private ConcurrentMap<Long, LocalDateTime> boardUpdates;
    public BoardController(BoardRepository repo){
        this.repo = repo;
        //this.waitingUsersService = Executors.newCachedThreadPool();
        //this.boardUpdates = new ConcurrentHashMap<>();
    }

    /**
     * Gets a Board by id
     * @param id - the id of the board that is to be retrieved
     * @return a ResponseEntity containing the retrieved Board or a badrequest error if id is invalid
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id){
        if(id < 0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /*@GetMapping("/polling/{id}")
    public DeferredResult<ResponseEntity<Board>> pollBoard(@PathVariable("id") long id) throws Exception{
        DeferredResult<ResponseEntity<Board>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);

        this.waitingUsersService.execute( () -> {
            try {
                // this for loop will be changed to keep running until the value of the LocalDateTime in the hashmap gets updated,
                // signalling that the resource has been updated.
                while (this.boardUpdates.get(id) == null || true) {

                }
                output.setResult(ResponseEntity.ok(repo.findById(id).get()));
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        output.onTimeout(() -> {
            try{
            output.setResult(ResponseEntity.ok(repo.findById(id).get()));
        }catch(Exception e){
                e.printStackTrace();
            }
        });
        return output;
    }*/
    /**
     * Creates a new Board and sets its name, the new board is added to the database
     * @return a ResponseEntity containing the newly created Board or a badrequest error if the board is invalid
     */

    @PostMapping("/new-board")
    public ResponseEntity<Board> getNewBoard(@RequestBody Board board){
        if(board.getLists() == null || board.getName() == null)return ResponseEntity.badRequest().build();
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }


}
