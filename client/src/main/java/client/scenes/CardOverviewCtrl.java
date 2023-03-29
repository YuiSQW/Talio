package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;

public class CardOverviewCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private ListContainerCtrl listContainerCtrl;
    // The Card object that is displayed
    private Card card;
    private int cardIndex;
    @FXML
    private Button saveButton,removeButton, closeButton,minimizeButton;
    @FXML
    private Pane toolBar;
    @FXML
    private TextField title;
    @FXML
    private TextArea description,tasks;
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
    public void init(Stage stage, ListContainerCtrl listContainerCtrl,Card card){
        this.stage=stage;
        this.listContainerCtrl=listContainerCtrl;
        this.card=card;
        this.cardIndex=this.listContainerCtrl.getList().getCardList().indexOf(this.card);
        this.title.setText(this.card.getTitle());
        this.description.setText(this.card.getDescription());
//        this.tasks.setText(this.card.getTasks().toString());
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

    public void saveChanges(){
        if(!this.title.getText().equals(this.card.getTitle())){
            this.card.setTitle(this.title.getText());
        }
        if(!this.description.getText().equals(this.card.getDescription())){
            this.card.setDescription(this.description.getText());
        }
        this.listContainerCtrl.updateCard(this.card,this.cardIndex);
        this.closeCard();
    }
    public void removeCard(){
        this.listContainerCtrl.removeCard(this.card);
        this.card.setParentList(null);
        closeCard();
    }
    /**
     * Method linked to the closeButton which switches the scene of the primary stage to the BoardOverview
     * after the changes to card were/were not saved
     */
    public void clearFields() {
        this.title.clear();
        this.description.clear();
        this.tasks.clear();
    }
    public void clearTitle() {
        this.title.clear();
    }
    public void cancel() {
        clearFields();
    }
    public void clearDescription() {
        this.description.clear();
    }
    public void clearTasks(){
        this.tasks.clear();
    }
    public void closeCard(){
        cancel();
        this.stage.close();
    }
    public void minimize(){
        this.stage.setIconified(true);}
}