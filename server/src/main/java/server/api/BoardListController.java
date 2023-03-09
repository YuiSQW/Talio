package server.api;

import commons.Board;
import commons.BoardList;
import commons.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardListRepository;
import server.database.BoardRepository;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/boardlists")
public class BoardListController {

    /**
     * repo - the JpaRepository that is used for all items of type BoardList
     */
    private final BoardListRepository repo;

    /**
     * parentRepo - the JpaRepository that is used for all items of type Board. This repo is necessary
     * for retrieving the value of the parentBoard field when constructing a new BoardList
     */
    private final BoardRepository parentRepo;

    public BoardListController(BoardListRepository repo, BoardRepository parentRepo){
        this.repo = repo;
        this.parentRepo = parentRepo;
    }

    /**
     * Gets a BoardList by id
     * @param id - the id of the BoardList that has to be retrieved
     * @return a ResponseEntity containing the BoardList that was requested by id, or a badrequest error if id is invalid
     */
    @GetMapping("/{id}")
    ResponseEntity<BoardList> getById(@PathVariable("id") long id){
        if(id < 0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());

    }

    /**
     * Creates a new BoardList as a child of the board for which the id is passed
     * @param boardId - the id of the board to which the BoardList should be added
     * @param name - the name that will be given to the newly created BoardList
     * @return a ResponseEntity containing the newly created BoardList or a badrequest error if id is invalid
     */
    @GetMapping("/new-boardlist/{boardId}/{name}")
    ResponseEntity<BoardList> getNewList(@PathVariable("boardId") long boardId, @PathVariable("name") String name){
        if(boardId < 0 || !parentRepo.existsById(boardId)){
            return ResponseEntity.badRequest().build();
        }
        Board parent = parentRepo.getById(boardId);
        BoardList listToBeAdded = new BoardList(name, new ArrayList<Card>(), parent);
        BoardList addedList = repo.save(listToBeAdded);
        return ResponseEntity.ok(addedList);
    }

    @PutMapping("/{id}/{newName}")
    public BoardList renameList(@PathVariable("id") long id, @PathVariable("newName") String newName) {
        BoardList currentList = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND));
        currentList.setName(newName);
        return repo.save(currentList);
    }

    @DeleteMapping("/{id}")
    public void deleteList(@PathVariable("id") long id) {
        repo.deleteById(id);
    }


}
