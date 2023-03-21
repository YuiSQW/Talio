package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.BoardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BoardOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private Board board;
    private double x,y;
    @FXML
    private Button closeButton,minimizeButton,maximizeButton,addList;
    @FXML
    private Pane toolBar;
    @FXML
    private TilePane tilePane;
    @FXML
    private TextField boardTitle;

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
        this.board=new Board(this.boardTitle.getText(),new ArrayList<BoardList>());
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
        //Update the name of the board based on user input
        this.boardTitle.textProperty().addListener((observable,oldValue,newValue )->{
            this.board.setName(newValue);
        });
    }
    public Board getBoard() {
        return board;
    }

    /**
     * Connected to the Add List button
     */
    public void addNewList(){
        this.mainCtrl.showListOverview(this);
    }

    /**
     * Adds a new list everytime a new list gets created
     * @param - new created child BoardList of the Board
     */
    public void addNewVbox(BoardList list) {
        this.board.addList(list);
        //Creates a child right after the last added list
        tilePane.getChildren().add((tilePane.getChildren().size() - 1), new ListContainerCtrl(tilePane,list));

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

    /**
     * Refreshes the overview of the board with all the updates of the database
     */
    public void refresh(){
        //TODO implements the logic related to retrieving the lists and displaying them
    }
    /**
     * Function that is connected to the closeButton of the controller
     * It delegates the function of closing the app to the Main Controller
     */
    public void close(){
        this.mainCtrl.closeApp();
    }
    /**
     * Function that is connected to the minimizeButton of the controller
     * It delegates the function of minimizing the window of the app
     * to the Main Controller
     */
    public void minimize(){
        this.mainCtrl.minimizeStage();}

    /**
     * Method that is connected to the maximizeButton of the controller
     * It delegates the function of maximizing the window of the app
     */
    public void maxMin(){
        this.mainCtrl.maxMinStage();}
}