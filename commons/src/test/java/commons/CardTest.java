package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getDescriptionTest() {
        Card testCard = new Card("cardTitle", "card description");
        assertEquals("card description", testCard.getDescription());
    }

    @Test
    void getTitleTest() {
        Card testCard = new Card("cardTitle", "card description");
        assertEquals("cardTitle", testCard.getTitle());
    }

    @Test
    void setDescriptionTest(){
        Card testCard = new Card("cardTitle", "card description");
        testCard.setDescription("new description");
        assertEquals("new description", testCard.getDescription());
    }

    @Test
    void setTitleTest(){
        Card testCard = new Card("cardTitle", "card description");
        testCard.setTitle("new title");
        assertEquals("new title", testCard.getTitle());
    }

    @Test
    void equalsTest(){
        Card testCard = new Card("cardTitle", "card description");
        Card otherCard = new Card("cardTitle", "card description");
        assertTrue(testCard.equals(otherCard));
        otherCard.setTitle("new title");
        assertFalse(testCard.equals(otherCard));
    }
}