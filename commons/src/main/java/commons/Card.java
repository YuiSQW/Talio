package commons;



import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Card {
    /**
     * Cards contain a title and a description
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(mappedBy = "parentCard", fetch = FetchType.EAGER)
    private List<Task> taskList;

    @JsonIgnore //this field needs to be ignored if converted to json, since it would otherwise be stuck in infinite loop
    @ManyToOne
    private BoardList parentList; //the parent of this card
    private String title;
    private String description;

    public Card(){
        // default constructor that is necessary for Jackson, don't use!!
        // Jackson starts complaining if it is private access
    }
    public Card(String title, String description, BoardList parentList){
        this.title = title;
        this.description = description;
        this.parentList = parentList;
    }
    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setParentList(BoardList list){this.parentList = list;}

    public BoardList getParentList(){
        return this.parentList;
    }
    @Override
    public boolean equals(Object other){
        return other instanceof Card &&
               ((Card)other).title.equals(this.title) &&
               ((Card)other).description.equals(this.description) &&
               ((Card)other).parentList.equals(this.parentList);
    }

}
