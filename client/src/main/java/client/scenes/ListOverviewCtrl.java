package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.BoardList;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ListOverviewCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    @FXML
    private Button addCard;
    @FXML
    private Button addList;
    @FXML
    private TextField listName;
    @FXML
    private Pane toolBar;
    @FXML
    private Button closeButton, minimizeButton;
    private double x,y;
    @Inject
    public ListOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }
    public void addNewCard(){
        this.mainCtrl.addCardOverview();
    }
    public void close(){this.mainCtrl.showBoardOverview();}
    public void minimize(){this.mainCtrl.minimizeStage();}

    /**
     * The function initializes the functionality of dragging the
     * window of the application
     * @param stage the primary stage of the application
     */
    public void init(Stage stage){
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

    }

    /**
     * This function creates a new BoardList object and returns it
     * @return new list
     */
    public BoardList getBoardList() {
        var p = listName.getText();
        return new BoardList(p, new ArrayList<Card>(), new Board("", new ArrayList<>()));

    }

    /**
     * Adds a new BoardList
     * For now it prints out the name of the list
     */
    public void AddNewBoardList() {
        if(!emptyCheck()) {
            BoardList list = getBoardList();
            System.out.println(list.getName());
        }

        //TODO delegate listName to the BoardOverview

        close();
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


}