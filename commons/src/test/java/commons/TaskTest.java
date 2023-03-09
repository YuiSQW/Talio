package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void IdTest(){
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
