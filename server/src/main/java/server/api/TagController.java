package server.api;


import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.CardRepository;
import server.database.TagRepository;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository repo;
    private final CardRepository cardsRepo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

    public TagController(TagRepository tagRepository, CardRepository cardRepository){
        this.repo=tagRepository;
        this.cardsRepo=cardRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id){
        if(id < 0 || !this.repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

}
