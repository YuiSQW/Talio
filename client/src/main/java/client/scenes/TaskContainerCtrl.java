package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import javax.inject.Inject;

public class TaskContainerCtrl extends VBox{
    private Task task;
    private AddCardCtrl addCardCtrl;

    private MainCtrl mainCtrl;

    private final ServerUtils serverUtils;

    private ListView<Card> listView;

    @Inject
    public TaskContainerCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void init(TilePane tilePane, AddCardCtrl addCardCtrl){
        this.addCardCtrl=addCardCtrl;
        this.task=new Task(addCardCtrl.getCard(),"Empty");
        //this.task=this.serverUtils.postNewTask(this.task,this.addCardCtrl.getCard());
        Button button = new Button("x");
        button.setMinHeight(100.0);
        button.setMaxHeight(100.0);
        button.setPrefHeight(100.0);
        button.setPrefWidth(100.0);
        button.setStyle("-fx-background-radius: 1em;");

        HBox taskHbox=new HBox(button);
        taskHbox.setMinHeight(100.0);
        taskHbox.setMaxHeight(100.0);
        taskHbox.setPrefHeight(200.0);
        taskHbox.setPrefWidth(200.0);
        ///tilePane.setMargin(this, new Insets(10,0,0,0));
        getChildren().addAll(taskHbox);
    }
}
