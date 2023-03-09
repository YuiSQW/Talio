package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.BoardList;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class AddCardCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Pane toolBar;
    @FXML
    private Button saveButton, closeButton, minimizeButton;
    private double x,y;

    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }
    //TODO need to change it to private when delegating to other class
    public Card getCard() {
        var p = title.getText();
        var q = description.getText();

        if(!emptyCheck()) {
            System.out.println(p);
            System.out.println(q);
        }

        //TODO delegate card to CardOverviewCtrl to let it store the card in the database
        return new Card(p, q, new BoardList("", new ArrayList<>(), new Board("", new ArrayList<>())));
    }

    //Still has no usage & needs to get replaced when data can get stored in database
    public boolean emptyCheck() {
        return Objects.equals(title.getText(), "") || Objects.equals(description.getText(), "");
    }

    //Still has no usage
    public void cancel() {
        clearFields();
    }


    //Gets used with eventOnMouseClick, not working 100% because every time you click the fields clear
    //TODO delegate this to CardOverviewCtrl so it doesn't get executed more than once
    public void clearFields() {
        title.clear();
        description.clear();
    }
    public void clearTitle() {
        title.clear();
    }
    public void clearDescription() {
        description.clear();
    }
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
    }
    public void closeCard(){
        this.mainCtrl.showBoardOverview();
    }
    public void minimize(){this.mainCtrl.minimizeStage();}

}
