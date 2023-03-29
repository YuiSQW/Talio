package commons;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;

    @JsonIgnore
    @ManyToOne
    private Card parentCard;
    public Task(){
    }

    public Task(Card parentCard,String name){
        this.parentCard=parentCard;
        this.name=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card getParentCard(){
        return this.parentCard;
    }


    public void setParentCard(Card parentCard) {
        this.parentCard = parentCard;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        if(this.name.equals(task.getName()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
