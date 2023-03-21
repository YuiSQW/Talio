package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import commons.Board;
import commons.BoardList;
import commons.Card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wiremock.org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;


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

    @Test
    void connectFailTest() {
        ServerUtils server = new ServerUtils();
        String invalidIpAddress = "invalid";
        assertThrows(Exception.class, () -> {
            server.connect(invalidIpAddress);
        });
    }
}