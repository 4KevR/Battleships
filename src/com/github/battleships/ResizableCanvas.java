package com.github.battleships;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class ResizableCanvas extends Canvas {
    String[] textBoardX = {"", " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10"};
    String[] textBoardY = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    public ResizableCanvas(int mode) {
        widthProperty().addListener(evt -> draw(mode));
        heightProperty().addListener(evt -> draw(mode));
    }

    private void draw(int mode) {
        double width = getWidth();
        double height = getHeight();
        if (width != 0 && height != 0) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);
            gc.setLineWidth(3);
            if (mode == 0) {
                gc.setFont(new Font("Times New Roman", (36.0 / 1920) * width));

                for (double grid = 0; grid <= (960.0 / 1920) * width; grid += (960.0 / 1920) * width) {
                    double x = (120.0 / 1920) * width + grid;
                    double y = (180.0 / 1080) * height;
                    for (; x < (840.0 / 1920) * width + grid + 1; x += (72.0 / 1920) * width) {
                        gc.setStroke(Color.BLACK);
                        gc.strokeLine(x, y, x, y + (720.0 / 1080) * height);
                        gc.setStroke(Color.BLUE);
                        gc.strokeText(textBoardX[(int) Math.round((x - ((120.0 / 1920) * width + grid)) / ((72.0 / 1920) * width))],
                                x - ((60.0 / 1920) * width), y - ((20.0 / 1080) * height));
                    }
                    x -= (72.0 / 1920) * width;
                    for (; y < (900.0 / 1080) * height + 1; y += (72.0 / 1080) * height) {
                        gc.setStroke(Color.BLACK);
                        gc.strokeLine(x - ((720.0 / 1920) * width), y, x, y);
                        gc.setStroke(Color.BLUE);
                        gc.strokeText(textBoardY[(int) Math.round((y - ((180.0 / 1080) * height)) / ((72.0 / 1080) * height))],
                                x - ((760.0 / 1920) * width), y - ((25.0 / 1080) * height));
                    }
                }
            }
        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}