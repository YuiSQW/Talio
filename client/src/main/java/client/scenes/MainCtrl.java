/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;
    private CardOverviewCtrl cardOverviewCtrl;
    private Scene cardOverview;

    private AddCardCtrl addCardCtrl;
    private Scene addCardOverview;

    private ListOverviewCtrl listOverviewCtrl;
    private Scene listOverview;
    private WelcomeCtrl welcomeCtrl;
    private Scene welcome;

    public void initialize(Stage primaryStage,
                           Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<BoardOverviewCtrl, Parent> board,
                           Pair<ListOverviewCtrl, Parent> list,
                           Pair<CardOverviewCtrl, Parent> card,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<WelcomeCtrl, Parent> welcome) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
        this.boardOverviewCtrl=board.getKey();
        this.boardOverview= new Scene(board.getValue());

        this.listOverviewCtrl=list.getKey();
        this.listOverview=new Scene(list.getValue());

        this.welcomeCtrl = welcome.getKey();
        this.welcome = new Scene(welcome.getValue());

        this.cardOverviewCtrl=card.getKey();
        this.cardOverview=new Scene(card.getValue());
        this.addCardCtrl=addCard.getKey();
        this.addCardOverview=new Scene(addCard.getValue());
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        //showWelcomeOverview();
        showBoardOverview();
        primaryStage.show();
    }
    public void showBoardOverview(){
        primaryStage.setTitle("Main Board");
        boardOverview.setFill(Color.TRANSPARENT);
        primaryStage.setScene(boardOverview);

        this.boardOverviewCtrl.init(primaryStage);
    }

    public void showWelcomeOverview() {
        primaryStage.setTitle("Welcome");
        boardOverview.setFill(Color.TRANSPARENT);
        primaryStage.setScene(welcome);

    }
    public void showCardOverview(){
        Stage secondaryStage = new Stage(StageStyle.TRANSPARENT);
        secondaryStage.setTitle("Card Overview:");
        secondaryStage.show();
        cardOverview.setFill(Color.TRANSPARENT);
        secondaryStage.setScene(this.cardOverview);
        this.cardOverviewCtrl.init(secondaryStage);
    }
    public void addCardOverview(ListOverviewCtrl listOverviewCtrl){
        Stage secondaryStage = new Stage(StageStyle.TRANSPARENT);
        secondaryStage.setTitle("Add new Card:");
        secondaryStage.show();
        addCardOverview.setFill(Color.TRANSPARENT);
        secondaryStage.setScene(this.addCardOverview);
        this.addCardCtrl.init(secondaryStage,listOverviewCtrl);
    }
    public void showListOverview(BoardOverviewCtrl boardOverviewCtrl){
        Stage secondaryStage = new Stage(StageStyle.TRANSPARENT);
        secondaryStage.setTitle("Add new List");
        secondaryStage.show();
        listOverview.setFill(Color.TRANSPARENT);
        secondaryStage.setScene(this.listOverview);
        this.listOverviewCtrl.init(secondaryStage,boardOverviewCtrl);
    }

    /**
     * Method which causes the primary stage of the app to be close
     */
    public void closeApp(){
        Platform.exit();
    }
    /**
     * Method which causes the primary stage of the application to be minimized
     */
    public void minimizeStage(){
        this.primaryStage.setIconified(true);
    }
    /**
     * Method which causes the primary stage of the application to fill up the screen
     */
    public void maxMinStage(){
        this.primaryStage.setMaximized(!this.primaryStage.isMaximized());
    }

    //   OBSOLETE METHOD FOR QUOTE APPLICATION
    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    //    OBSOLETE METHOD FOR THE QUOTE APPLICATION
    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }



}