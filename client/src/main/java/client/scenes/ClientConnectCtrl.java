package client.scenes;


import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;


public class ClientConnectCtrl {

    private final ServerUtils serverUtils;

    private final MainCtrl mainCtrl;
    @FXML
    private TextField ipAddressField;

    @FXML
    private Button connectButton;

    /**
     * Constructor of the ClientConnectCtrl class
     * @param serverUtils to interact with the server through HTTP
     * @param mainCtrl the main controller
     */
    @Inject
    public ClientConnectCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This method is called when the 'Connect' button is clicked.
     * (Or when enter is pressed, see the handleKeyPressed method below)
     * It retrieves the IP address entered in the text field and tries to connect to the server.
     * (See connect() method in ServerUtils)
     * When connection is successful, the board overview is shown.
     * If the IP address is invalid, the invalidIpAddressAlert pops up.
     */
    @FXML
    public void connectToServer() {
        String ipAddress = ipAddressField.getText();

        if(ipAddress == null || ipAddress.length() == 0){
            invalidIpAddressAlert();
        }
        else {
            try{
                serverUtils.connect(ipAddress);
                mainCtrl.showBoardOverview();
            }
            catch (Exception exception){
                invalidIpAddressAlert();
            }
        }
    }

    /**
     * This method is called when the user tries to connect, but entered an invalid IP address.
     * We use an alert box with a message as a popup.
     * The user can then click OK and try again.
     */
    public void invalidIpAddressAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid IP address of a Talio server", ButtonType.OK);
        alert.setHeaderText("Invalid IP address");
        alert.showAndWait();
    }

    /**
     * This method is called when a key is pressed in the text field.
     * It checks if the pressed key is the Enter key and calls connectToServer() if it is.
     * Pressing enter is thus equivalent to clicking the connect button.
     * @param event The key event
     */
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            connectToServer();
        }
    }
}
