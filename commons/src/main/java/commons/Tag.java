package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag{
    private final String tagType;
    private final String color;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
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
    public String getTagType() {
        return tagType;
    }

    public String getColor() {
        return color;
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
