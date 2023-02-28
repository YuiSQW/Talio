package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardListTest {

    @Test
    void getCardListTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        assertEquals(new ArrayList<Card>(), listTest.getCardList());
    }

    @Test
    void getNameTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        assertEquals("testboard", listTest.getName());
    }

    @Test
    void setNameTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        listTest.setName("new board name");
        assertEquals("new board name", listTest.getName());
    }

    @Test
    void setCardListTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        List<Card> testList = new ArrayList<Card>();
        testList.add(new Card("testCard", "test description"));
        listTest.setCardList(testList);
        assertEquals(new Card("testCard", "test description"), listTest.getCardList().get(0));
    }

    @Test
    void addCardTest() {
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        listTest.addCard(new Card("testCard", "test description"));
        assertEquals(new Card("testCard", "test description"), listTest.getCardList().get(0));
    }

    @Test
    void deleteCard() {
        //TODO
    }

    @Test
    void emptyConstructorTest(){
        BoardList listTest = new BoardList();
        assertNotNull(listTest);
        assertEquals("", listTest.getName());
        assertEquals(new ArrayList<Card>(), listTest.getCardList());
    }

    @Test
    void equalsTest(){
        BoardList listTest = new BoardList("testboard", new ArrayList<Card>());
        BoardList otherList = new BoardList("testboard", new ArrayList<Card>());
        assertEquals(listTest, otherList);
        otherList.setName("new name");
        assertNotEquals(listTest, otherList);
    }

}