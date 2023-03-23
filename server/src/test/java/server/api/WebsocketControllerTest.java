package server.api;

import commons.Board;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebsocketControllerTest {

    @Value("${local.server.port}")
    private Integer port;
    private String url;
    private CompletableFuture<Board> comFut;

    private Board receivedBoard;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        this.url = "ws://localhost:" + port + "/boardsession";
        this.comFut = new CompletableFuture<>();
        this.receivedBoard = null;
    }
    @Test
    void sendBoardOnSubscriptionTest() throws Exception{
        WebSocketStompClient socket = new WebSocketStompClient(new StandardWebSocketClient());
        socket.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = socket.connect(url, new StompSessionHandlerAdapter(){}).get(10, SECONDS);


        session.subscribe("/boards/boardfeed/1", new GetBoardStompFrameHandler());
        Thread.sleep(3000);
        assertNotNull(receivedBoard);
    }

    @Test
    void sendChangedBoardTest() throws Exception{
        WebSocketStompClient socket = new WebSocketStompClient(new StandardWebSocketClient());
        socket.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = socket.connect(url, new StompSessionHandlerAdapter() {}).get();
        String randString = RandomStringUtils.random(6, true, false);
        session.subscribe("/boards/boardfeed/1", new GetBoardStompFrameHandler());
        mockMvc.perform(put("/api/boards/change-name/1/" + randString));

        Thread.sleep(5000);

        assertEquals(randString, receivedBoard.getName());

    }

    private class GetBoardStompFrameHandler implements StompFrameHandler{

        @Override
        public Type getPayloadType(StompHeaders stompHeaders){
            return Board.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object board){
            System.out.println("Frame received");
            receivedBoard = (Board) board;
        }
    }
}
