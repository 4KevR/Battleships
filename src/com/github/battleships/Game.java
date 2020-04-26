package com.github.battleships;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javafx.event.EventHandler;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class Game extends Application {
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    double screenWidth = gd.getDisplayMode().getWidth()*0.7;
    double screenHeight = gd.getDisplayMode().getHeight()*0.7;

    Stage window;

    VBox startGroup = new VBox();
    VBox enterNamesGroup = new VBox();
    StackPane gameGroup = new StackPane();
    VBox waitGroup = new VBox();
    BorderPane rootGroup = new BorderPane();

    Scene gameScene;

    Label lbl;
    double opacity = 0;

    int actualPlayer = 0;
    int playerMode = 0;

    int [] shipLengths = {5, 4 ,4, 3, 3, 3, 2, 2, 2, 2};
    int actualShip = 0;
    Label labelShipPlayer = new Label();

    Player [] players = new Player[2];
    int cacheHitAttempts = 0;
    int cacheHitShips = 0;

    Button completeStep = new Button("Complete Step");

    DropShadow shadow = new DropShadow();

    ResizableCanvas grid;
    ResizableCanvas canvasHoverPlaceShip;
    ResizableCanvas canvasHoverShoot;
    ResizableCanvas showShips;
    EventHandler<MouseEvent> mouseEvent;

    DoubleProperty mouseAreaHeight = new SimpleDoubleProperty();

    int firstComputerShot = 0;

    public static void main (String[] args) {
        launch();
    }

    @Override
    public void start (Stage stage) throws Exception {
        window = stage;
        window.setMinWidth(640); window.setMinHeight(360);

        gameScene = new Scene(rootGroup, screenWidth, screenHeight);

        MenuBar menuBar = new MenuBar();
        Menu menuView = new Menu("View");

        MenuItem checkMenuItem = new MenuItem("Activate Fullscreen");
        menuView.getItems().add(checkMenuItem);
        checkMenuItem.setOnAction(checkEvent -> {
            window.setFullScreen(true);
            menuBar.setVisible(false);
        });

        menuBar.getMenus().addAll(menuView);

        gameScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                menuBar.setVisible(true);
            }
        });

        rootGroup.setTop(menuBar);
        rootGroup.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null)));

        window.setFullScreenExitHint("To disable the fullscreen, press ESC");
        window.setResizable(false);
        window.setTitle("Battleships!");
        window.setScene(gameScene);
        window.show();
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

        rootGroup.setCenter(startGroup);
    }

    private void enterNames (int playerMode) {
        this.playerMode = playerMode;

        enterNamesGroup.setAlignment(Pos.CENTER);
        enterNamesGroup.setSpacing(20);

        Button submitNames = new Button("Submit");
        submitNames.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(10);

        Label label1 = new Label("Name Player 1:");
        TextField textField1 = new TextField ();
        hb.getChildren().addAll(label1, textField1);

        TextField textField2 = new TextField();
        Slider slider = new Slider(0,5,0);
        if (playerMode == 1) {
            Label label2 = new Label("Name Player 2:");
            hb.getChildren().addAll(label2, textField2);
            enterNamesGroup.getChildren().addAll(hb, submitNames);
        } else {
            slider.setMin(0); slider.setMax(5);
            slider.setValue(1);
            slider.setMinorTickCount(0); slider.setMajorTickUnit(1);
            slider.setSnapToTicks(true); slider.setShowTickMarks(true); slider.setShowTickLabels(true);
            slider.setMaxWidth(screenWidth*0.5);
            slider.setLabelFormatter(new StringConverter<>() {
                @Override
                public String toString(Double n) {
                    if (n < 0.5) return "extremely easy";
                    if (n < 1.5) return "easy";
                    if (n < 2.5) return "normal";
                    if (n < 3.5) return "hard";
                    if (n < 4.5) return "very hard";

                    return "(almost) impossible";
                }

                @Override
                public Double fromString(String s) {
                    switch (s) {
                        case "extremely easy":
                            return 0d;
                        case "easy":
                            return 1d;
                        case "normal":
                            return 2d;
                        case "hard":
                            return 3d;
                        case "very hard":
                            return 4d;
                        case "impossible":
                            return 5d;

                        default:
                            return -1d;
                    }
                }
            });
            enterNamesGroup.getChildren().addAll(hb, slider, submitNames);
        }

        submitNames.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> submitNames.setEffect(shadow));
        submitNames.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> submitNames.setEffect(null));

        submitNames.setOnAction(actionEvent ->  {
            players[0] = new Player(textField1.getText(), shipLengths);
            if (playerMode == 1) {
                players[1] = new Player(textField2.getText(), shipLengths);
            } else {
                players[1] = new ComputerPlayer(shipLengths, (int) slider.getValue());
            }
            rootGroup.setCenter(gameGroup);
            this.placeShips();
        });
        rootGroup.setCenter(enterNamesGroup);
    }

    private void placeShips () {
        if (playerMode == 1 || actualPlayer == 0) {
            //Show grid
            grid = new ResizableCanvas(0, players, actualPlayer);
            gameGroup.getChildren().add(grid);
            grid.widthProperty().bind(rootGroup.widthProperty());
            grid.heightProperty().bind(rootGroup.heightProperty());

            //Show placed ships
            showShips = new ResizableCanvas(2, players, actualPlayer);
            gameGroup.getChildren().add(showShips);
            showShips.widthProperty().bind(rootGroup.widthProperty());
            showShips.heightProperty().bind(rootGroup.heightProperty());

            //Display text
            HBox hb = new HBox();
            hb.setAlignment(Pos.TOP_LEFT);
            labelShipPlayer.setText(players[actualPlayer].getName() + ": Place your ships!");
            labelShipPlayer.setFont(new Font("Times New Roman", (40.0 / 1920) * screenWidth));
            labelShipPlayer.setTranslateX(10);
            labelShipPlayer.setTranslateY(10);
            hb.getChildren().add(labelShipPlayer);

            HBox hb2 = new HBox();
            hb2.setAlignment(Pos.BOTTOM_RIGHT);

            completeStep.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
            completeStep.setTranslateX(-20);
            completeStep.setTranslateY(-40);
            completeStep.setVisible(false);
            completeStep.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> completeStep.setEffect(shadow));
            completeStep.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> completeStep.setEffect(null));
            completeStep.setOnAction(actionEvent -> this.waitForNext(1));

            Button quitGame = new Button("Quit Game");
            quitGame.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
            quitGame.setTranslateX(-10);
            quitGame.setTranslateY(-40);
            quitGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> quitGame.setEffect(shadow));
            quitGame.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> quitGame.setEffect(null));
            quitGame.setOnAction(actionEvent -> System.exit(0));

            hb2.getChildren().addAll(completeStep, quitGame);

            gameGroup.getChildren().addAll(hb, hb2);

            gameScene.heightProperty().addListener(evt -> setNewPropertyHeight(gameScene.getHeight()));
            setNewPropertyHeight(gameScene.getHeight());

            this.utilityPlaceShips();

            gameGroup.setOnMouseClicked(utilityPlaceShipEvent -> {
                if (utilityPlaceShipEvent.getButton() == MouseButton.PRIMARY) {
                    if (players[actualPlayer].placedShips != players[actualPlayer].numberOfShipPoints || actualShip == shipLengths.length) {
                        //Placing ship
                        int shouldPlaceShip = 0;
                        for (int counter = 0; counter < actualShip; counter++) {
                            shouldPlaceShip += shipLengths[counter];
                        }
                        if (players[actualPlayer].placedShips == shouldPlaceShip) {
                            gameGroup.getChildren().remove(canvasHoverPlaceShip);
                            showShips.draw(2, actualPlayer);
                            this.utilityPlaceShips();
                        }
                    } else {
                        //shooting
                        if (cacheHitAttempts != players[(actualPlayer+1)%2].hitAttempts) {
                            cacheHitAttempts = players[(actualPlayer+1)%2].hitAttempts;
                            this.processShot();
                        }
                    }
                }
            });
        }
    }

    public final void setNewPropertyHeight (double value) { mouseAreaHeight.set((value)*(2./3)); }

    private void utilityPlaceShips () {
        if(actualShip != shipLengths.length) {
            canvasHoverPlaceShip = new ResizableCanvas(1, players, actualPlayer);
            gameGroup.getChildren().add(canvasHoverPlaceShip);
            canvasHoverPlaceShip.widthProperty().bind(rootGroup.widthProperty());
            canvasHoverPlaceShip.heightProperty().bind(mouseAreaHeight);

            mouseEvent = new MousePlaceShip(canvasHoverPlaceShip, shipLengths[actualShip], players[actualPlayer]);
            canvasHoverPlaceShip.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent);

            actualShip += 1;
        } else {
            actualShip = 0;

            if((actualPlayer+1)%2 == 1) {
                this.waitForNext(0);
            } else {
                this.waitForNext(1);
            }
        }
    }

    private void step () {
        completeStep.setVisible(false);

        labelShipPlayer.setText(players[actualPlayer].getName() + ": Attack!");

        canvasHoverShoot = new ResizableCanvas(1, players, actualPlayer);
        gameGroup.getChildren().add(canvasHoverShoot);
        canvasHoverShoot.widthProperty().bind(rootGroup.widthProperty());
        canvasHoverShoot.heightProperty().bind(mouseAreaHeight);

        mouseEvent = new MouseShoot(canvasHoverShoot, players[(actualPlayer+1)%2]);
        canvasHoverShoot.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent);

        cacheHitAttempts = players[(actualPlayer+1)%2].hitAttempts;
        cacheHitShips = players[(actualPlayer+1)%2].hitShips;
    }

    private void waitForNext (int mode) {
        if (playerMode == 1) {
            actualPlayer = (actualPlayer+1)%2;

            waitGroup = new VBox();

            Label label = new Label("Are you " + players[actualPlayer].getName() + "?");

            Button submit = new Button("Continue");
            submit.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

            waitGroup.setAlignment(Pos.CENTER);
            waitGroup.setSpacing(20);
            waitGroup.getChildren().addAll(label, submit);

            DropShadow shadow = new DropShadow();
            submit.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    e -> submit.setEffect(shadow));
            submit.addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> submit.setEffect(null));

            submit.setOnAction(actionEvent -> {
                if (mode == 0) {
                    gameGroup = new StackPane();
                    rootGroup.setCenter(gameGroup);
                    this.placeShips();
                } else {
                    showShips.draw(2, actualPlayer);
                    rootGroup.setCenter(gameGroup);
                    this.step();
                }
            });
            rootGroup.setCenter(waitGroup);
        } else {
            if (firstComputerShot != 0) {
                completeStep.setVisible(false);
                actualPlayer = 1;
                AnimationTimer computerShot = new animateComputerShot();
                computerShot.start();
                labelShipPlayer.setText("Computer is playing...");
            } else {
                firstComputerShot++;
                step();
            }
        }
    }

    private int processShot () {
        if (playerMode == 0 && actualPlayer == 1) {
            showShips.draw(2, 0);
        } else {
            showShips.draw(2, actualPlayer);
        }
        if (players[(actualPlayer+1)%2].lastShot == 4) {
            labelShipPlayer.setText(players[actualPlayer].getName()+": Won game!");
            gameGroup.getChildren().remove(canvasHoverShoot);
            return 2;
        } else if (players[(actualPlayer+1)%2].lastShot == 3) {
            labelShipPlayer.setText(players[actualPlayer].getName()+": Ship destroyed!");
        } else if (players[(actualPlayer+1)%2].lastShot == 2) {
            labelShipPlayer.setText(players[actualPlayer].getName()+": Hit ship!");
        } else {
            if (playerMode == 1 || (playerMode == 0 && actualPlayer == 0)) {
                gameGroup.getChildren().remove(canvasHoverShoot);
                completeStep.setVisible(true);
            }
            labelShipPlayer.setText(players[actualPlayer].getName()+": Hit water!");
            return 1;
        }
        return 0;
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

    private class animateComputerShot extends AnimationTimer {
        int timer = 0;
        int end = 0;
        @Override
        public void handle (long now) {
            timer++;
            if (timer == 50) {
                if (end == 0) {
                    timer = 0;
                    players[0].computerShot(((ComputerPlayer)players[1]).difficulty);
                    end = processShot();
                } else {
                    if (end == 1) {
                        actualPlayer = 0;
                        step();
                    }
                    stop();
                }
            }
        }
    }
}