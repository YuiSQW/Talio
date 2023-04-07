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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCardCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private ListContainerCtrl listContainerCtrl;
    private static boolean tagDialogOpen;

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
    private List<Tag> assignedTags;
    @FXML
    private Button saveButton, closeButton, minimizeButton;
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
        this.assignedTags= new ArrayList<Tag>();
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
        Card card= new Card(cardTitle,cardDescription,this.listContainerCtrl.getList());

        Platform.runLater(() ->this.serverUtils.postNewCard(card, this.listContainerCtrl.getList()));
        for(Tag tag:this.assignedTags){
            Platform.runLater(() -> this.serverUtils.addTagToCard(card,tag));
        }
        this.closeCard();
    }

    /**
     * Adds a new tag to the corresponding card from the list of available tags
     */
    public void addTag(Tag tag){
        if(!this.assignedTags.contains(tag)){
            this.assignedTags.add(tag);
            Label tagLabel= new Label(tag.getTagType());
            tagLabel.setPrefWidth(50.0);
            tagLabel.setPrefHeight(30.0);
            tagLabel.setStyle("-fx-background-color:"+tag.getColor()+";");
            tagLabel.setStyle("-fx-background-radius: 1em;-fx-alignment:center;");
            tagLabel.setStyle("-fx-border-color: black;-fx-border-radius: 1em;");
            this.tagBar.getChildren().add(0,tagLabel);
        }
    }

    /**
     * Method which configures the ComboBox and Tags functionality
     */
    private void configureTagBox() {
        this.tagBox.setCellFactory(listView -> {
            ListCell<Tag> cell = new ListCell<Tag>() {
                @Override
                protected void updateItem(Tag item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTagType());
                        setStyle("-fx-background-color:" + item.getColor() + ";");
                    }
                }
            };

            return cell;
        });
        Tag mock = new Tag("Create Tag", "white");
        availableTags.add(mock);

        this.tagBox.setOnAction(event -> {
            Tag selectedTag = tagBox.getValue();
            if (selectedTag.getTagType().equals("Create Tag")) {
                if (!tagDialogOpen) {
                    tagDialogOpen=true;
                    this.mainCtrl.addNewTag(this.listContainerCtrl.getBoardOverviewCtrl(), this);
                }
            }
            else {
                addTag(selectedTag);
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
        for(int i=1;i<=this.tagBar.getChildren().size()-2;i++){
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
    public static void setTagDialogOpen(boolean valToSet){
        tagDialogOpen = valToSet;
    }



}
