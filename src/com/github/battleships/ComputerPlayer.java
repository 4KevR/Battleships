package com.github.battleships;

import java.util.Random;

public class ComputerPlayer extends Player {
    public int difficulty;

    public ComputerPlayer(int[] shipLengths, int difficulty) {
        super("Computer", shipLengths);
        this.difficulty = difficulty;
        this.placeShipComputer();
    }

    public void placeShipComputer () {
        int success = 0;
        this.myArea.initArea();
        for (int ship: shipLengths) {
            int counterTry = 0;
            success = 0;
            while (counterTry < 20) {
                int alignment = new Random().nextInt(2);
                int posX;
                int posY;
                if (alignment == 0) {
                    posX = new Random().nextInt(10 - ship);
                    posY = new Random().nextInt(10);
                } else {
                    posX = new Random().nextInt(10);
                    posY = new Random().nextInt(10 - ship);
                }
                int countShips = 0;
                for (int counter = 0; counter < ship; counter++) {
                    int[] field = {posX, posY};
                    if (alignment == 0) {
                        field[0] += counter;
                    } else {
                        field[1] += counter;
                    }
                    countShips += this.countShips(field);
                }
                if (countShips == 0) {
                    int [] field = {posX, posY};
                    this.placeShip(field, alignment, ship);
                    success = 1;
                    break;
                } else {
                    counterTry++;
                }
            }
            if (success == 0) {
                break;
            }
        }
        if (success == 1) {
            this.myArea.showArea();
            placedShips = numberOfShipPoints;
        } else {
            this.placeShipComputer();
        }
    }
}
