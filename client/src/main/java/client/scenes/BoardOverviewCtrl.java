package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;

public class BoardOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private double x,y;
    @FXML
    private Button closeButton, minimizeButton;
    @FXML
    private Pane toolBar;
    @FXML
    private Button addList;
    @FXML
    private TextField boardTitle;

    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
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
     * Function that is connected to the closeButton of the controller
     * It delegates the function of closing the app to the Main Controller
     */
    public void close(){ this.mainCtrl.closeApp();}
    /**
     * Function that is connected to the minimizeButton of the controller
     * It delegates the function of minimizing the window of the app
     * to the Main Controller
     */
    public void minimize(){ this.mainCtrl.minimizeStage();}
    public void addNewList(){
        this.mainCtrl.showListOverview();
    }
}
