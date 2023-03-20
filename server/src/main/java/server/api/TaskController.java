package server.api;

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

    @PostMapping("/new-task/{cardId}")
    public ResponseEntity<Task> getNewTask(@PathVariable("cardId") long cardId,@RequestBody Task newTask){
        if(newTask == null || newTask.getName() == null || !parentRepo.existsById(cardId)){
            return ResponseEntity.badRequest().build();
        }
        newTask.setParentCard(parentRepo.getById(cardId));
        Task addedTask=repo.save(newTask);
        return ResponseEntity.ok(addedTask);
    }
}
