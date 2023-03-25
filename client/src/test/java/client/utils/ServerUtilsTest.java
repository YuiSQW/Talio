package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import commons.Board;
import commons.BoardList;
import commons.Card;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;


@WireMockTest(httpPort = 8080)
class ServerUtilsTest {


    @Test
    void getCardTest() throws Exception{
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        stubFor(get("/api/cards/33").willReturn(
            aResponse().withHeader("Content-Type","application/json").withBody(new ObjectMapper().writeValueAsString(new Card("test", "", list))))
            );
        ServerUtils server = new ServerUtils();
        Card card = server.getCard(33);
        assertNotNull(card);
        assertEquals("test", card.getTitle());
    }

    @Test
    void getBoardTest() throws Exception{
        Board testBoard = new Board("testname", new ArrayList<>());
        stubFor(get("/api/boards/129").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(testBoard))
        ));
        ServerUtils server = new ServerUtils();
        Board board = server.getBoard(129);
        assertNotNull(board);
        assertEquals("testname", board.getName());
    }
    

    @Test
    void postNewCardTest() throws Exception{
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        list.id = 1;
        Card testCard = new Card("title", "desc", list);
        stubFor(post("/api/cards/new-card/1").willReturn(
            aResponse().withHeader("Content-Type", "application/json")
                .withBody(new ObjectMapper().writeValueAsString(testCard))
        ));
        ServerUtils server = new ServerUtils();
        Card card = server.postNewCard(testCard, list);
        assertNotNull(card);
        assertEquals("title", card.getTitle());
        assertEquals("desc", card.getDescription());
    }
    
    /**
     * Tests the behavior of the deleteCard method in the ServerUtils class when
     * called with a valid card to delete.
     * @throws Exception is there is an error with the test
     */
    @Test
    void deleteCardTest() throws Exception {
        Board board = new Board("", new ArrayList<>());
        Card cardToDelete = new Card("testname", "bla", new BoardList("", new ArrayList<>(), board));
        cardToDelete.id = 1;
        stubFor(delete("api/cards/delete/1").willReturn(
                aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(cardToDelete))
        ));
        ServerUtils server = new ServerUtils();
        assertDoesNotThrow(() -> server.deleteCard(cardToDelete));
        
    }
    @Test
    void postNewBoardTest() throws Exception{
        Board boardToPost = new Board("testname", new ArrayList<>());
        stubFor(post("/api/boards/new-board").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(boardToPost))
        ));
        ServerUtils server = new ServerUtils();
        Board board = new Board("test", new ArrayList<>());
        Board createdBoard = server.postNewBoard(board);
        assertNotNull(createdBoard);
        assertEquals("testname", createdBoard.getName());
        assertEquals(new ArrayList<BoardList>(),createdBoard.getLists() );
    }
    
    /**
     * Test to verify if the renaming of a board works
     * Sends a put request to the server with the specified id + new name of the board
     * Checks if the response of the server is the board with the expected new name
     * @throws Exception if test fails
     */
    @Test
    void renameBoard() throws Exception{
        Board board = new Board("newname", new ArrayList<>());
        board.id = 1;
        stubFor(put("/api/boards/1/newname").willReturn(
                aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(board))));
        ServerUtils server = new ServerUtils();
        Board changedBoard = server.renameBoard(board);
        assertNotNull(changedBoard);
        assertEquals("newname", changedBoard.getName());
    }

    @Test
    void getListTest() throws Exception{
        BoardList list = new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>()));
        stubFor(get("/api/boardlists/1").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(list))
        ));
        ServerUtils server = new ServerUtils();
        BoardList receivedList =  server.getList(1);
        assertNotNull(receivedList);
        assertEquals("", receivedList.getName());
        assertEquals(new ArrayList<Card>(), receivedList.getCardList());
    }
    
    @Test
    void postListTest() throws Exception{
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        board.id = 1;
        stubFor(post("/api/boardlists/new-boardlist/1").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(list))));
        ServerUtils server = new ServerUtils();
        BoardList createdList = server.postNewList(list,board);
        assertNotNull(createdList);
        assertEquals("", createdList.getName());
        assertEquals(new ArrayList<Card>(), createdList.getCardList());
    }

    @Test
    void renameListTest() throws Exception{
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("newname", new ArrayList<>(), board);
        list.id = 1;
        stubFor(put("/api/boardlists/1/newname").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(list))));
        ServerUtils server = new ServerUtils();
        BoardList changedList = server.renameList(list, board);
        assertNotNull(changedList);
        assertEquals("newname", changedList.getName());
        assertEquals(new ArrayList<Card>(), changedList.getCardList());
    }
    

    
    @Test
    void deleteListTest() throws Exception{
        Board board = new Board("", new ArrayList<>());
        BoardList list = new BoardList("", new ArrayList<>(), board);
        list.id = 1;
        stubFor(delete("api/boardlists/1").willReturn(
            aResponse().withHeader("Content-Type", "application/json").withBody(new ObjectMapper().writeValueAsString(list))
        ));
        ServerUtils server = new ServerUtils();
        assertDoesNotThrow(() -> server.deleteList(list));

    }
}