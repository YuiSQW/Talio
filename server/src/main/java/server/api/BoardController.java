package server.api;


import commons.Board;
import commons.BoardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    /**
     * the JpaRepository that is used to retrieve items of type Board
     */
    private final BoardRepository repo;

    public BoardController(BoardRepository repo){
        this.repo = repo;
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

    /**
     * Creates a new Board and sets its name, the new board is added to the database
     * @param name - the name of the new Board
     * @return a ResponseEntity containing the newly created Board or a badrequest error if the name is invalid
     */
    @GetMapping("/new-board/{name}")
    public ResponseEntity<Board> getNewBoard(@PathVariable("name") String name){
        if(name == null || name.equals(""))return ResponseEntity.badRequest().build();
        Board board = new Board(name, new ArrayList<BoardList>());
        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }


}
