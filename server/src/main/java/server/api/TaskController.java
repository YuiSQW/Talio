package server.api;

import commons.Card;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository repo;

    private final CardRepository parentRepo;

    public TaskController(TaskRepository repo, CardRepository parentRepo) {
        this.repo = repo;
        this.parentRepo = parentRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id){
        if(id < 0 || !repo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @GetMapping("/new-task/{cardId}/{name}")
    public ResponseEntity<Task> getNewTask(@PathVariable("cardId") long cardId,@PathVariable("name") String name){
        if(cardId < 0 || !parentRepo.existsById(cardId)){
            return ResponseEntity.badRequest().build();
        }
        Card parentCard=parentRepo.getById(cardId);
        Task taskToAdd=new Task(parentCard,name);
        Task addedTask=repo.save(taskToAdd);
        return ResponseEntity.ok(addedTask);
    }
}
