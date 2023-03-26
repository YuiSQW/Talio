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
import server.database.BoardRepository;



import java.util.ArrayList;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import static org.hamcrest.Matchers.*;


@WebMvcTest(BoardController.class)
@MockBeans({@MockBean(BoardRepository.class), @MockBean(BoardUpdateListener.class)})
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository repo;

    @Autowired
    private BoardUpdateListener boardUpdateListener;

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

    @Test
    void reorderListTestCorrect() throws Exception {
        Board board=new Board("test1",new ArrayList<>());
        ArrayList<BoardList> lists= new ArrayList<>();
        lists.add(new BoardList("name1",new ArrayList<>(),board));
        lists.add(new BoardList("name2",new ArrayList<>(),board));
        board.setLists(lists);

        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(board);
        this.mockMvc.perform(put("/api/boards/move-list/1/0/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.lists[0].name",is("name2")))
                .andExpect(jsonPath("$.lists[1].name",is("name1")));
    }

    @Test
    void reorderListTestErrorNoSuchBoard() throws Exception{
        Board board=new Board("test1",new ArrayList<>());
        ArrayList<BoardList> lists= new ArrayList<>();
        lists.add(new BoardList("name1",new ArrayList<>(),board));
        lists.add(new BoardList("name2",new ArrayList<>(),board));
        board.setLists(lists);

        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(board);
        this.mockMvc.perform(put("/api/boards/move-list/1/0/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reorderListTestErrorListIndexTooBig() throws Exception{
        Board board=new Board("test1",new ArrayList<>());
        ArrayList<BoardList> lists= new ArrayList<>();
        lists.add(new BoardList("name1",new ArrayList<>(),board));
        lists.add(new BoardList("name2",new ArrayList<>(),board));
        board.setLists(lists);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(board);
        this.mockMvc.perform(put("/api/boards/move-list/1/2/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reorderListTestErrorInvalidNewPosition() throws Exception{
        Board board=new Board("test1",new ArrayList<>());
        ArrayList<BoardList> lists= new ArrayList<>();
        lists.add(new BoardList("name1",new ArrayList<>(),board));
        lists.add(new BoardList("name2",new ArrayList<>(),board));
        board.setLists(lists);
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(board);
        this.mockMvc.perform(put("/api/boards/move-list/1/0/2"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void changeNameTestCorrect() throws Exception{
        Board board=new Board("test",new ArrayList<>());
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        Mockito.when(this.repo.save(Mockito.any(Board.class))).thenReturn(board);
        this.mockMvc.perform(put("/api/boards/change-name/1/test2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is("test2")));
    }
    @Test
    void changeNameTestError() throws Exception{
        Board board=new Board("test",new ArrayList<>());
        Mockito.when(this.repo.existsById(Mockito.anyLong())).thenReturn(false);
        this.mockMvc.perform(put("/api/boards/change-name/1/test2"))
                .andExpect(status().isBadRequest());
    }


}


