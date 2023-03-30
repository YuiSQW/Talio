package client.scenes;

import client.utils.ServerUtils;
import client.utils.WebsocketServerUtils;
import commons.Board;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BoardOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    
    private final WebsocketServerUtils websocketServerUtils;
    private Board board;
    private double x,y;
    
    @FXML
    private Button closeButton,minimizeButton,maximizeButton,addList, renameBoardBtn;
    @FXML
    private Pane toolBar;
    @FXML
    private TilePane tilePane;
    @FXML
    private TextField boardTitle;

    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, WebsocketServerUtils websocketServerUtils) {
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
        this.websocketServerUtils = websocketServerUtils;
    }
    /**
     * The function initializes the functionality of dragging the
     * window of the application
     * @param stage the primary stage of the application
     */
    public void init(Stage stage) throws Exception {
        
        //TODO Get the client to choose its board, so that you automatically get a board based on ID,
        // and you don't have to follow the procedure below
        
        /*
         * Note: this is the board, with its id I use, to test syncing
         */
        //this.board = serverUtils.getBoard(330);
        
        //You can delete this line in principle, but then the client sees "Title shortly" instead of the database title
        //this.boardTitle.setText(this.board.getName());
    
        /*
         * ATTENTION: Steps to make sure that the syncing works on your local host (make sure 2 clients connect to the same board)
         * 1. Create a boardObject and propagate it to the database
         * 2. Look in the H2 console or via printing what your board id is
         * 3. When running a second client, comment out the postNewBoard function
         * 4. Use the serverUtils.getBoard method and enter the ID you got from the already existing board.
         */
    
        
        //This board is the one without id
        Board board = new Board(this.boardTitle.getText(), new ArrayList<>());
        
        //Assign the board to the one postNewBoard creates (the one with generated id)
        this.board = serverUtils.postNewBoard(board);
        
    
        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });
    
        //Initializes the socket
        websocketServerUtils.initSocket();
        
        //Makes sure that the client is subscribed to this board
        websocketServerUtils.subscribeToBoard(this.board.id);
    
        //Using AtomicBoolean, because it is more tread safe and to prevent data inconsistency when dealing with multiple threads
        AtomicBoolean userChangesField = new AtomicBoolean(false);
        
        // Set up a Timeline to update the GUI every second, this is better as the timer,
        // because this is mostly used for JavaFX applications, firstly I implemented it with AnimatedTimer,
        // but that took so much CPU resources
        
        Timeline timeline = new Timeline(
                //Call the refresh method every second
                new KeyFrame(Duration.seconds(3), event -> {
                    
                    boolean hasChanged = refresh(userChangesField.get());
                    //Set the userChangesField to the value from the refresh() func
                    userChangesField.set(hasChanged);
                    
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        boardTitle.setOnMousePressed(event -> {
            userChangesField.set(true);
            System.out.println("true");
            //TODO send the changed flag to the server
        });
    
        boardTitle.setOnKeyTyped(event -> {
            userChangesField.set(true);
            System.out.println("true");
            //TODO send the changed flag to the server
        });
    
        
    }
    
    /**
     * The function which is connected to the renameBtn
     * sets the board name to the new title
     * sets the label to the new title
     */
    public void renameBoardTitle(){
        this.board.setName(boardTitle.getText());
        this.board = serverUtils.renameBoard(this.board);
        boardTitle.setText(boardTitle.getText());
        
    }
    
    public Board getBoard() {
        return board;
    }

    /**
     * Connected to the addList button
     */
    public void addNewList(){
        this.addNewVbox();
    }

    /**
     * Method which creates a new ListContainer object
     * which contains a child BoardList instance of the Board
     */
    public void addNewVbox() {
        ListContainerCtrl listContainerCtrl= new ListContainerCtrl(this.mainCtrl,this.serverUtils);
        listContainerCtrl.init(tilePane,this);
        tilePane.getChildren().add((tilePane.getChildren().size() - 1), listContainerCtrl);
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
     *
     * @return the boolean if a client is editing the field
     */
    public boolean refresh(boolean isUserEditing){
        //TODO implements the logic related to retrieving the lists and displaying them
        this.board = websocketServerUtils.getCurrentBoard();
        this.board = serverUtils.getBoard(this.board.id);
        
        //Disables the button when field is either empty, or the same as the value in the database
        renameBoardBtn.disableProperty().bind((boardTitle.textProperty().isEqualTo(serverUtils.getBoard(this.board.id).getName()))
                .or(boardTitle.textProperty().isEmpty()));
        
    
        //If the user is not editing the textField, then you can set the boardTitle textField to the new value
        //Otherwise the clients get constantly interrupted
        if (!isUserEditing) {
            boardTitle.setText(this.board.getName());
            //TODO sent the isUserEditing flag to the server
            
        }
        
        //Return false again after the label has been set
        return false;
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