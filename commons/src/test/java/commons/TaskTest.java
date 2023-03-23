package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the task class
 */
public class TaskTest {
    @Test
    void getNameTest(){
        Task testTask=new Task(null,"Name");
        String name=testTask.getName();
        assertEquals(name,"Name");
    }

    @Test
    void setNameTest(){
        Task testTask=new Task(null,"Name");
        testTask.setName("Name2");
        assertEquals(testTask.getName(),"Name2");
    }
    @Test
    void idTest(){
        Task testTask=new Task(null,"Name");
        testTask.setId(12);
        assertEquals(testTask.getId(),12);
    }

    @Test
    void equalsTest(){
        Board t = new Board("", new ArrayList<>());
        BoardList r  = new BoardList("", new ArrayList<>(), t);
        Card c = new Card("cardTitle", "card description", r);
        Task task=new Task(c,"Name");
        Task task2=new Task(c,"Name");
        assertEquals(task,task2);
    }
}
