package client.scenes;

import commons.BoardList;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.geometry.Insets;

/**
 * The List container class which can be used to generate custom Vbox
 *  It's children are a text-field + listview Element.
 *
 */
public class ListContainerCtrl extends VBox {

    /**
     * Constructor to create VBox with all its elements, needed for the user
     * to create their own list
     * @param tilePane the parent which the vbox is part of
     * @param list the BoardList object that is represented by the vbox
     */
    public ListContainerCtrl(TilePane tilePane, BoardList list) {

        TextField listName = new TextField(list.getName());
        listName.setPrefHeight(47.0);
        listName.setPrefWidth(150.0);

        // the close button
        Button removeBtn = new Button("x");
        removeBtn.setPrefHeight(35.0);
        removeBtn.setPrefWidth(34.0);
        removeBtn.setStyle("-fx-background-radius: 1em;");

        // The HBox which needs to hold the listName and removeBtn
        HBox boardListTitle = new HBox(listName, removeBtn);
        boardListTitle.setPrefHeight(38.0);
        boardListTitle.setPrefWidth(214.0);
        boardListTitle.setSpacing(15.0);
        boardListTitle.setPadding(new Insets(10.0));

        // ListView is completed with the titles of the Cards
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(306.0);
        listView.setPrefWidth(214.0);
        ObservableList<String> cards = FXCollections.observableArrayList();
        for(Card card:list.getCardList()){
            cards.add(card.getTitle());
        }
        listView.setItems(cards);

        //Spacing between the nodes, for reference, look at the FXML file
        setSpacing(10.0);
        getChildren().addAll(boardListTitle, listView);

        //Every vbox has the ability to delete itself
        //TODO also delete the ID of the list in the database
        removeBtn.setOnAction(event -> tilePane.getChildren().remove(ListContainerCtrl.this));

    }

    //TODO drag & drop functions

}

