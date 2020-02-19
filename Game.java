import javafx.application.Application;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.event.EventHandler;

import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Game extends Application {
    Stage window;
    Group rootSpieler1 = this.makeGrids();
    Group rootSpieler2 = this.makeGrids();
    Group blank = new Group();
    
    Scene scene1 = new Scene(rootSpieler1, 1920, 1080);
    Scene scene2 = new Scene(rootSpieler2, 1920, 1080);
    Scene sceneBlank = new Scene(blank, 1920, 1080);
    
    int intAktuellerSpieler = 0;
    
    public Game() {
    }
    
    public void startGame() {
        window.setTitle("Schiffe versenken!");
        window.setScene(scene1);
        window.show();
        
        this.setzeSchiffe();
    }
    
    private void setzeSchiffe() {
        Canvas canvasHover = new Canvas(1920, 1080);
        rootSpieler1.getChildren().add(canvasHover);
        
        EventHandler<MouseEvent> mouseEvent = new MouseFieldHover(canvasHover);
        canvasHover.addEventHandler(MouseEvent.ANY, mouseEvent);
    }
    
    private void step() {
        
    }
    
    private void waitForNext() {
        
    }
    
    
    //Needs for JavaFX
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setFullScreen(true);
        this.startGame();
    }
    
    private Group makeGrids() {
        Group temporal = new Group();
        
        String[] textBoardX = {"", " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10"};
        String[] textBoardY = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        
        Canvas canvas = new Canvas(1920, 1080);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
        gc.setFont(new Font("Times New Roman", 30));
        
        for(int grid = 0; grid <= 960; grid += 960) {
            int x = 120 + grid;
            int y = 180;
            for(; x <= 840 + grid; x += 72) {
                gc.setStroke(Color.BLACK);
                gc.strokeLine(x, y, x, y+720);
                gc.setStroke(Color.BLUE);
                gc.strokeText(textBoardX[(x-(120+grid))/72], x-55, y-20);
            }
            x -= 72;
            for(; y <= 900; y += 72) {
                gc.setStroke(Color.BLACK);
                gc.strokeLine(x-720, y, x, y);
                gc.setStroke(Color.BLUE);
                gc.strokeText(textBoardY[(y-180)/72], x-760, y-25);
            }
        }
        
        temporal.getChildren().add(canvas);
        return temporal;
    }
}
