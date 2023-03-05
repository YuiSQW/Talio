package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getDescriptionTest() {
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card testCard = new Card("cardTitle", "card description", r);
        assertEquals("card description", testCard.getDescription());
    }

    @Test
    void getTitleTest() {
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card testCard = new Card("cardTitle", "card description", r);
        assertEquals("cardTitle", testCard.getTitle());
    }

    @Test
    void setDescriptionTest(){
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card testCard = new Card("cardTitle", "card description", r);
        testCard.setDescription("new description");
        assertEquals("new description", testCard.getDescription());
    }

    @Test
    void setTitleTest(){
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card testCard = new Card("cardTitle", "card description", r);
        testCard.setTitle("new title");
        assertEquals("new title", testCard.getTitle());
    }

    @Test
    void equalsTest(){
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card testCard = new Card("cardTitle", "card description", r);
        Card otherCard = new Card("cardTitle", "card description", r);
        assertTrue(testCard.equals(otherCard));
        otherCard.setTitle("new title");
        assertFalse(testCard.equals(otherCard));
    }
}