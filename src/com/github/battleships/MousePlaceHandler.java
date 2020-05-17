package com.github.battleships;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.io.IOException;


public class MousePlaceHandler implements EventHandler<MouseEvent> {
    int mode;
    Player player;
    ResizableCanvas canvasHover;
    GraphicsContext hover;
    int [] shipLength = new int[2];
    int alignment = 0;
    int valuePos = 0;
    int [] pos = new int[2];

    public MousePlaceHandler (int mode, ResizableCanvas canvasHoverPlaceShip, int shipLength, Player player) {
        this.mode = mode;
        this.player = player;
        this.canvasHover = canvasHoverPlaceShip;
        this.hover = this.canvasHover.getGraphicsContext2D();
        this.shipLength[0] = shipLength; this.shipLength[1] = 1;
        this.canvasHover.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                int temp = this.shipLength[0];
                this.shipLength[0] = this.shipLength[1];
                this.shipLength[1] = temp;
                this.alignment = (this.alignment+1)%2;
                handle(mouseEvent);
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY && valuePos == 1) {
                hover.clearRect(0, 0, this.canvasHover.getWidth(), this.canvasHover.getHeight());
                this.player.placeShip(pos, this.alignment, this.shipLength[alignment]);
            }
        });
    }

    public MousePlaceHandler (int mode, ResizableCanvas canvasHoverShoot, Player player) {
        this.mode = mode;
        this.player = player;
        this.canvasHover = canvasHoverShoot;
        this.hover = this.canvasHover.getGraphicsContext2D();
        this.canvasHover.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && valuePos == 1) {
                hover.clearRect(0, 0, this.canvasHover.getWidth(), this.canvasHover.getHeight());
                valuePos = 0;
                if (this.player instanceof RemotePlayer) {
                    try {
                        this.player.shoot(pos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        this.player.shoot(pos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void handle (MouseEvent ev){
        double x = ev.getX();
        double y = ev.getY();

        double width = canvasHover.getWidth();
        double height = canvasHover.getHeight();

        hover.clearRect(0, 0, canvasHover.getWidth(), canvasHover.getHeight());
        hover.setFill(Color.GREEN);

        if (x > (120.0/1920)*width && x < (840.0/1920)*width && mode == 0) {
            int [] temp = new int[2];

            int posX = (int)((x-((120.0/1920)*width))/((72.0/1920)*width));
            int posY = (int)(y/((72.0/1080)*(height*(3./2))));

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
                valuePos = 0;
            } else {valuePos = 1; pos[0] = posX; pos[1] = posY;}

            hover.fillRect((120.0/1920)*width+((72.0/1920)*width*posX),
                    ((72.0/1080)*(height*(3./2)))*posY,
                    (72.0/1920)*width*shipLength[0], (72.0/1080)*(height*(3./2))*shipLength[1]);
            if (temp[1] > 0) {
                shipLength[temp[0]] = temp[1];
            }
        } else if (x > (120.0/1920)*width+(width/2) && x < (840.0/1920)*width+(width/2) && mode == 1) {
            int posX = (int)((x-((120.0/1920)*width+(width/2)))/((72.0/1920)*width));
            int posY = (int)(y/((72.0/1080)*(height*(3./2))));

            int [] field = {posX, posY};
            if (player.myArea.giveField(field) == 'X' || player.myArea.giveField(field) == 'O') {
                hover.setFill(Color.RED);
                valuePos = 0;
            } else {valuePos = 1; pos[0] = posX; pos[1] = posY;}

            hover.fillRect((120.0/1920)*width+(width/2)+((72.0/1920)*width*posX),
                    (72.0/1080)*(height*(3./2))*posY,
                    (72.0/1920)*width, (72.0/1080)*(height*(3./2)));
        } else {
            valuePos = 0;
        }
    }
}