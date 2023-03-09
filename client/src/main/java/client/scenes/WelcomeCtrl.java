package client.scenes;

import javafx.fxml.FXML;
import java.io.IOException;
import java.net.Socket;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class WelcomeCtrl {

    @FXML
    private TextField ipAddressField;

    @FXML
    private Button connectButton;

    private Socket socket;

    @FXML
    public void connectToServer() {
        String ipAddress = ipAddressField.getText();
        try {
            socket = new Socket(ipAddress, 8080);
            //TODO direct the user to the board
        } catch (IOException ioException) {
            System.err.println("Error connecting to server at IP address: " + ipAddress);
            ioException.printStackTrace();
        }

    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            connectToServer();
        }
    }
}
