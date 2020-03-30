package com.github.battleships;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;


public class MouseFieldHover implements EventHandler<MouseEvent> {
    ResizableCanvas canvasHover;
    GraphicsContext hover;

    public MouseFieldHover(ResizableCanvas canvasHover) {
        this.canvasHover = canvasHover;
        hover = canvasHover.getGraphicsContext2D();
    }

    public void handle(MouseEvent ev){
        double x = ev.getX();
        double y = ev.getY();

        double width = canvasHover.getWidth();
        double height = canvasHover.getHeight();

        hover.clearRect(0, 0, canvasHover.getWidth(), canvasHover.getHeight());

        if(x > (120.0/1920)*width && x < (840.0/1920)*width && y > (180.0/1080)*height && y < (900.0/1080)*height) {
            hover.setFill(Color.GREEN);
            hover.fillRect((120.0/1920)*width+((72.0/1920)*width*(int)((x-((120.0/1920)*width))/((72.0/1920)*width))),
                    (180.0/1080)*height+((72.0/1080)*height*(int)((y-((180.0/1080)*height))/((72.0/1080)*height))),
                    (72.0/1920)*width, (72.0/1080)*height);
            hover.setFill(Color.RED);
            hover.fillOval(x-((20.0/1920)*width/2), y-((20.0/1920)*width/2), (20.0/1920)*width, (20.0/1080)*height);
        }
    }
}