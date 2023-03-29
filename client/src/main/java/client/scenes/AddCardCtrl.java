package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AddCardCtrl {

    private Card currentCard;

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private ListContainerCtrl listContainerCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Pane toolBar;
    @FXML
    private Button saveButton, closeButton, minimizeButton, newTaskButton;
    @FXML
    private Button clearTitleButton, clearDescriptionButton;
    @FXML
    private TilePane tilePane;
    private double x,y;

    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    public void init(Stage stage,ListContainerCtrl listContainerCtrl){
        this.stage=stage;
        this.listContainerCtrl=listContainerCtrl;
        this.title.setText("Title");
        this.description.setText("Description of the task");
        //this.tasks.setText("Subtasks");
        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });

        saveButton.disableProperty().bind(title.textProperty().isEmpty());
    }

    /**
     * Creates new Card object based on the String values of the text fields
     * Delegates the new Card to the parent BoardList
     * If both text fields are empty, no Card object is created and the stage is closed
     */
    public void saveCard(){
        var p = title.getText();
        var q = description.getText();
        Card card= new Card(p,q,this.listContainerCtrl.getList());


        var children=tilePane.getChildren();//get each member of the tilePane
        var tasks=new ArrayList<Task>();
        for(Node node:children){// when the window is closed (and the card is saved) the tasks are added to the db
            if(node.getClass()== TaskContainerCtrl.class) {
                TaskContainerCtrl container = (TaskContainerCtrl) node;
                Task taskToAdd = new Task(currentCard, container.getText());
                tasks.add(taskToAdd);//we add them to a list that is sent to the method that also adds the card to db
            }
        }
        this.currentCard=card;
        this.listContainerCtrl.saveNewCard(card,tasks);
        this.closeCard();
    }
    //Still has no usage & needs to get replaced when data can get stored in database
    public boolean emptyCheck() {
        return Objects.equals(title.getText(), "") || Objects.equals(description.getText(), "");
    }

    //Still has no usage
    //Gets used with eventOnMouseClick, not working 100% because every time you click the fields clear
    public void clearFields() {
        title.clear();
        description.clear();
        clearTasks();
    }
    public void clearTitle() {
        title.clear();
    }
    public void clearDescription(){
        description.clear();
    }
    public void clearTasks(){
        ///loop thru taskContainers and delete the
        Iterator<Node> itr=tilePane.getChildren().iterator();
        while(itr.hasNext()){
            Node node= itr.next();
            if(node.getClass()==TaskContainerCtrl.class)
                itr.remove();
        }
    }

    public void cancel() {
        clearFields();
    }

    public void closeCard(){
        cancel();
        this.stage.close();
    }
    public void minimize(){
        this.stage.setIconified(true);
    }

    public Card getCard(){
        return this.currentCard;
    }

    public void addTask(){
        TaskContainerCtrl taskContainerCtrl=new TaskContainerCtrl(this.mainCtrl,this.serverUtils);
        taskContainerCtrl.init(tilePane,this);
        tilePane.getChildren().add(tilePane.getChildren().size()-1,taskContainerCtrl);
    }

}
