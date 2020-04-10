package com.github.battleships;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import javafx.event.EventHandler;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Game extends Application {
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    double screenWidth = gd.getDisplayMode().getWidth()*0.6;
    double screenHeight = gd.getDisplayMode().getHeight()*0.6;

    Stage window;

    VBox startGroup = new VBox();
    VBox enterNamesGroup = new VBox();
    StackPane gameGroup = new StackPane();
    VBox waitGroup = new VBox();

    Scene startScene;
    Scene enterNamesScene;
    Scene gameScene;
    Scene waitScene;

    Label lbl;
    double opacity = 0;

    int actualPlayer = 0;

    int [] shipLengths = {5, 4 ,4, 3, 3, 3, 2, 2, 2, 2};
    int actualShip = 0;

    Player [] players = new Player[2];

    ResizableCanvas canvasHover;
    ResizableCanvas showShips;
    EventHandler<MouseEvent> mouseEvent;

    public static void main (String[] args) {
        launch();
    }

    @Override
    public void start (Stage stage) throws Exception {
        window = stage;
        window.setMinWidth(640);
        window.setMinHeight(360);

        this.startGame();
    }

    public void startGame () {
        startGroup.setAlignment(Pos.CENTER);

        lbl = new Label("Battleships!");
        lbl.setFont(new Font("Times New Roman", (90.0 / 1920) * screenWidth));
        lbl.setTranslateY(-100);
        startGroup.getChildren().add(lbl);

        AnimationTimer timer = new fadeIn();
        timer.start();

        Button [] button = {new Button("Singleplayer"), new Button("Multiplayer")};
        button[0].setTranslateY(-30);

        startGroup.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        startGroup.getChildren().addAll(button[0], button[1]);

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            button[i].setOnAction(actionEvent -> this.enterNames(finalI));
            button[i].setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        }

        DropShadow shadow = new DropShadow();
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            button[i].addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> button[finalI].setEffect(shadow));
            int finalI1 = i;
            button[i].addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> button[finalI1].setEffect(null));
        }

        startScene = new Scene(startGroup,  screenWidth, screenHeight);

        window.setTitle("Battleships!");
        window.setScene(startScene);
        window.show();
    }

    private void enterNames (int playerMode) {
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);

        Label label1 = new Label("Name Player 1:");
        TextField textField1 = new TextField ();
        hb.getChildren().addAll(label1, textField1);

        TextField textField2 = new TextField();
        if (playerMode == 1) {
            Label label2 = new Label("Name Player 2:");
            hb.getChildren().addAll(label2, textField2);
        }

        hb.setSpacing(10);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

        enterNamesGroup.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        enterNamesGroup.setAlignment(Pos.CENTER);
        enterNamesGroup.setSpacing(20);
        enterNamesGroup.getChildren().addAll(hb, submit);

        DropShadow shadow = new DropShadow();
        submit.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> submit.setEffect(shadow));
        submit.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> submit.setEffect(null));

        int numberOfShipPoints = 0;
        for (int ship: shipLengths) {
            numberOfShipPoints += ship;
        }

        int finalNumberOfShipPoints = numberOfShipPoints;
        submit.setOnAction(actionEvent ->  {
            players[0] = new Player(textField1.getText(), finalNumberOfShipPoints);
            if (playerMode == 1) {
                players[1] = new Player(textField2.getText(), finalNumberOfShipPoints);
            }
            this.placeShips();
        });

        enterNamesScene = new Scene(enterNamesGroup,  screenWidth, screenHeight);

        window.setScene(enterNamesScene);
    }

    private void placeShips () {
        gameGroup = new StackPane();
        this.resizableCanvasUtility(0);

        showShips = new ResizableCanvas(2, players[actualPlayer]);
        gameGroup.getChildren().add(showShips);
        showShips.widthProperty().bind(gameGroup.widthProperty());
        showShips.heightProperty().bind(gameGroup.heightProperty());

        HBox hb = new HBox();
        hb.setAlignment(Pos.TOP_LEFT);
        Label labelShipPlayer = new Label(players[actualPlayer].getName() + ": Place your ships!");
        labelShipPlayer.setFont(new Font("Times New Roman", (40.0 / 1920) * screenWidth));
        labelShipPlayer.setTranslateX(10); labelShipPlayer.setTranslateY(10);
        hb.getChildren().add(labelShipPlayer);

        gameGroup.getChildren().add(hb);
        gameGroup.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        gameScene = new Scene(gameGroup,  screenWidth, screenHeight);

        window.setScene(gameScene);

        this.utilityPlaceShips();

        gameGroup.setOnMouseClicked(utilityPlaceShipEvent -> {
            if (utilityPlaceShipEvent.getButton() == MouseButton.PRIMARY) {
                showShips.draw(2);
                this.utilityPlaceShips();
            }
        });
    }

    private void utilityPlaceShips () {
        if(actualShip != shipLengths.length) {
            canvasHover = this.resizableCanvasUtility(1);
            mouseEvent = new MousePlaceShip(canvasHover, shipLengths[actualShip], players[actualPlayer]);
            canvasHover.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent);
            players[actualPlayer].myArea.zeigeSpielfeld();
            actualShip += 1;
        } else {
            actualPlayer = (actualPlayer+1)%2;
            actualShip = 0;

            if(actualPlayer == 1) {
                this.waitForNext(0);
            } else {
                waitGroup = new VBox();
                this.waitForNext(1);
            }
        }
    }

    private void step () {

    }

    private void waitForNext (int mode) {
        Label label = new Label("Are you "+players[actualPlayer].getName()+"?");

        Button submit = new Button("Continue");
        submit.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

        waitGroup.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));
        waitGroup.setAlignment(Pos.CENTER);
        waitGroup.setSpacing(20);
        waitGroup.getChildren().addAll(label, submit);

        DropShadow shadow = new DropShadow();
        submit.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> submit.setEffect(shadow));
        submit.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> submit.setEffect(null));

        submit.setOnAction(actionEvent ->  {
            if (mode == 0) {
                this.placeShips();
            } else {
                this.step();
            }
        });

        waitScene = new Scene(waitGroup,  screenWidth, screenHeight);

        window.setScene(waitScene);
    }

    private ResizableCanvas resizableCanvasUtility (int mode) {
        ResizableCanvas canvas = new ResizableCanvas(mode, players[actualPlayer]);

        gameGroup.getChildren().add(canvas);
        canvas.widthProperty().bind(gameGroup.widthProperty());
        canvas.heightProperty().bind(gameGroup.heightProperty());

        return canvas;
    }

    private class fadeIn extends AnimationTimer {
        @Override
        public void handle (long now) {
            opacity += 0.02;
            lbl.opacityProperty().set(opacity);
            if (opacity >= 1) {
                stop();
            }
        }
    }
}