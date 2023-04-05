package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;


public class CustomizationCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private CustomizationCtrl customizationCtrl;
    @FXML
    private Pane toolBar;
    private double x,y;
    @FXML
    private Button closeButton, minimizeButton;
    
    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    public void init(Stage stage){
        this.stage=stage;
        
        toolBar.setOnMousePressed( mouseEvent -> {
            this.x= mouseEvent.getSceneX();
            this.y= mouseEvent.getSceneY();
        });
        toolBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-this.x);
            stage.setY(mouseEvent.getScreenY()-this.y);
        });
    }

    public void close(){
        this.stage.close();
    }
    
    public void minimize(){
        this.stage.setIconified(true);
    }
    
}
