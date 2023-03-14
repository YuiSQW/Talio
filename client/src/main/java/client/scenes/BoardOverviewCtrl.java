package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
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
    private TilePane tilePane;

    //This hBox is right now just for testing which design is better
    @FXML
    private HBox hBox;


    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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

        //Function to get a new List template
        addNewVbox();
    }

    public  void addNewVbox() {
        //Creates a child right after the last added list
        tilePane.getChildren().add((tilePane.getChildren().size() - 1), new ListContainerCtrl(tilePane));

    }

    /**
     * Retrieves the list based on the order of the tilePane parent
     * right now it doesn't have any usage
     * @param num which list needs to be retrieved
     * @return the vBox with its elements
     */
    public VBox getChild(int num) {
        return (VBox) tilePane.getChildren().get(num);
    }


}