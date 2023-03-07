package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getListsTest() {
        Board testBoard = new Board("testboard", new ArrayList<BoardList>());
        assertEquals(new ArrayList<BoardList>(), testBoard.getLists());
    }

    @Test
    void setLists() {
        Board testBoard = new Board("testboard", new ArrayList<BoardList>());
        List<BoardList> testList = new ArrayList<BoardList>();
        testList.add(new BoardList("", new ArrayList<>(), testBoard));
        testBoard.setLists(testList);
        assertEquals(new BoardList("", new ArrayList<>(), testBoard), testBoard.getLists().get(0));
    }

    @Test
    void getName() {
        Board testBoard = new Board("testboard", new ArrayList<BoardList>());
        assertEquals("testboard", testBoard.getName());
    }

    @Test
    void setName() {
        Board testBoard = new Board("testboard", new ArrayList<BoardList>());
        testBoard.setName("new name");
        assertEquals("new name", testBoard.getName());
    }


    @Test
    void testEquals() {
        Board testBoard = new Board("testboard", new ArrayList<BoardList>());
        Board otherBoard = new Board("testboard", new ArrayList<BoardList>());
        assertEquals(testBoard, otherBoard);
        otherBoard.setName("new name");
        assertNotEquals(testBoard, otherBoard);
    }
}