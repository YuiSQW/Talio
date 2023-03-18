package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.BoardList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;

import server.database.BoardListRepository;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardListController.class)
@MockBeans({@MockBean(BoardListRepository.class), @MockBean(BoardRepository.class)})
class BoardListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardListRepository repo;

    @Autowired
    private BoardRepository parentRepo;

    @Test
    void getByIdTestCorrect() throws Exception {
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>()))));
        this.mockMvc.perform(get("/api/boardlists/1")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getByIdTestError() throws Exception{
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(get("/api/boardlists/-1")).andExpect(status().isBadRequest());
    }
    @Test
    void getNewListCorrect() throws Exception {
        Mockito.when(this.parentRepo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.parentRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Board("", new ArrayList<>())));
        Mockito.when(this.repo.save(Mockito.any(BoardList.class))).thenReturn(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>())));
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        this.mockMvc.perform(
            post("/api/boardlists/new-boardlist/1").contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(list))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewListError() throws Exception{
        Mockito.when(this.parentRepo.existsById(Mockito.anyLong())).thenReturn(false);
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        this.mockMvc.perform(post("/api/boardlists/new-boardlist/-1").contentType("application/json").content(new ObjectMapper().writeValueAsString(list))).andExpect(status().isBadRequest());
    }

    @Test
    void renameListCorrect() throws Exception{
        Mockito.when(repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>()))));
        Mockito.when(repo.save(Mockito.any(BoardList.class))).thenReturn(new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>())));
        this.mockMvc.perform(put("/api/boardlists/1/newname")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void renameListError() throws Exception{
        Mockito.when(repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(put("/api/boardlists/-1/newname")).andExpect(status().isBadRequest());
    }

    @Test
    void deleteListTest() {
        assertDoesNotThrow(() ->{
            BoardListController c = new BoardListController(repo, parentRepo);
            c.deleteList(0);
        });
    }
}