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
import java.util.Objects;

/**
 * The class with it responsible for giving the CardOverview control
 */
public class AddCardCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private ListOverviewCtrl listOverviewCtrl;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private Pane toolBar;
    @FXML
    private Button saveButton, closeButton, minimizeButton;
    private double x,y;

    @Inject
    public AddCardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    /**
     * The function initializes the functionality of dragging the
     * window of the application
     * @param stage the primary stage of the application
     */
    public void init(Stage stage,ListOverviewCtrl listOverviewCtrl){
        this.stage=stage;
        this.listOverviewCtrl=listOverviewCtrl;
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
        Card card= new Card(p,q,this.listOverviewCtrl.getBoardList());
        this.listOverviewCtrl.saveNewCard(card);
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
    }
    public void clearTitle() {
        title.clear();
    }
    public void cancel() {
        clearFields();
    }
    public void clearDescription() {
        description.clear();
    }
    public void closeCard(){
        cancel();
        this.stage.close();
    }
    public void minimize() {
        this.stage.setIconified(true);
    }


}
