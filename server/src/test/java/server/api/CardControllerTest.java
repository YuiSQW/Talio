package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.BoardList;
import commons.Card;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @BeforeEach
    void setup(){
        testBoard = new Board("", new ArrayList<>());
        list = new BoardList("",new ArrayList<>(), testBoard);
        testBoard.addList(list);

    }
    @Test
    void getByIdTestCorrect() throws Exception{
        Mockito.when(repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Card("", "", list)));
        this.mockMvc.perform(get("/api/cards/1")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getByIdTestError()throws Exception{
        Mockito.when(repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(get("/api/cards/-1")).andExpect(status().isBadRequest());
    }

    @Test
    void getNewCardTestCorrect() throws Exception{
        Mockito.when(parentRepo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repo.save(Mockito.any(Card.class))).thenReturn(new Card("", "", list));
        Mockito.when(parentRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>()))));
        String content = new ObjectMapper().writeValueAsString(new Card("", "", list));
        this.mockMvc.perform(post("/api/cards/new-card/1").contentType("application/json").content(content)).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewCardTestError() throws Exception{
        Mockito.when(parentRepo.existsById(Mockito.anyLong())).thenReturn(false);
        String content = new ObjectMapper().writeValueAsString(new Card("", "", list));
        this.mockMvc.perform(post("/api/cards/new-card/-1").contentType("application/json").content(content)).andExpect(status().isBadRequest());
    }
}