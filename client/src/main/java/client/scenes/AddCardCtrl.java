package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.util.*;

public class AddCardCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private ListContainerCtrl listContainerCtrl;

    @FXML
    private TextField title;
    @FXML
    private TextArea description,tasks;
    @FXML
    private Pane toolBar;
    @FXML
    private HBox tagBar;
    @FXML
    private ComboBox<Tag> tagBox;
    // The available tags that the user can choose from
    private ObservableList<Tag> availableTags;
    // The tags that are assigned to a Card during its creation
    private Set<Tag> assignedTags;
    @FXML
    private Button saveButton,createTag, closeButton, minimizeButton;
    @FXML
    private Button clearTitleButton, clearDescriptionButton, clearTasksButton;
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
        this.tasks.setText("Subtasks");
        this.assignedTags= new HashSet<Tag>();
        // Fetch the available tags related to the current board
        this.availableTags= FXCollections.observableArrayList(this.listContainerCtrl.getBoardOverviewCtrl()
                .getAvailableTags());
        this.tagBox.setItems(availableTags);
        configureTagBox();

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
        String cardTitle = title.getText();
        String cardDescription = description.getText();
        Card card= new Card(cardTitle,cardDescription,new ArrayList<>(),this.listContainerCtrl.getList());
        Platform.runLater(() ->this.serverUtils.postNewCard(card, this.listContainerCtrl.getList()));
        if(this.assignedTags.size()!=0){
            for(Tag tag:this.assignedTags){
                Platform.runLater(() -> this.serverUtils.addTagToCard(card,tag));
            }
        }
        this.closeCard();
    }

    /**
     * Adds a new tag to the corresponding card based on the selection of the user
     */
    public void addTag(Tag tag){
        if(!this.assignedTags.contains(tag)){
            this.assignedTags.add(tag);
            Label tagLabel= new Label(tag.getTagName());
            tagLabel.setPrefWidth(50.0);
            tagLabel.setPrefHeight(30.0);
            tagLabel.setStyle("-fx-background-color:"+tag.getColor()+";");
            this.tagBar.getChildren().add(0,tagLabel);
        }
        if (!this.availableTags.contains(tag)) {
            this.availableTags.add(tag);
        }
    }
    public void createNewTag(){
        this.mainCtrl.addNewTag(listContainerCtrl.getBoardOverviewCtrl(),this);
    }

    /**
     * Method which configures the ComboBox list cells
     */
    private void configureTagBox() {
        this.tagBox.setCellFactory(listView -> new ListCell<Tag>() {
                @Override
                protected void updateItem(Tag item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTagName());
                        setStyle("-fx-background-color:"+ item.getColor()+";");
                    }
                }
        });
    }

    public ObservableList<Tag> getAvailableTags() {
        return availableTags;
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
        tasks.clear();
        for(int i=1;i<=this.tagBar.getChildren().size()-3;i++){
            this.tagBar.getChildren().remove(0);
        }
    }
    public void clearTitle() {
        title.clear();
    }
    public void clearDescription(){
        description.clear();
    }
    public void clearTasks(){
        tasks.clear();
    }

    public void cancel() {
        clearFields();
    }

    public void closeCard(){
        cancel();
        ListContainerCtrl.setCardDialogOpen(false);
        this.stage.close();
    }
    public void minimize(){
        this.stage.setIconified(true);
    }




}
