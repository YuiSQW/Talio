package client.scenes;

import client.utils.ServerUtils;
import commons.BoardList;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.geometry.Insets;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * The List container class which can be used to generate custom Vbox
 *
 */
public class ListContainerCtrl extends VBox {
    // The BoardList object which is contained in the ListContainer instance
    private BoardList list;
    // The Controller of the parent Board of the BoardList instance
    private BoardOverviewCtrl boardOverviewCtrl;
    // ObservableList which holds the Card objects of the BoardList
    private ObservableList<Card> cards;
    // ListView which displays the content of the ObservableList instance
    private ListView<Card> listView;
    private MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    
    private static boolean hasChangedFlag;
    @Inject
    public ListContainerCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }
    /**
     * Constructor to create VBox with all its elements, needed for the user
     * to create their own list
     * @param tilePane the parent which the vbox is part of
     * @param boardOverviewCtrl the Controller of the parent Board
     */
    public void init(TilePane tilePane, BoardOverviewCtrl boardOverviewCtrl, BoardList boardList) {

        this.boardOverviewCtrl=boardOverviewCtrl;
        // Creates the new BoardList object and sets it parent Board
        if(boardList==null){
            this.list= new BoardList("Empty List",new ArrayList<Card>(),this.boardOverviewCtrl.getBoard());
            this.list = serverUtils.postNewList(this.list, this.boardOverviewCtrl.getBoard());
            this.cards=FXCollections.observableArrayList();
        } else{
            this.list=boardList;
            this.cards = FXCollections.observableArrayList();
            this.cards.addAll(this.list.getCardList());
        }

        Label listName = new Label(this.list.getName());
        listName.setPrefHeight(47.0);
        listName.setPrefWidth(100.0);
        listName.setStyle("-fx-text-alignment:center;");
        // the edit button
        Button editButton = new Button("Edit");
        editButton.setPrefHeight(35.0);
        editButton.setPrefWidth(50.0);
        editButton.setStyle("-fx-background-radius:1em;");
        // the close button
        Button removeBtn = new Button("x");
        removeBtn.setPrefHeight(35.0);
        removeBtn.setPrefWidth(34.0);
        removeBtn.setStyle("-fx-background-radius: 1em;");

        // The HBox which needs to hold the listName and removeBtn
        HBox boardListTitle = new HBox(listName,editButton, removeBtn);
        boardListTitle.setPrefHeight(38.0);
        boardListTitle.setPrefWidth(214.0);
        boardListTitle.setSpacing(15.0);
        boardListTitle.setPadding(new Insets(10.0));

        // ListView is completed with the titles of the Cards
        ListView<Card> listView = new ListView<>();
        this.listView=listView;
        listView.setPrefHeight(306.0);
        listView.setPrefWidth(215.0);
        listView.setItems(cards);
        
        // Set up the Custom CellFactory of the ListView which describes how are
        // the cells of the list view generated (value and encapsulated Card object)
        // and how are the cells going to interact with the user
        listView.setCellFactory(list -> {
            ListCell<Card> cell = new ListCell<Card>() {
                @Override
                protected void updateItem(Card card, boolean empty) {
                    super.updateItem(card, empty);
                    if (empty || card == null) {
                        setText(null);
                    } else {
                        setText(card.getTitle());
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if(cell.getText()!=null) {
                    Card item = cell.getItem();
                    this.mainCtrl.showCardOverview(this, item);
                }
            });
            return cell;
        });

        // Button which enables adding new Card objects to the ListView
        Button addCardButton = new Button("+ New Card");
        addCardButton.setPrefHeight(34.0);
        addCardButton.setPrefWidth(100.0);
        addCardButton.setStyle("-fx-background-radius: 1em;");

        // The nested VBox which needs to hold the listView and addCardButton
        VBox cardList= new VBox(listView,addCardButton);
        cardList.setPrefWidth(215.0);
        cardList.setPrefHeight(350.0);
        cardList.setSpacing(10.0);
        cardList.setPadding(new Insets(10.0));
        addCardButton.setAlignment(Pos.BOTTOM_CENTER);

        //Spacing between the nodes, for reference, look at the FXML file
        setSpacing(10.0);
        getChildren().addAll(boardListTitle, cardList);
        setStyle("-fx-border-color:black;-fx-border-radius:15;");

        //Adds spacing between the rows of lists
        TilePane.setMargin(this, new Insets(10,0,0,0));

        //Every vbox has the ability to delete itself
        removeBtn.setOnAction(event -> {
            tilePane.getChildren().remove(ListContainerCtrl.this);
            serverUtils.deleteList(this.list);
        });
        addCardButton.setOnAction(event -> {
            this.mainCtrl.addCardOverview(this);
            
        });
        editButton.setOnAction(event -> this.mainCtrl.editListName(this));

    }

    /**
     * Getter for the BoardList instance contained in the ListContainer
     * @return the reference to the BoardList instance
     */
    public BoardList getList(){
        return this.list;
    }

    /**
     * Method which updates the display of the Card object whose properties have been modified
     * @param card the Card object that has undergone changes
     * @param cardIndex the index of the before mentioned Card object
     */
    public void updateCard(Card card, int cardIndex){
        this.cards.set(cardIndex,card);
    }

    /**
     * Method which saves a new Card object to the BoardList instance of the container
     * Also responsible for displaying the new Card in the ListView of the VBox
     * @param card the new Card object
     */
    public void saveNewCard(Card card) {
        //Adds the card with id and not the old one
        //Create a new card, so that the old one (without id) doesn't get used anymore
        Card newCard = serverUtils.postNewCard(card, this.list);
        //Add the Card with ID to the lists
        this.cards.add(newCard);
        this.list.getCardList().add(newCard);
        
    }

    /**
     * Method which updates the name of the BoardList instance of the container
     * Also responsible for changing the text value of the listName label
     * @param newName the new String value
     */
    public void updateListName(String newName){
        this.getList().setName(newName);
        HBox hbox=(HBox) this.getChildren().get(0);
        Label listName=(Label)hbox.getChildren().get(0);
        listName.setText(newName);
        //The new name gets saved to the server
        serverUtils.renameList(this.list, this.boardOverviewCtrl.getBoard());
        
    }

    /**
     * Method which removes a particular existing Card object from both the
     * BoardList instance and the ObservableList<Card> instance of the container
     * @param card the Card object to be removed
     */
    public void removeCard(Card card){
        this.list.getCardList().remove(card);
        this.cards.remove(card);
        //Delete the card from the server
        serverUtils.deleteCard(card);
    
    
    
    }


    //TODO drag & drop functions

}

