package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.inject.Inject;


public class CustomizationCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Stage stage;
    private CustomizationCtrl customizationCtrl;
    private BoardOverviewCtrl boardOverviewCtrl;
    private ListContainerCtrl listContainerCtrl;
    @FXML
    private Pane toolBar;
    private double x,y;
    @FXML
    private Button closeButton, minimizeButton, boardreset, listreset;
    @FXML
    private ColorPicker boardbackgroundcp, boardfontcp, listbackgroundcp, listfontcp;
    
    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, ServerUtils serverUtils){
        this.mainCtrl=mainCtrl;
        this.serverUtils=serverUtils;
    }

    public void init(Stage stage, BoardOverviewCtrl boardOverviewCtrl){
        this.stage=stage;
        this.boardOverviewCtrl = boardOverviewCtrl;
        
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

    public void boardreset(){
        //reseting the background and colorpicker
        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, null, null);
        Background background = new Background(backgroundFill);
        boardOverviewCtrl.gettilepane().setBackground(background);
        boardbackgroundcp.setValue(Color.WHITE);

        //reseting the font and colorpicker
        boardOverviewCtrl.getboardTitle().setStyle(null);
    }

    public void boardchangebackground(){
        BackgroundFill backgroundFill = new BackgroundFill(boardbackgroundcp.getValue(), null, null);
        Background background = new Background(backgroundFill);
        boardOverviewCtrl.gettilepane().setBackground(background);
    }

    public void boardchangefont(){
        Color value = boardfontcp.getValue();
        String style = String.format("-fx-text-fill: %s;", toHexString(value));
        boardOverviewCtrl.getboardTitle().setStyle(style);
    }

    private static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
    
        return String.format("#%08X", (r + g + b + a));
    }

    public void listreset(){

    }

    public void listchangebackground(){
        boardOverviewCtrl.setlistcolor(listbackgroundcp.getValue());
    }

    public void listchangefont(){
        boardOverviewCtrl.setlistnamecolor(listfontcp.getValue());
    }

    
}
