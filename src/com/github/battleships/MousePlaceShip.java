package com.github.battleships;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.util.Arrays;


public class MousePlaceShip implements EventHandler<MouseEvent> {
    Player player;
    ResizableCanvas canvasHover;
    GraphicsContext hover;
    int [] shipLength = new int[2];
    int alignment = 0;
    int valueShip = 0;
    int [] shipPos = new int[2];

    public MousePlaceShip (ResizableCanvas canvasHover, int shipLength, Player player) {
        this.player = player;
        this.canvasHover = canvasHover;
        this.hover = canvasHover.getGraphicsContext2D();
        this.shipLength[0] = shipLength; this.shipLength[1] = 1;
        canvasHover.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                int temp = this.shipLength[0];
                this.shipLength[0] = this.shipLength[1];
                this.shipLength[1] = temp;
                this.alignment = (this.alignment+1)%2;
                handle(mouseEvent);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY || valueShip == 1) {
                hover.clearRect(0, 0, canvasHover.getWidth(), canvasHover.getHeight());
                this.player.placeShip(shipPos, this.alignment, this.shipLength[alignment]);
            }
        });
        canvasHover.requestFocus();
    }

    public void handle (MouseEvent ev){
        double x = ev.getX();
        double y = ev.getY();

        double width = canvasHover.getWidth();
        double height = canvasHover.getHeight();

        hover.clearRect(0, 0, canvasHover.getWidth(), canvasHover.getHeight());

        if (x > (120.0/1920)*width && x < (840.0/1920)*width && y > (180.0/1080)*height && y < (900.0/1080)*height) {
            int [] temp = new int[2];

            hover.setFill(Color.GREEN);

            int posX = (int)((x-((120.0/1920)*width))/((72.0/1920)*width));
            int posY = (int)((y-((180.0/1080)*height))/((72.0/1080)*height));

            int countShips = 0;
            for (int counter = 0; counter < shipLength[alignment]; counter++) {
                int [] field = {posX, posY};
                if (alignment == 0) {field[0] += counter;}
                else {field[1] += counter;}
                countShips += player.countShips(field);
            }

            if (posX+shipLength[0] > 10 || posY+shipLength[1] > 10 || countShips > 0) {
                hover.setFill(Color.RED);
                if (posX+shipLength[0] > 10) {temp[1] = shipLength[0]; shipLength[0] = 10-posX;}
                if (posY+shipLength[1] > 10) {temp[0] = 1; temp[1] = shipLength[1]; shipLength[1] = 10-posY;}
                valueShip = 0;
            } else {valueShip = 1; shipPos[0] = posX; shipPos[1] = posY;}
            hover.fillRect((120.0/1920)*width+((72.0/1920)*width*posX),
                    (180.0/1080)*height+((72.0/1080)*height*posY),
                    (72.0/1920)*width*shipLength[0], (72.0/1080)*height*shipLength[1]);
            if (temp[1] > 0) {
                shipLength[temp[0]] = temp[1];
            }
        }
    }
}