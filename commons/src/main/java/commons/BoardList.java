package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BoardList{
    /**
     * I named the list on the Board "BoardList" to prevent any confusion with java.util.List
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(mappedBy = "parentList")
    private List<Card> cardList;

    @JsonIgnore //this field needs to be ignored if converted to json, since it would otherwise be stuck in infinite loop
    @ManyToOne
    private Board parentBoard; // the parent Board of this BoardList
    private String name;


    BoardList(){
        //default constructor that is necessary for Jackson to work, don't use !!
    }


    public BoardList(String name, List<Card> cardList, Board parentBoard){
        this.name = name;
        this.cardList = cardList;
        this.parentBoard = parentBoard;
    }

    public List<Card> getCardList(){
        return this.cardList;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCardList(List<Card> cardList){
        this.cardList = cardList;
    }


    @Override
    public boolean equals(Object other){
        return other instanceof BoardList &&
               ((BoardList) other).cardList.equals(this.cardList) &&
               ((BoardList) other).name.equals(this.name) &&
               ((BoardList) other).parentBoard.equals(this.parentBoard);
    }

}
