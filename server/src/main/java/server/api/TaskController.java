package server.api;

import commons.Card;
import commons.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TaskRepository;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository repo;

    private final CardRepository parentRepo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

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

    @PostMapping("/new-task/{taskId}")
    public ResponseEntity<Task> getNewTask(@PathVariable("taskId") long taskId, @RequestBody Task newTask){
        if(newTask == null || newTask.getName() == null || !parentRepo.existsById(taskId)){
            return ResponseEntity.badRequest().build();
        }

        Card parentCard=parentRepo.getById(taskId);
        newTask.setParentCard(parentCard);
        Task addedTask=repo.save(newTask);
        boardUpdateListener.add(addedTask.getParentCard().getParentList().getParentBoard());

        return ResponseEntity.ok(addedTask);
    }

    @PutMapping("/change-name/{taskId}/{newName}")
    public ResponseEntity<Task> changeName(@PathVariable("taskId") long taskId, @PathVariable("newName")String newName){
        if(!repo.existsById(taskId)) {
            return ResponseEntity.badRequest().build();
        }
        Task currentTask=repo.findById(taskId).get();
        currentTask.setName(newName);
        Task updatedTask=repo.save(currentTask);
        boardUpdateListener.add(updatedTask.getParentCard().getParentList().getParentBoard());
        return ResponseEntity.ok(updatedTask);
    }
}
