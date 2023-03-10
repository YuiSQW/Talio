package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;

public class ListOverviewCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    @FXML
    private Button addCard;
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
    }
}