package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Tag implements Serializable {
    private String tagName;
    private String color;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    // Define the many-to-one relationship between tags and parent board
    @JsonIgnore
    @ManyToOne
    private Board parentBoard; //the Board instance that the tag corresponds to

    // Define the many-to-many relationship between tags and cards
    @ManyToMany
    @JoinTable(
            name = "card_tag",
            joinColumns = @JoinColumn(name="tag_id"),
            inverseJoinColumns = @JoinColumn(name="card_id"))
    private List<Card> cards;

    public Tag(String tagName, String color, Board parentBoard) {
        this.tagName = tagName;
        this.color = color;
        this.parentBoard=parentBoard;
        this.cards=new ArrayList<>();

    }
    public Tag(){
        //default constructor necessary for Jackson operation
    }
    public String getTagName() {
        return tagName;
    }

    public String getColor() {
        return color;
    }

    public void setParentBoard(Board board){
        this.parentBoard=parentBoard;
    }
    public Board getParentBoard(){
        return this.parentBoard;
    }

    public List<Card> getCards() {
        return cards;
    }
    public void addCard(Card card){
        this.cards.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getTagName(), tag.getTagName()) && Objects.equals(getColor(), tag.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagName(), getColor());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagType='" + tagName + '\'' +
                '}';
    }
}
