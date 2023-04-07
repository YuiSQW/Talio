package client.scenes;

import client.utils.ServerUtils;
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

public class AddTagCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private BoardOverviewCtrl boardOverviewCtrl;
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
        this.boardOverviewCtrl=boardOverviewCtrl;
        this.addCardCtrl=addCardCtrl;
        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });
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

        this.colorBox.setOnAction(event ->{
             String selectedColor = colorBox.getValue();
             this.colorField.setText(selectedColor);

        });

        saveButton.disableProperty().bind(nameField.textProperty().isEmpty());

    }
    public void saveTag(){
        Tag newTag= new Tag(this.nameField.getText(),this.colorField.getText());
        this.addCardCtrl.addTag(newTag);
        this.addCardCtrl.getAvailableTags().add(newTag);
        Platform.runLater(() ->this.serverUtils.postNewTag(newTag,this.boardOverviewCtrl.getBoard()));
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
        AddCardCtrl.setTagDialogOpen(false);
        this.stage.close();
    }


}
