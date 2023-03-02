package client.scenes;

import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Objects;

public class AddCardCtrl {

    @FXML
    private TextField title;
    @FXML
    private TextArea description;


    //TODO need to change it to private when delegating to other class
    public Card getCard() {
        var p = title.getText();
        var q = description.getText();

        if(!emptyCheck()) {
            System.out.println(p);
            System.out.println(q);
        }

        //TODO delegate card to CardOverviewCtrl to let it store the card in the database
        return new Card(p, q);
    }

    //Still has no usage & needs to get replaced when data can get stored in database
    public boolean emptyCheck() {
        return Objects.equals(title.getText(), "") || Objects.equals(description.getText(), "");
    }

    //Still has no usage
    public void cancel() {
        clearFields();
    }


    //Gets used with eventOnMouseClick, not working 100% because every time you click the fields clear
    //TODO delegate this to CardOverviewCtrl so it doesn't get executed more than once
    public void clearFields() {
        title.clear();
        description.clear();
    }

    public void clearTitle() {
        title.clear();
    }

    public void clearDescription() {
        description.clear();
    }


}
