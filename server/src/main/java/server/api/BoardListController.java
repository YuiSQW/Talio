package server.api;


import commons.BoardList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardListRepository;
import server.database.BoardRepository;



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
     * @return a ResponseEntity containing the newly created BoardList or a badrequest error if id is invalid
     */
    @PostMapping("/new-boardlist/{boardId}")
    ResponseEntity<BoardList> getNewList(@RequestBody BoardList newList,@PathVariable("boardId") long boardId){
        if(boardId < 0 || !parentRepo.existsById(boardId)){
            return ResponseEntity.badRequest().build();
        }
        newList.setParentBoard(parentRepo.getById(boardId));
        BoardList addedList = repo.save(newList);
        return ResponseEntity.ok(addedList);
    }

    @PutMapping("/{id}/{newName}")
    public ResponseEntity<BoardList> renameList(@PathVariable("id") long id, @PathVariable("newName") String newName) {
        if(!repo.existsById(id))return ResponseEntity.badRequest().build();
        BoardList currentList = repo.findById(id).get();
        currentList.setName(newName);
        return ResponseEntity.ok(repo.save(currentList));
    }

    @DeleteMapping("/{id}")
    public void deleteList(@PathVariable("id") long id) {
        try {
            repo.deleteById(id);
        }catch(IllegalArgumentException e){
            System.out.println("The id for deleteList cannot be null");
            e.printStackTrace();
        }
    }


}
