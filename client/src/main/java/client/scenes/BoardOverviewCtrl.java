package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class BoardOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private double x,y;
    @FXML
    private Button addList,closeButton,minimizeButton,maximizeButton;
    @FXML
    private Pane toolBar;
    @FXML
    private Label boardTitle;

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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        },0,1000L);

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

    /**
     * Method that is connected to the maximizeButton of the controller
     * It delegates the function of maximizing the window of the app
     */
    public void MAX_MIN(){ this.mainCtrl.MAX_MIN_Stage();}
    public void addNewList(){
        this.mainCtrl.showListOverview();
    }
    /**
     * Refreshes the overview of the board with all the updates of the database
     */
    public void refresh(){
        //TODO implements the logic related to retrieving the lists and displaying them
    }

}