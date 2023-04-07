package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag implements Serializable {
    private String tagType;
    // Store the hex code of the color
    private String color;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @JsonIgnore
    @ManyToOne
    private Board parentBoard; //the Board instance that the tag corresponds to
    @ManyToMany
    @JoinTable(
            name = "card_tag",
            joinColumns = @JoinColumn(name="tag_id"),
            inverseJoinColumns = @JoinColumn(name="card_id"))
    private Set<Card> cards;

    public Tag(String tagType, String color) {
        this.tagType = tagType;
        this.color = color;
        this.cards= new HashSet<Card>();

    }
    public Tag(){
        //default constructor necessary for Jackson operation
    }
    public String getTagType() {
        return tagType;
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

    public Set<Card> getCards() {
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
        return Objects.equals(getTagType(), tag.getTagType()) && Objects.equals(getColor(), tag.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagType(), getColor());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagType='" + tagType + '\'' +
                '}';
    }
}
