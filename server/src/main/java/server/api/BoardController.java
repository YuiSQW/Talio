package server.api;



import commons.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.BoardRepository;



@RestController
@RequestMapping("/api/boards")
public class BoardController {

    /**
     * the JpaRepository that is used to retrieve items of type Board
     */
    private final BoardRepository repo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

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
     * @return a ResponseEntity containing the newly created Board or a badrequest error if the board is invalid
     */

    @PostMapping("/new-board")
    public ResponseEntity<Board> getNewBoard(@RequestBody Board board){
        if(board.getLists() == null || board.getName() == null)return ResponseEntity.badRequest().build();
        Board saved = repo.save(board);
        boardUpdateListener.add(saved);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/{newName}")
    public ResponseEntity<Board> changeName(@PathVariable("id") long id, @PathVariable("newName") String newName){
        if(!repo.existsById(id))return ResponseEntity.badRequest().build();
        Board board = repo.findById(id).get();
        board.setName(newName);
        Board updatedBoard = repo.save(board);
        boardUpdateListener.add(updatedBoard);
        return ResponseEntity.ok(updatedBoard);
    }


}
