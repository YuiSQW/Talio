package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.inject.Inject;

/**
 * Controller class for handling the creation of a new tag by the user
 */
public class AddTagCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private Board parentBoard;
    private AddCardCtrl addCardCtrl;
    @FXML
    private Pane toolBar;
    @FXML
    private ComboBox<String> colorBox;
    @FXML
    private TextField nameField,colorField;
    @FXML
    private Button closeButton,minimizeButton,saveButton;
    private double x,y;

    @Inject
    public AddTagCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    public void init(Stage stage, BoardOverviewCtrl boardOverviewCtrl,AddCardCtrl addCardCtrl){
        this.stage=stage;
        this.parentBoard=boardOverviewCtrl.getBoard();
        this.addCardCtrl=addCardCtrl;
        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });

        // List of colors the user can choose from
        ObservableList<String> colors= FXCollections.observableArrayList("red","blue","green");
        this.colorBox.setItems(colors);
        this.colorBox.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-background-color:"+item+";");
                }
            }
        });
        // Select and display the desired color
        this.colorBox.setOnAction(event ->{
             String selectedColor = colorBox.getValue();
             this.colorField.setText(selectedColor);

        });

        saveButton.disableProperty().bind(nameField.textProperty().isEmpty());

    }
    public void saveTag(){
        Tag newTag= new Tag(this.nameField.getText(),this.colorField.getText(), this.parentBoard);
        this.addCardCtrl.addTag(newTag);
        System.out.println("New Tag added");
        Platform.runLater(() ->this.serverUtils.postNewTag(newTag,this.parentBoard));
        this.closeTag();
    }

    public void minimize(){
        this.stage.setIconified(true);
    }
    public void clearTitle() {
        nameField.clear();
    }
    public void clearColor(){
        colorField.clear();
    }
    public void cancel() {
        clearTitle();
        clearColor();
    }
    public void closeTag(){
        cancel();
        this.stage.close();
    }


}
