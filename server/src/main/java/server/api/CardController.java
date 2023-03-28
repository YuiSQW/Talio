package server.api;



import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    /**
     * repo - the JpaRepository that is used for all items of type Card
     */
    private final CardRepository repo;

    /**
     * parentRepo - the JpaRepository that is used to retrieve the parent BoardList of the card, in order to set the parentList field
     */
    private final BoardListRepository parentRepo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

    public CardController(CardRepository repo, BoardListRepository parentRepo){
        this.repo = repo;
        this.parentRepo = parentRepo;
    }


    /**
     * Gets a card by id
     * @param id - the id specified in the url, id specifies the id of the card to be retrieved
     * @return a ResponseEntity containing the card that has been requested by id
     */

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id){
        if(id < 0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Creates and adds a new card to a boardlist, the card is also added to the database
     * @param boardListId - the id of the boardlist to which the card should be added to, the boardlist should already exist in the jparepository
     * @return a ResponseEntity that contains the newly created card
     */

    @PostMapping("/new-card/{boardListId}")
    public ResponseEntity<Card> getNewCard(@RequestBody Card newCard, @PathVariable("boardListId") long boardListId){
        if(newCard == null || newCard.getTitle() == null || newCard.getDescription() == null || !parentRepo.existsById(boardListId))
            return ResponseEntity.badRequest().build();
        newCard.setParentList(parentRepo.findById(boardListId).get());
        Card saved = repo.save(newCard);
        boardUpdateListener.add(saved.getParentList().getParentBoard());
        return ResponseEntity.ok(saved);
    }
    
    
  
    /**
     * Deletes the card from the database
     * @param id the ID of the card to delete
     * @throws IllegalArgumentException if the provided ID is null
     */
    @DeleteMapping("delete/{id}")
    public void deleteCard(@PathVariable("id") long id) {
        try {
            repo.deleteById(id);
        }catch(IllegalArgumentException e){
            System.out.println("The id for deleteCard cannot be null");
            e.printStackTrace();
        }
    }
    
}
