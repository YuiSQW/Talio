package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import commons.Board;


import org.junit.jupiter.api.Test;


import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.web.servlet.MockMvc;
import server.database.BoardRepository;



import java.util.ArrayList;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BoardController.class)
@MockBeans({@MockBean(BoardRepository.class)})
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository repo;

    //Checks whether invalid id is correctly handled
    @Test
    void getByIdTestError() throws Exception{
        this.mockMvc.perform(get("/api/boards/-1")).andExpect(status().isBadRequest());
    }

    @Test
    void getByIdTestCorrect() throws Exception{
        Mockito.when(this.repo.existsById((long)1)).thenReturn(true);
        Mockito.when(this.repo.findById((long)1)).thenReturn(Optional.of(new Board("test", new ArrayList<>())));
        this.mockMvc.perform(get("/api/boards/1")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewBoardTest() throws Exception{
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(new Board("", new ArrayList<>()));
        String content = new ObjectMapper().writeValueAsString(new Board("", new ArrayList<>()));
        this.mockMvc.perform(
            post("/api/boards/new-board").contentType("application/json").content(content)).andExpect(status().is2xxSuccessful());
    }


    @Test
    void getNewBoardTestError() throws Exception{
        String content = new ObjectMapper().writeValueAsString(new Board(null, new ArrayList<>()));
        this.mockMvc.perform(post("/api/boards/new-board").contentType("application/json").content(content)).andExpect(status().isBadRequest());

    }

}


