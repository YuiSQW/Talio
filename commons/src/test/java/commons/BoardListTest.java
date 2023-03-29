package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardListTest {

    @Test
    void getCardListTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        assertEquals(new ArrayList<Card>(), listTest.getCardList());
    }

    @Test
    void getNameTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        assertEquals("testboard", listTest.getName());
    }

    @Test
    void setNameTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        listTest.setName("new board name");
        assertEquals("new board name", listTest.getName());
    }

    @Test
    void setCardListTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        List<Card> testList = new ArrayList<Card>();
        testList.add(new Card("testCard", "test description", listTest));
        listTest.setCardList(testList);
        assertEquals(new Card("testCard", "test description", listTest), listTest.getCardList().get(0));
    }



    @Test
    void equalsTest(){
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        BoardList otherList = new BoardList("testboard", new ArrayList<Card>(), new Board("", new ArrayList<>()));
        assertEquals(listTest, otherList);
        otherList.setName("new name");
        assertNotEquals(listTest, otherList);
    }

    @Test
    void getIdTest(){
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        listTest.setId(20);
        Long id=listTest.getId();
        id++;
        Long id2=listTest.getId();
        assertEquals(id,id2+1);
    }

    @Test
    void setParentBoard(){
        Board board=new Board("name",new ArrayList<>());
        BoardList boardList=new BoardList("name", new ArrayList<>());
        boardList.setParentBoard(board);
        assertEquals(board, boardList.getParentBoard());
    }
}