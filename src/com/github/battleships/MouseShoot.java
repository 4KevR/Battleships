package com.github.battleships;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class MouseShoot implements EventHandler<MouseEvent> {
    ResizableCanvas canvasHoverShoot;
    GraphicsContext hover;
    Player player;
    int valueShot = 0;
    int [] shootPos = new int[2];

    public MouseShoot (ResizableCanvas canvasHoverShoot, Player player) {
        this.player = player;
        this.canvasHoverShoot = canvasHoverShoot;
        this.hover = this.canvasHoverShoot.getGraphicsContext2D();
        this.canvasHoverShoot.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && valueShot == 1) {
                hover.clearRect(0, 0, this.canvasHoverShoot.getWidth(), this.canvasHoverShoot.getHeight());
                valueShot = 0;
                this.player.shoot(shootPos);
            }
        });
    }

    public void handle (MouseEvent ev) {
        double x = ev.getX();
        double y = ev.getY();

        double width = canvasHoverShoot.getWidth();
        double height = canvasHoverShoot.getHeight();

        hover.clearRect(0, 0, canvasHoverShoot.getWidth(), canvasHoverShoot.getHeight());

        if (x > (120.0/1920)*width+(width/2) && x < (840.0/1920)*width+(width/2)) {
            hover.setFill(Color.GREEN);

            int posX = (int)((x-((120.0/1920)*width+(width/2)))/((72.0/1920)*width));
            int posY = (int)(y/((72.0/1080)*(height*(3./2))));

            int [] field = {posX, posY};
            if (player.myArea.giveField(field) == 'X' || player.myArea.giveField(field) == 'O') {
                hover.setFill(Color.RED);
                valueShot = 0;
            } else {valueShot = 1; shootPos[0] = posX; shootPos[1] = posY;}

            hover.fillRect((120.0/1920)*width+(width/2)+((72.0/1920)*width*posX),
                    (72.0/1080)*(height*(3./2))*posY,
                    (72.0/1920)*width, (72.0/1080)*(height*(3./2)));
        } else {
            valueShot = 0;
        }
    }
}
