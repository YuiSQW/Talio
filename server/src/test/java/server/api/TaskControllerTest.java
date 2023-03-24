package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import commons.Board;


import commons.BoardList;
import commons.Card;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;
import server.database.CardRepository;
import server.database.TaskRepository;


import java.util.ArrayList;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TaskController.class)
@MockBeans({@MockBean(TaskRepository.class), @MockBean(CardRepository.class), @MockBean(BoardUpdateListener.class)})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private CardRepository parentRepo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

    private Board testBoard;
    private BoardList testList;

    private Card testCard;
    @BeforeEach
    void setup(){
        testBoard = new Board("", new ArrayList<>());
        testList = new BoardList("",new ArrayList<>(), testBoard);
        testBoard.addList(testList);
        testCard=new Card("","",testList);


    }

    @Test
    void getByIdTestError() throws Exception{
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(get("/api/tasks/-1")).andExpect(status().isBadRequest());
    }

    @Test
    void getByIdTestCorrect() throws Exception{
        Mockito.when(repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Task(testCard, "")));
        this.mockMvc.perform(get("/api/tasks/1")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewTaskTestCorrect() throws Exception{
        Mockito.when(parentRepo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repo.save(Mockito.any(Task.class))).thenReturn(new Task(testCard, ""));
        Mockito.when(parentRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Card("","",new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>())))));
        String content = new ObjectMapper().writeValueAsString(new Task(testCard, ""));
        this.mockMvc.perform(post("/api/tasks/new-task/1").contentType("application/json").content(content)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewTaskTestError() throws Exception{
        Mockito.when(parentRepo.existsById(Mockito.anyLong())).thenReturn(false);
        String content = new ObjectMapper().writeValueAsString(new Task(testCard, ""));
        this.mockMvc.perform(post("/api/tasks/new-task/-1").contentType("application/json").content(content)).andExpect(status().isBadRequest());
    }


}
