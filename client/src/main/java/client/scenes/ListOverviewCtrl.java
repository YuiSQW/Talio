package client.scenes;

import client.utils.ServerUtils;
import commons.BoardList;
import commons.Card;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ListOverviewCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private BoardOverviewCtrl boardOverviewCtrl;
    private BoardList list;
    @FXML
    private Button addCard, addList, closeButton,minimizeButton;
    @FXML
    private TextField listName;
    @FXML
    private Pane toolBar;
    @FXML
    private ListView<String> listView;
    private ObservableList<String> oLcards;
    private double x,y;

    @Inject
    public ListOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    /**
     * The function initializes the functionality of dragging the
     * window of the application
     * @param stage the primary stage of the application
     */
    public void init(Stage stage, BoardOverviewCtrl boardOverviewCtrl){
        this.stage=stage;
        this.boardOverviewCtrl=boardOverviewCtrl;
        this.list= new BoardList(this.listName.getText(),new ArrayList<Card>());
        this.oLcards = FXCollections.observableArrayList();
        this.listView.setItems(oLcards);

        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });

        //Disable the btn if there is no user input
        addList.disableProperty().bind(listName.textProperty().isEmpty());
        // Update the name of the list based on user input
        this.listName.textProperty().addListener((observable, oldValue, newValue) ->{
            this.list.setName(newValue);
        } );
    }
    public void addNewCard(){
        this.mainCtrl.addCardOverview(this);
    }
    public void saveNewCard(Card card) {
        this.list.getCardList().add(card);
        this.oLcards.add(card.getTitle());
        }

    /**
     * @return the BoardList objects that is associated with the instance of the ListOverviewCtrl class
     */
    public BoardList getBoardList() {
        return this.list;
    }

    /**
     * Saves and adds the BoardList object to the parent board
     */
    public void addNewBoardList() {
        this.list.setParentBoard(this.boardOverviewCtrl.getBoard());
        this.boardOverviewCtrl.addNewVbox(list);
        this.close();
    }

    /**
     * Checks if listName field is empty
     * @return boolean whether empty or not
     */
    public boolean emptyCheck() {
        return Objects.equals(listName.getText(), "");
    }

    /**
     * Clears the listName text-field
     */
    public void clearFields() {
        listName.clear();
    }
    public void close(){
        this.stage.close();
        this.mainCtrl.showBoardOverview();}
    public void minimize(){
        this.mainCtrl.minimizeStage();}



}