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
import server.database.BoardListRepository;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.Optional;


import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@MockBeans({@MockBean(CardRepository.class), @MockBean(BoardListRepository.class), @MockBean(BoardUpdateListener.class)})
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository repo;

    @Autowired
    private BoardListRepository parentRepo;

    @Autowired BoardUpdateListener boardUpdateListener;
    private Board testBoard;
    private BoardList list;
    
    private Card card;
    @BeforeEach
    void setup(){
        testBoard = new Board("", new ArrayList<>());
        list = new BoardList("",new ArrayList<>(), testBoard);
        testBoard.addList(list);
        card = new Card("", "", list);
        
    }
    @Test
    void getByIdTestCorrect() throws Exception{
        when(repo.existsById(Mockito.anyLong())).thenReturn(true);
        when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Card("", "", list)));
        this.mockMvc.perform(get("/api/cards/1")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getByIdTestError()throws Exception{
        when(repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(get("/api/cards/-1")).andExpect(status().isBadRequest());
    }

    @Test
    void getNewCardTestCorrect() throws Exception{
        when(parentRepo.existsById(Mockito.anyLong())).thenReturn(true);
        when(repo.save(Mockito.any(Card.class))).thenReturn(new Card("", "", list));
        when(parentRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>()))));
        String content = new ObjectMapper().writeValueAsString(new Card("", "", list));
        this.mockMvc.perform(post("/api/cards/new-card/1").contentType("application/json").content(content)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewCardTestError() throws Exception{
        when(parentRepo.existsById(Mockito.anyLong())).thenReturn(false);
        String content = new ObjectMapper().writeValueAsString(new Card("", "", list));
        this.mockMvc.perform(post("/api/cards/new-card/-1").contentType("application/json").content(content)).andExpect(status().isBadRequest());
    }
    
    /**
     * Test the behavior of the deleteCard in the CardController class
     */
    @Test
    void deleteCardCorrect() {
        assertDoesNotThrow(() ->{
            CardController c = new CardController(repo, parentRepo);
            c.deleteCard(0);
        });
    }

    @Test
    void reorderTasksTestCorrect() throws Exception {
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Task task1= new Task(card,"task1");
        Task task2= new Task(card,"task2");
        card.addTask(task1);
        card.addTask(task2);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/move-task/1/0/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.taskList[0].name",is("task2")))
                .andExpect(jsonPath("$.taskList[1].name",is("task1")));
    }

    @Test
    void reorderTasksTestErrorCardNotFound() throws Exception {
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Task task1= new Task(card,"task1");
        Task task2= new Task(card,"task2");
        card.addTask(task1);
        card.addTask(task2);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/move-task/1/0/1"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void reorderTasksTestErrorTaskIndextooBig() throws Exception {
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Task task1= new Task(card,"task1");
        Task task2= new Task(card,"task2");
        card.addTask(task1);
        card.addTask(task2);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/move-task/1/2/1"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void reorderTasksTestErrorInvalidNewPosition() throws Exception {
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Task task1= new Task(card,"task1");
        Task task2= new Task(card,"task2");
        card.addTask(task1);
        card.addTask(task2);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/move-task/1/0/2"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void renameCardCorrect() throws Exception{
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/change-name/1/newtitle"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title",is("newtitle")));
    }
    @Test
    void renameCardError() throws Exception{
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/change-name/1/newtitle"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeCardDescriptionCorrect() throws Exception{
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/change-description/1/newtitle"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.description",is("newtitle")));
    }
    @Test
    void changeCardDescriptionError() throws Exception{
        Board board = new Board("test1", new ArrayList<>());
        BoardList boardList = new BoardList("test", new ArrayList<>(), board);
        Card card = new Card("card1", "testcard", boardList);
        boardList.addCard(card);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(card));
        Mockito.when(this.repo.save(Mockito.any(Card.class))).thenReturn(card);
        this.mockMvc.perform(put("/api/cards/change-description/1/newtitle"))
                .andExpect(status().isBadRequest());
    }
    
}