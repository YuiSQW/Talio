package server.api;


import commons.Board;
import commons.Card;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository repo;
    private final CardRepository cardsRepo;

    private final BoardRepository parentRepo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

    public TagController(TagRepository tagRepository, CardRepository cardRepository,
                         BoardRepository parentRepo){
        this.repo=tagRepository;
        this.cardsRepo=cardRepository;
        this.parentRepo=parentRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id){
        if(id < 0 || !this.repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }
    @GetMapping("/get-tags/{cardId}")
    public ResponseEntity<Set<Tag>> getTagsOfCard(@PathVariable("card_id") long cardId){
        if( cardId<0 || !this.cardsRepo.existsById(cardId)){
            return ResponseEntity.badRequest().build();
        }
        Card card = this.cardsRepo.getById(cardId);
        Set<Tag> tags = card.getTagSet();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/get-tags/{parentId}")
    public ResponseEntity<Set<Tag>> getTagsOfBoard(@PathVariable("parentId") long parentId){
        if( parentId<0 || !this.parentRepo.existsById(parentId)){
            return ResponseEntity.badRequest().build();
        }
        Board parentBoard = this.parentRepo.getById(parentId);
        Set<Tag> tags = parentBoard.getTags();
        return ResponseEntity.ok(tags);
    }
    @PostMapping("/new-tag/{parentId}")
    public ResponseEntity<Tag> postNewTag(@RequestBody Tag newTag,
                                          @PathVariable("parentId") long parentId){
        if( parentId < 0 || !this.parentRepo.existsById(parentId)){
            return ResponseEntity.badRequest().build();
        }
        Board parentBoard= parentRepo.getById(parentId);
        newTag.setParentBoard(parentBoard);
        parentBoard.addTag(newTag);
        Board updatedBoard= this.parentRepo.saveAndFlush(parentBoard);
        boardUpdateListener.add(updatedBoard);
        return ResponseEntity.ok(newTag);
    }
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") long id){
        if(!repo.existsById(id)){
            return;
        }
        Tag tagToDelete = repo.getById(id);
        Board affectedBoard = parentRepo.getById(tagToDelete.getParentBoard().id);
        affectedBoard.deleteTag(tagToDelete);
        repo.deleteById(id);
        Board updatedBoard = parentRepo.saveAndFlush(affectedBoard);
        boardUpdateListener.add(updatedBoard);
    }
    @PutMapping("/add-tag/{card_id}/{tag_id}")
    public ResponseEntity<Tag> addTagToCard(@PathVariable("cardId") long cardId,
                                               @PathVariable("tagId") long tagId){
        if(cardId<0 || tagId<0 || !this.repo.existsById(tagId)
        || !this.cardsRepo.existsById(cardId)){
            return ResponseEntity.badRequest().build();
        }
        Card card= this.cardsRepo.getById(cardId);
        Tag tag= this.repo.getById(tagId);
        card.addTag(tag);
        tag.addCard(card);
        boardUpdateListener.add(tag.getParentBoard());
        return ResponseEntity.ok(tag);
    }



}
