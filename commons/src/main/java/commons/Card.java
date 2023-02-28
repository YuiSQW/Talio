package commons;



public class Card {
    /**
     * Cards contain a title and a description
     * In the future Cards can contain a List of type 'Task' which is to be implemented
     */

    private String title;
    private String description;

    public Card(String title, String description){
        this.title = title;
        this.description = description;
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

    @Override
    public boolean equals(Object other){
        return other instanceof Card &&
               ((Card)other).title.equals(this.title) &&
               ((Card)other).description.equals(this.description);
    }

}
