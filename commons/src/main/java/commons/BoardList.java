package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_lists")
public class BoardList implements Serializable {
    /**
     * I named the list on the Board "BoardList" to prevent any confusion with java.util.List
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<Card> cardList;
    private String name;

    public BoardList(){

        this.cardList = new ArrayList<Card>();
        this.name = "";
    }

    public BoardList(String name, List<Card> cardList){
        this.name = name;
        this.cardList = cardList;
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

    public void addCard(Card cardToAdd){
        this.cardList.add(cardToAdd);
    }

    public long getId() { return this.id;}

    public void setId(long id) {this.id = id;} //made this for testing, as the db isn't up and running yet.

    public void deleteCard(long id){
        //TODO
        //the id field of Card needs to be added in the future for database purposes
        //to be implemented here: deleting a card from cardList based on its id
    }

    @Override
    public boolean equals(Object other){
        return other instanceof BoardList &&
               ((BoardList) other).cardList.equals(this.cardList) &&
               ((BoardList) other).name.equals(this.name);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
