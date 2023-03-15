package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;

public class CardOverviewCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    @FXML
    private Button closeButton,minimizeButton;
    @FXML
    private Pane toolBar;
    private double x,y;
    @Inject
    public CardOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
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
    /**
     * Method linked to the closeButton which switches the scene of the primary stage to the BoardOverview
     * after the changes to card were/were not saved
     */
    public void closeCard(){
        this.stage.close();
        this.mainCtrl.showBoardOverview();
    }
    public void minimizeStage(){ this.stage.setIconified(true);}
}