package server.api;



import commons.Board;

import commons.BoardList;
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

    @PutMapping("/change-name/{id}/{newName}")
    public ResponseEntity<Board> changeName(@PathVariable("id") long id, @PathVariable("newName") String newName){
        if(!repo.existsById(id))return ResponseEntity.badRequest().build();
        Board board = repo.findById(id).get();
        board.setName(newName);
        Board updatedBoard = repo.save(board);
        boardUpdateListener.add(updatedBoard);
        return ResponseEntity.ok(updatedBoard);
    }

    /**
     * Method that moves a list from one position (listToMove) to a different one (newPos).
     * The method first removes the list and then add it to the correct position, so if we have board with 3 lists(list0, list1, list2), to put list0 at the back
     * we would do a call with listToMove=0 ad newPos=2.
     * @return a ResponseEntity that contains the modified board or a badrequests error if the method fails.
     */
    @PutMapping("/move-list/{id}/{listToMove}/{newPos}")
    public ResponseEntity<Board> reorderList(@PathVariable("id") long id, @PathVariable("listToMove") long listToMove,@PathVariable("newPos") long newPos){
        if(!repo.existsById(id) || listToMove<0 || newPos<0) {
            return ResponseEntity.badRequest().build();

        }
        Board board=repo.findById(id).get();
        var lists=board.getLists();
        if(listToMove>=lists.size() || newPos>=lists.size()) {
            return ResponseEntity.badRequest().build();
        }
        BoardList movedList=lists.get((int) listToMove);
        lists.remove(movedList);
        lists.add((int) newPos,movedList);
        board.setLists(lists);
        Board updatedBoard = repo.save(board);
        boardUpdateListener.add(updatedBoard);
        return ResponseEntity.ok(updatedBoard);
    }


}
