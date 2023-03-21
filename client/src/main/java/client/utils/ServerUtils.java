/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


import commons.Board;
import commons.BoardList;
import commons.Card;


import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

/**
 * The connection between the GUI and the server
 */
public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public Card getCard(long cardId){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/cards/" + cardId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<Card>(){});
    }

    public Card postNewCard(Card newCard, BoardList parentBoardList){
        Card card = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/cards/new-card/" + parentBoardList.id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(newCard, APPLICATION_JSON), Card.class);
        card.setParentList(parentBoardList);
        return card;
    }

    public BoardList getList(long listId){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boardlists/" + listId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<BoardList>(){});
    }


    public BoardList postNewList(BoardList newBoardList, Board parent){
        BoardList createdList = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boardlists/new-boardlist/" + parent.id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(newBoardList, APPLICATION_JSON), BoardList.class);
        createdList.setParentBoard(parent);
        return createdList;
    }

    public BoardList renameList(BoardList changedList, Board parent){
        BoardList list = ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boardlists/" + changedList.id + "/" + changedList.getName())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(changedList, APPLICATION_JSON), BoardList.class);
        list.setParentBoard(parent);
        return list;
    }

    public void deleteList(BoardList listToDelete){
        ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boardlists/" + listToDelete.id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }
    public Board getBoard(long boardId){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boards/" + boardId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<Board>(){});
    }


    public Board postNewBoard(Board newBoard){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/boards/new-board")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(newBoard, APPLICATION_JSON), Board.class);
    }



    public List<BoardList> getLists(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/api/boardlists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<BoardList>>() {});
    }

}